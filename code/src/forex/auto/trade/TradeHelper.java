package forex.auto.trade;

import forex.auto.trade.core.Indicator;
import forex.auto.trade.core.TimeSeriseContext;

public abstract class TradeHelper implements Indicator {

	public final static int ONE_MIN = 60000;
	public final static int FIVE_MIN = 300000;
	public final static int FIFTTH_MIN = 15 * 60000;
	public final static int HALF_HOUR = 30 * 60000;
	public final static int ONE_HOUR = 60 * 60000;
	public final static int FOUR_HOUR = 240 * 60000;
	public final static int ONE_DAY = 24 * 60 * 60000;

	public static int PRICE_CLOSE = 0;
	public static int PRICE_OPEN = 1;
	public static int PRICE_HIGH = 2;
	public static int PRICE_LOW = 3;

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;

	TimeSeriseContext tCtx = null;
	
	public abstract void start();

	public abstract void init();

	public void destroy() {};
	
	
	public void init(TimeSeriseContext config) {
		
		tCtx = config;
		
		init();
		
	}
	
	
//	public void execute() {
//		start();
//		countedBars = tCtx.bars();
//	}
	
	
	
	
	public TimeSeriseContext getContext() {
		return tCtx;
	}
	
	
	/*
	private static HashMap<IndicatorKey, MACD> macd = new HashMap<IndicatorKey, MACD>();

	public static double iMACD(String symbol, int timeframe,
			int fast_ema_period, int slow_ema_period, int signal_period,
			int applied_price, int mode, int shift) {

		IndicatorKey key = new IndicatorKey(
				((31 * fast_ema_period) * 31 + slow_ema_period) * 31
						+ signal_period);

		MACD macdI = macd.get(key);
		if (macdI == null) {
			TradeService trader = TradeService.getInstance();
			TimeSerise ts = trader.getTimeSerise(timeframe);
			macdI = new MACD(fast_ema_period, slow_ema_period, signal_period);
			ts.registerIndicator(macdI);
			macd.put(key, macdI);
		}
		return macdI.value(mode, shift);
	}
	*/
	
	
}
