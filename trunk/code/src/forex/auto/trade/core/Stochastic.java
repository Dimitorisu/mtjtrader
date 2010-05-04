package forex.auto.trade.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
	double highestPrice = -1;


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

	public double iMACD(String symbol, int timeframe, int fast_ema_period,
			int slow_ema_period, int signal_period, int applied_price,
			int mode, int shift) {

		return 0;

	}

	public void update(int size) {
		// already load new candle.index recalculate.
		highestIndex++;
		lowestIndex++;

		// signal_line.update(size);
		double low = candles[0].getLow();
		double high = candles[0].getHigh();
		if (low < this.lowestPrice) {
			this.lowestPrice = low;
			this.lowestIndex = 0;
		} else if (lowestIndex == k_period) {
			double min = Double.MAX_VALUE;
			for (int j = 0; j < k_period; j++) {
				double l = candles[j].getLow();
				if (l < min) {
					min = l;
					lowestIndex = j;

				}
			}
			this.lowestPrice = min;
		}

		if (high < this.highestPrice) {
			this.highestPrice = high;
			this.highestIndex = 0;
		} else if (highestIndex == k_period) {
			double max = Double.MIN_VALUE;
			for (int j = 0; j < k_period; j++) {
				double h = candles[j].getHigh();
				if (h > max) {
					max = h;
					highestIndex = j;
				}
			}
			this.highestPrice = max;
		}

		int i = size;
		while (i >= 1) {
			k[i] = k[i - 1];
			signal[i] = signal[i - 1];
			i--;
		}
		double close = candles[0].getClose();
		sumlow[0] = close - this.lowestPrice;
		sumhigh[0] = this.highestPrice - this.lowestPrice;
		
		double lsum =0;
		double hsum =0;
		for (int j = 0; j < slow_period; j++) {
			lsum = lsum + sumlow[j];
			hsum = hsum + sumhigh[j];
		}
		BigDecimal a = new BigDecimal(lsum*100);
		BigDecimal b = new BigDecimal(hsum);
		if (b.compareTo(BigDecimal.ZERO) == 0)
			k[0] = 100.0;
		else {
			BigDecimal main_curr = a.divide(b, 7, RoundingMode.HALF_UP);
			k[0] = main_curr.doubleValue();
		}

		double sum = 0;
		for (int j = 0; j < d_period; j++) {
			sum = sum + k[j];
		}
		BigDecimal m = new BigDecimal(sum);
		BigDecimal n = new BigDecimal(d_period);
		BigDecimal signal_curr = m.divide(n, 7, RoundingMode.HALF_UP);
		signal[0] = signal_curr.doubleValue();
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
