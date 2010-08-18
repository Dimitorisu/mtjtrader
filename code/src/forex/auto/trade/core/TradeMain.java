package forex.auto.trade.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.ea.MyEA;
import forex.auto.trade.lib.Candle;

public class TradeMain {

	private static Log log = LogFactory.getLog(TradeMain.class);
	private static TradeService ts = null;

	public static void start() {
		ts = TradeService.getInstance();
		ts.addEa(new MyEA());
		ts.start();
	}
	
	public static void stop() {
		
	}

	public static int syncData(int time, double open, double low, double high,
			double close) {
		if (ts != null) {
			Candle c = new Candle();
			c.setTime(time * 1000); // change from second to ms.
			c.setOpen(open);
			c.setLow(low);
			c.setHigh(high);
			c.setClose(close);
			ts.addData(c);
		}
		return 0;
	}

	public static int doTrade(double ask, double bid) {


		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TradeService ts = TradeService.getInstance();
		FilePriceProvider dp = new FilePriceProvider();
		dp.init();
		// ts.addDataProvider(dp);
	
		

		ts.start();
		// 
		while (true) {
			Candle candle = dp.read();
			if (candle == null) {
				break;
			}
			ts.addData(candle);
			ts.run();
		}

		ts.stop();

	}

}
