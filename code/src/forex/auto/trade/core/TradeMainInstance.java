package forex.auto.trade.core;

import java.util.ArrayList;
import java.util.Properties;

import net.wimpi.telnetd.TelnetD;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.Order;
import forex.auto.trade.OrderManager;
import forex.auto.trade.ea.MyEA;
import forex.auto.trade.lib.Candle;
import forex.auto.trade.lib.TerminalShell;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TradeMainInstance tm = new TradeMainInstance();
		tm.start();

		FilePriceProvider dp = new FilePriceProvider();
		dp.init();
		// ts.addDataProvider(dp);

		// 
		while (true) {
			Candle candle = dp.read();
			if (candle == null) {
				break;
			}

			long time = candle.getTime();
			double open = candle.getOpen();
			double low = candle.getLow();
			double high = candle.getHigh();
			double close = candle.getClose();

			tm.syncData((int) (time / 1000), open, low, high, close);
			String cmd = tm.doTrade(close, close);
			if (cmd != null)
				System.err.println("send order:" + cmd);
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tm.stop();

	}

}
