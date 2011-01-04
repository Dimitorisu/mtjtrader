package org.jtrader.core;

import java.util.ArrayList;
import java.util.Properties;

import net.wimpi.telnetd.TelnetD;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jtrader.data.provider.FilePriceProvider;
import org.jtrader.smi.TerminalShell;

import forex.auto.trade.ea.MyEA;

public class TradeMainInstance {

	private static Log log = LogFactory.getLog(TradeMainInstance.class);
	private TradeService ts = null;
	TelnetD daemon = null;

	private static TradeMainInstance instance = new TradeMainInstance();

	private TradeMainInstance() {
	}

	public static TradeMainInstance getInstance() {

		return instance;
	}

	public void start() {

		try {
			Properties props = new Properties();

			props.load(TerminalShell.class
					.getResourceAsStream("/telnetd.properties"));

			daemon = TelnetD.getReference();
			if (daemon == null) {
				// 1. create singleton instance
				daemon = TelnetD.createTelnetD(props);
				// 2.start serving
				daemon.start();

			}

			ts = TradeService.getInstance();
			ts.addEa(new MyEA());
			ts.start();
			if (log.isInfoEnabled()) {
				log.info("JTrade is started!");
			}

		} catch (Throwable e) {
			log.error("Start jtrade error!", e);
		}

	}

	public void stop() {

		daemon.stop();

	}

	public int syncData(int time, double open, double low, double high,
			double close) {
		try {
			if (ts != null) {

				Candle c = new Candle();
				c.setTime(((long) time) * 1000); // change from second to ms.
				c.setOpen(open);
				c.setLow(low);
				c.setHigh(high);
				c.setClose(close);

				ts.addData(c);
				ts.run();
			}
		} catch (Throwable t) {
			if (log.isErrorEnabled()) {
				log.error("Do SyncOrder error!)", t);
			}
		}

		return 0;
	}

	public int doSyncOrder(int orderTicket, int type, double volume,
			double price, double stoploss, double profit) {
		try {
			OrderManager om = OrderManager.getInstance();
			Order order = om.findOrder(orderTicket);
			if (order == null) {
				Order newOrder = new Order(orderTicket, type, volume, price,
						stoploss, profit);
				om.syncOrder(newOrder);

			} else {
				order.setPrice(price);
				order.setProfit(profit);
				order.setStoploss(stoploss);
				order.setType(type);
				order.setVolume(volume);
			}
		} catch (Throwable t) {
			if (log.isErrorEnabled()) {
				log.error("Do SyncOrder error!)", t);
			}
			return -1;
		}
		return 0;

	}

	public String doTrade(double ask, double bid) {

		String ret = null;
		try {
			OrderManager om = OrderManager.getInstance();
			om.clearSyncState();
			ts.trade(ask, bid);
			ArrayList<String> cmdList = om.getOrderCMD();
			if(cmdList.size()>0) {
				ret = cmdList.get(0);
				cmdList.remove(0);
			}
			
			return ret;
		} catch (Throwable t) {
			if (log.isErrorEnabled()) {
				log.error("Do trade error!)", t);
			}
		}
		return null;

	}

	

}
