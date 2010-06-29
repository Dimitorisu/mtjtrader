package forex.auto.trade.core;

import forex.auto.trade.TradeHelper;

public class EMA extends TradeHelper implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	BarValue hist = null;
	int time_period = 9;

	public EMA() {
	}

	public EMA(int _time_period) {
		this.time_period = _time_period;
	}

	public double value(int shift) {

		return hist.getValue(shift);

	}

	public void start() {
		TimeSeriseConfig config = this.getContext();

		int countIndex = this.unCountedBars();
		int bars = config.bars();

		if (countIndex == 0) {
			int n = bars - countIndex;

			if (n <= time_period) {

				double a = 2 * config.getCandle(countIndex).getClose()
						+ (n - 1) * hist.getValue(countIndex + 1);
				hist.setValue(countIndex, a / (n + 1));
			} else {
				double a = 2 * config.getCandle(countIndex).getClose()
						+ (time_period - 1) * hist.getValue(countIndex + 1);
				hist.setValue(countIndex, a / (time_period + 1));
			}
		} else {
			while (countIndex > 0) {

				int n = bars - countIndex;

				if (n <= time_period) {

					double a = 2 * config.getCandle(countIndex).getClose()
							+ (n - 1) * hist.getValue(0);
					hist.newValue(a / (n + 1));
				} else {
					double a = 2 * config.getCandle(countIndex).getClose()
							+ (time_period - 1) * hist.getValue(0);
					hist.newValue(a / (time_period + 1));
				}

				countIndex--;

			}
		}

	}

	public void init() {
		TimeSeriseConfig config = this.getContext();
		int tCount = config.maxTickCount();
		hist = new BarValue(tCount);
	}
}
