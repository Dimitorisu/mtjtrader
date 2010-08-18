package forex.auto.trade.core;

import forex.auto.trade.TradeHelper;

public class Bands extends TradeHelper implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	public static int MODE_UPPER = 2;
	public static int MODE_LOWER = 5;
	BarValue main = null;
	BarValue upper = null;
	BarValue lower = null;
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
			return main.getValue(shift);
		} else if (mode == MODE_UPPER) {
			return upper.getValue(shift);
		} else if (mode == MODE_LOWER) {
			return lower.getValue(shift);
		} else {
			return 0;
		}

	}

	public void start() {

		TimeSeriseContext config = this.getContext();
		int size = config.bars();

		int period = time_period > size ? size : time_period;

		double sum = 0;
		for (int j = 0; j < period; j++) {
			sum += config.getCandle(j).getClose();
		}
		double mainValue = sum / period;

		double dsum = 0;
		for (int j = 0; j < period; j++) {
			double a = config.getCandle(j).getClose() * period - sum;
			dsum += a * a;
		}

		double d = period * period * period;
		double diff = StrictMath.sqrt(dsum / d) * this.deviation;

		if (config.unCountedBars() > 0) {
			main.newValue(mainValue);
			upper.newValue(mainValue + diff);
			lower.newValue(mainValue - diff);
		} else {
			main.setValue(0, mainValue);
			upper.setValue(0, mainValue + diff);
			lower.setValue(0, mainValue - diff);
		}
	}

	public void init() {
		TimeSeriseContext config = this.getContext();
		int tCount = config.maxTickCount();
		main = new BarValue(tCount);
		upper = new BarValue(tCount);
		lower = new BarValue(tCount);
	}

}
