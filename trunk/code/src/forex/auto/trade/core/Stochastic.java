package forex.auto.trade.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

import forex.auto.trade.lib.Candle;

public class Stochastic implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	double[] k = null;
	double[] d = null;
	double[] hist = null;
	Candle[] candles = null;
	int k_period = 5;
	int d_period = 3;
	int slow_period = 3;

	int lowestIndex = 0;
	int highestIndex = 0;
	double lowestPrice = Double.MAX_VALUE;
	double highestPrice = -1;

	EMA fast_line = null;
	EMA slow_line = null;

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
			return fast_line.value(shift) - slow_line.value(shift);
		} else if (mode == MODE_SIGNAL) {
			return d[shift];
		} else {
			return fast_line.value(shift) - slow_line.value(shift);
		}
	}

	public double iMACD(String symbol, int timeframe, int fast_ema_period,
			int slow_ema_period, int signal_period, int applied_price,
			int mode, int shift) {

		return 0;

	}

	public void update(int size) {
		
		// signal_line.update(size);
		double low = candles[0].getLow();
		double high = candles[0].getHigh();
		

		int i = size;
		while (i >= 1) {
			k[i] = k[i - 1];
			d[i] = d[i - 1];
			i--;
		}
		k[0] = fast_line.value(0) - slow_line.value(0);

		double sum = 0;
		for (int j = 0; j < slow_period; j++) {
			sum = sum + k[j];
		}
		BigDecimal a = new BigDecimal(sum);
		BigDecimal b = new BigDecimal(slow_period);
		BigDecimal signal_curr = a.divide(b, 7, RoundingMode.HALF_UP);
		d[0] = signal_curr.doubleValue();
	}

	public void init(TimeSeriseConfig config) {
		candles = config.getCandles();
		int tCount = config.maxTickCount();

		// signal_line = new EMA(this.signal_period);
		// signal_line.init(config);

		k = new double[tCount];
		d = new double[tCount];
		// hist = new double[tCount];
	}
}
