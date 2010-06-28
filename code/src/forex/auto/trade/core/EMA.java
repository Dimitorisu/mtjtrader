package forex.auto.trade.core;

import forex.auto.trade.TradeHelper;
import forex.auto.trade.lib.Candle;

public class EMA extends TradeHelper implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	double[] hist = null;
	Candle[] candles = null;
	int time_period = 9;

	public EMA() {
	}

	public EMA(int _time_period) {
		this.time_period = _time_period;
	}

	public double value(int shift) {

		return hist[shift];

	}

	public void start() {
		TimeSeriseConfig config = this.getContext();
		
		int unCounted = size;
		if (unCounted >0) {
			while (unCounted >= 1) {
				hist[unCounted] = hist[unCounted - 1];
				unCounted--;
			}
		}

		if (config.bars() <= time_period) {
			
			double a = 2 * candles[0].getClose() + (size - 1) * hist[1];
			hist[0] = a / (size + 1);
		} else {
			double a = 2 * candles[0].getClose() + (time_period - 1) * hist[1];
			hist[0] = a / (time_period + 1);
		}

	}

	public void init() {
		TimeSeriseConfig config = this.getContext();
		candles = config.getCandles();
		int tCount = config.maxTickCount();
		hist = new double[tCount];
	}
}
