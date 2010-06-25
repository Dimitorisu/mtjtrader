package forex.auto.trade.core;

import forex.auto.trade.ea.MyEA;
import forex.auto.trade.lib.Candle;

public class TradeMain {

	private static TradeService ts = null;

	public static void start() {
		ts = TradeService.getInstance();
		ts.addEa(new MyEA());
	}

	public static int syncData(long time, double open, double low, double high,
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

	public static int addData(int timeFrame, long time, double open,
			double low, double high, double close) {

		if (ts != null) {
			Candle c = new Candle();
			c.setTime(time * 1000); // change from second to ms.
			c.setOpen(open);
			c.setLow(low);
			c.setHigh(high);
			c.setClose(close);
			ts.addData(timeFrame, c);
		}

		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TradeService ts = TradeService.getInstance();
		FilePriceProvider dp = new FilePriceProvider();
		dp.init();
		ts.addDataProvider(dp);
		ts.addEa(new MyEA());
		ts.start();
		ts.run();
		ts.stop();

	}

}
