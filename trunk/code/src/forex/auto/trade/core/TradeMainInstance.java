package forex.auto.trade.core;

import java.util.Properties;

import net.wimpi.telnetd.TelnetD;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.ea.MyEA;
import forex.auto.trade.lib.Candle;
import forex.auto.trade.lib.TerminalShell;

public class TradeMainInstance {

	private static Log log = LogFactory.getLog(TradeMainInstance.class);
	private TradeService ts = null;
	TelnetD daemon = null;

	private static TradeMainInstance instance = new TradeMainInstance();

	private TradeMainInstance() {
		// TODO Auto-generated constructor stub
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
		
		Candle c = new Candle();
		c.setTime(((long) time )* 1000); // change from second to ms.
		c.setOpen(open);
		c.setLow(low);
		c.setHigh(high);
		c.setClose(close);
		if (log.isDebugEnabled()) {
			log.debug("sync:" + c);
		}
		
		if (ts != null) {
			ts.addData(c);
			ts.run();
		}
		return 0;
	}

	public int doTrade(double ask, double bid) {

		if (daemon == null) {
			// this.start();
			return (int) ask * 1000;
		}

		else
			return 1;
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
			tm.doTrade(0, 0);
		}

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tm.stop();

	}

}
