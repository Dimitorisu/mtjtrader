package forex.auto.trade.core;

import forex.auto.trade.lib.Candle;

public class EMA implements Indicator {

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

	public void update(int size, boolean newTick) {
		int i = size;
		if (newTick) {
			while (i >= 1) {
				hist[i] = hist[i - 1];
				i--;
			}
		}

		if (size <= time_period) {
			double a = 2 * candles[0].getClose() + (size - 1) * hist[1];
			hist[0] = a / (size + 1);
		} else {
			double a = 2 * candles[0].getClose() + (time_period - 1) * hist[1];
			hist[0] = a / (time_period + 1);
		}

	}

	public void init(TimeSeriseConfig config) {
		candles = config.getCandles();
		int tCount = config.maxTickCount();
		hist = new double[tCount];
	}
}
