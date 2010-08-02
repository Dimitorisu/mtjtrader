package forex.auto.trade.core;

import forex.auto.trade.TradeHelper;
import forex.auto.trade.lib.Candle;

public class Stochastic extends TradeHelper implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	BarValue k = null;
	BarValue signal = null;

	BarValue sumlow = null;
	BarValue sumhigh = null;

	int k_period = 5;
	int d_period = 3;
	int slow_period = 3;

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
			return k.getValue(shift);
		} else if (mode == MODE_SIGNAL) {
			return signal.getValue(shift);
		} else {
			return k.getValue(shift);
		}
	}

	public void start() {
		
		
		// already load new candle.index recalculate.

		TimeSeriseConfig config = this.getContext();
		int bars = config.bars();
		// signal_line.update(size);
		Candle current = config.getCandle(0);
		double lowestPrice = config.iLowest(PRICE_LOW, k_period, 0).getLow();
		double highestPrice = config.iHighest(PRICE_HIGH, k_period, 0)
				.getHigh();

	
		double close = current.getClose();

		int countIndex = this.unCountedBars();
		if (countIndex > 0) {

			sumlow.newValue(close - lowestPrice);
			sumhigh.newValue(highestPrice - lowestPrice);

		} else {
			sumlow.setValue(0, close - lowestPrice);
			sumhigh.setValue(0, highestPrice - lowestPrice);
		}

		double lsum = 0;
		double hsum = 0;
		int sp_end = slow_period > bars ? bars : slow_period;
		
		for (int j = 0; j < sp_end; j++) {
			lsum = lsum + sumlow.getValue(j);
			hsum = hsum + sumhigh.getValue(j);
		}
		

		if (hsum == 0) {
			if (countIndex > 0) {
				k.newValue(100.0);
			} else {
				k.setValue(0, 100.0);
			}
		} else {
			if (countIndex > 0) {
				k.newValue(lsum * 100 / hsum);
			} else {
				k.setValue(0, lsum * 100 / hsum);
			}
		}

		double sum = 0;
		int dp_end = d_period > bars ? bars : d_period;
		for (int j = 0; j < dp_end; j++) {
			sum = sum + k.getValue(j);
		}

		if (countIndex > 0) {
			signal.newValue(sum / d_period);
		} else {
			signal.setValue(0, sum / d_period);
		}
	}

	public void init() {
		TimeSeriseConfig config = this.getContext();
		int tCount = config.maxTickCount();

		k = new BarValue(tCount);
		signal = new BarValue(tCount);

		sumlow = new BarValue(tCount);
		sumhigh = new BarValue(tCount);
	}
}
