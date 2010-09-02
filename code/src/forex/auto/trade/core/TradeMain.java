package forex.auto.trade.core;


public class TradeMain {

	public TradeMain() {
	}

	public void start() {

		TradeMainInstance.getInstance().start();
	}

	public void stop() {

		TradeMainInstance.getInstance().stop();

	}

	public int syncData(int time, double open, double low, double high,
			double close) {
		TradeMainInstance inst = TradeMainInstance.getInstance();
		return inst.syncData(time, open, low, high, close);
	}

	public int doTrade(double ask, double bid) {
		TradeMainInstance inst = TradeMainInstance.getInstance();
		return inst.doTrade(ask, bid);
	}

}
