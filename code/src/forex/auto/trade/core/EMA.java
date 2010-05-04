package forex.auto.trade.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

	public void update(int size) {
		int i = size;
		while (i >= 1) {
			hist[i] = hist[i - 1];
			i--;
		}

		if (size <= time_period ) {
			BigDecimal a = new BigDecimal(2* candles[0].getClose()+ (size -1)*hist[1]);
			BigDecimal b = new BigDecimal(size+1);
			BigDecimal ema = a.divide(b,7,RoundingMode.HALF_UP);
			hist[0] = ema.doubleValue();
		} else {
			BigDecimal a = new BigDecimal(2* candles[0].getClose()+ (time_period -1)*hist[1]);
			BigDecimal b = new BigDecimal(time_period+1);
			BigDecimal ema = a.divide(b,7,RoundingMode.HALF_UP);
			hist[0] = ema.doubleValue();
		}

	}

	public void init(TimeSeriseConfig config) {
		candles = config.getCandles();
		int tCount = config.maxTickCount();
		hist = new double[tCount];
	}
}
