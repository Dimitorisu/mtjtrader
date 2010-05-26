package forex.auto.trade.core;

import forex.auto.trade.lib.Candle;

public class Bands implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	public static int MODE_UPPER = 2;
	public static int MODE_LOWER = 5;
	double[] main = null;
	double[] upper = null;
	double[] lower = null;
	Candle[] candles = null;
	int time_period = 20;
	private int deviation = 2;

	public Bands() {

	}

	public Bands(int _time_period, int _deviation) {
		this.time_period = _time_period;
		this.deviation = _deviation;
	}

	public double value(int mode, int shift) {

		if (mode == MODE_MAIN) {
			return main[shift];
		} else if (mode == MODE_UPPER) {
			return upper[shift];
		} else if (mode == MODE_LOWER) {
			return lower[shift];
		} else {
			return 0;
		}

	}

	public void update(int size, boolean newTick) {
		int i = size;
		if (newTick) {
			
			while (i >= 1) {
				main[i] = main[i - 1];
				upper[i] = upper[i - 1];
				lower[i] = lower[i - 1];
				i--;
			}
		}

		int period = time_period > size ? size : time_period;

		double sum = 0;
		for (int j = 0; j < period; j++) {
			sum += candles[j].getClose();
		}
		double mainValue = sum / period;

		double dsum = 0;
		for (int j = 0; j < period; j++) {
			double a = candles[j].getClose() * period - sum;
			dsum += a * a;
		}

		double d = period * period * period;
		double diff = StrictMath.sqrt(dsum / d) * this.deviation;

		main[0] = mainValue;
		upper[0] = mainValue + diff;
		lower[0] = mainValue - diff;
	}

	public void init(TimeSeriseConfig config) {
		candles = config.getCandles();
		int tCount = config.maxTickCount();
		main = new double[tCount];
		upper = new double[tCount];
		lower = new double[tCount];
	}

}
