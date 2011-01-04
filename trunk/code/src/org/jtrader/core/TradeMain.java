package org.jtrader.core;

import org.jtrader.data.provider.FilePriceProvider;

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

	public String doTrade(double ask, double bid) {
		TradeMainInstance inst = TradeMainInstance.getInstance();
		return inst.doTrade(ask, bid);
	}

	public int doSyncOrder(int orderTicket, int type, double volume,
			double price, double stoploss, double profit) {
		TradeMainInstance inst = TradeMainInstance.getInstance();
		return inst.doSyncOrder(orderTicket, type, volume, price, stoploss,
				profit);
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
