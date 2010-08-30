package forex.auto.trade.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.ea.MyEA;
import forex.auto.trade.lib.Candle;

public class TradeMain {

	private static Log log = LogFactory.getLog(TradeMain.class);
	private TradeService ts = null;

	public TradeMain() {
		// TODO Auto-generated constructor stub
	}

	public void start() {
		try {
			ts = TradeService.getInstance();
			ts.addEa(new MyEA());
			ts.start();
			if (log.isInfoEnabled()) {
				log.info("JTrade is started!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stop() {

	}

	public int syncData(int time, double open, double low, double high,
			double close) {
		if (ts != null) {
			
			Candle c = new Candle();
			c.setTime(time * 1000); // change from second to ms.
			c.setOpen(open);
			c.setLow(low);
			c.setHigh(high);
			c.setClose(close);
			ts.addData(c);
			
			ts.run();
		}
		return 0;
	}

	public int doTrade(double ask, double bid) {

		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TradeMain tm = new TradeMain();
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
			int time = (int)candle.getTime()/1000;
			double open = candle.getOpen();
			double low = candle.getLow();
			double high = candle.getHigh();
			double close = candle.getClose();
			
			tm.syncData(time, open, low, high, close);
			tm.doTrade(0, 0);
		}

		tm.stop();

	}

}
