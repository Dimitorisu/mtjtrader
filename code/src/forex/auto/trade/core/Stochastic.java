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
		// signal_line.update(size);
		Candle current = config.getCandle(0);
		double low = current.getLow();
		double high = current.getHigh();
		if (low < this.lowestPrice) {
			this.lowestPrice = low;
			this.lowestIndex = 0;
		} else if (lowestIndex == k_period) {
			double min = low;
			lowestIndex = 0;
			for (int j = 1; j < k_period; j++) {
				double l = config.getCandle(j).getLow();
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
				double h = config.getCandle(j).getHigh();
				if (h > max) {
					max = h;
					highestIndex = j;
				}
			}
			this.highestPrice = max;
		}

		
		double close = current.getClose();

		int countIndex = this.unCountedBars();
		if(countIndex >0) {
		
		sumlow.newValue(close - this.lowestPrice);
		sumhigh.newValue( this.highestPrice - this.lowestPrice);
		
		} else {
			sumlow.setValue(0,close - this.lowestPrice);
			sumhigh.setValue(0, this.highestPrice - this.lowestPrice);
		}

		double lsum = 0;
		double hsum = 0;
		for (int j = 0; j < slow_period; j++) {
			lsum = lsum + sumlow.getValue(j);
			hsum = hsum + sumhigh.getValue(j);
		}

		if (hsum == 0){
			if(countIndex >0) {
			k.newValue(100.0);
			}else {
				k.setValue(0,100.0);
			}
		}else {
			if(countIndex >0) {
			k.newValue(lsum * 100 / hsum);
			} else {
				k.setValue(0,lsum * 100 / hsum);
			}
		}

		double sum = 0;
		for (int j = 0; j < d_period; j++) {
			sum = sum + k.getValue(j);
		}
		// BigDecimal m = new BigDecimal(sum);
		// BigDecimal n = new BigDecimal(d_period);
		// BigDecimal signal_curr = m.divide(n, 4, RoundingMode.HALF_UP);
		signal.newValue(sum / d_period);
		
		if(countIndex >0) {
			signal.newValue(sum / d_period);
		} else {
			signal.setValue(0,sum / d_period);
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
