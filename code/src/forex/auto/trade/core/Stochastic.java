package forex.auto.trade.core;

import forex.auto.trade.lib.Candle;

public class Stochastic implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	double[] k = null;
	double[] signal = null;

	double[] sumlow = null;
	double[] sumhigh = null;

	Candle[] candles = null;
	int k_period = 5;
	int d_period = 3;
	int slow_period = 3;

	int lowestIndex = 0;
	int highestIndex = 0;
	double lowestPrice = Double.MAX_VALUE;
	double highestPrice = Double.MIN_VALUE;

	// EMA signal_line = null;

	public Stochastic() {
	}

	public Stochastic(int _k_period, int _d_period, int _slow_period) {
		this.k_period = _k_period;
		this.d_period = _d_period;
		this.slow_period = _slow_period;

	}

	public double value(int mode, int shift) {

		if (mode == MODE_MAIN) {
			return k[shift];
		} else if (mode == MODE_SIGNAL) {
			return signal[shift];
		} else {
			return k[shift];
		}
	}

	public void update(int size, boolean newTick) {
		// already load new candle.index recalculate.
		int i = size;
		if (newTick) {
			while (i >= 1) {
				k[i] = k[i - 1];
				signal[i] = signal[i - 1];
				sumlow[i] = sumlow[i - 1];
				sumhigh[i] = sumhigh[i - 1];
				i--;
			}
			highestIndex++;
			lowestIndex++;
		}
		
		// signal_line.update(size);
		double low = candles[0].getLow();
		double high = candles[0].getHigh();
		if (low < this.lowestPrice) {
			this.lowestPrice = low;
			this.lowestIndex = 0;
		} else if (lowestIndex == k_period) {
			double min = low;
			lowestIndex = 0;
			for (int j = 1; j < k_period; j++) {
				double l = candles[j].getLow();
				if (l < min) {
					min = l;
					lowestIndex = j;
				}
			}
			this.lowestPrice = min;
		}

		if (high > this.highestPrice) {
			this.highestPrice = high;
			this.highestIndex = 0;
		} else if (highestIndex == k_period) {
			double max = high;
			highestIndex = 0;
			for (int j = 1; j < k_period; j++) {
				double h = candles[j].getHigh();
				if (h > max) {
					max = h;
					highestIndex = j;
				}
			}
			this.highestPrice = max;
		}

		
		double close = candles[0].getClose();

		sumlow[0] = close - this.lowestPrice;
		sumhigh[0] = this.highestPrice - this.lowestPrice;

		double lsum = 0;
		double hsum = 0;
		for (int j = 0; j < slow_period; j++) {
			lsum = lsum + sumlow[j];
			hsum = hsum + sumhigh[j];
		}

		if (hsum == 0)
			k[0] = 100.0;
		else {
			k[0] = lsum * 100 / hsum;
		}

		double sum = 0;
		for (int j = 0; j < d_period; j++) {
			sum = sum + k[j];
		}
		// BigDecimal m = new BigDecimal(sum);
		// BigDecimal n = new BigDecimal(d_period);
		// BigDecimal signal_curr = m.divide(n, 4, RoundingMode.HALF_UP);
		signal[0] = sum / d_period;
	}

	public void init(TimeSeriseConfig config) {
		candles = config.getCandles();
		int tCount = config.maxTickCount();

		// signal_line = new EMA(this.signal_period);
		// signal_line.init(config);

		k = new double[tCount];
		signal = new double[tCount];

		sumlow = new double[tCount];
		sumhigh = new double[tCount];
	}
}
