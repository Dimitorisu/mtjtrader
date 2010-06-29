package forex.auto.trade;

import java.util.HashMap;

import forex.auto.trade.core.MACD;
import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TimeSeriseConfig;
import forex.auto.trade.core.TradeService;

public abstract class TradeHelper {

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

	TimeSeriseConfig tCtx = null;
	private int countedBars =0;
	
	public abstract void start();

	public abstract void init();

	public void destroy() {};
	
	
	public void init(TimeSeriseConfig config) {
		
		tCtx = config;
		
		init();
		
	}
	
	
	public void onPriceChange() {
		start();
		countedBars = tCtx.bars();
	}
	
	public int unCountedBars() {
		
		return tCtx.bars() - countedBars;
		
	}
	
	
	public TimeSeriseConfig getContext() {
		return tCtx;
	}
	
	
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
	
	public static TimeSerise getTimeSerise(int timeframe) {
		TradeService trader = TradeService.getInstance();
		TimeSerise ts = trader.getTimeSerise(timeframe);
		return ts;
	}
}
