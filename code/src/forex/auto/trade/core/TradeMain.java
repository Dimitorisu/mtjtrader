package forex.auto.trade.core;

import forex.auto.trade.ea.MyEA;

public class TradeMain {

	private static TradeService ts = null;

	public static void start() {
		ts = TradeService.getInstance();
		ts.addEa(new MyEA());
	}

	public double doTrade(long time,double ask, double bid) {
		return 0;
	}

	public int initData(int timeFrame, long time, double open, double low,
			double high, double close) {
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
