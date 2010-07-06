package forex.auto.trade.core;

import forex.auto.trade.TradeHelper;

public class MACD extends TradeHelper implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	BarValue macd = null;
	BarValue signal = null;
	BarValue hist = null;
	int fast_ema_period = 12;
	int slow_ema_period = 26;
	int signal_period = 9;

	EMA fast_line = null;
	EMA slow_line = null;

	// EMA signal_line = null;

	public MACD() {
	}

	public MACD(int _fast_ema_period, int _slow_ema_period, int _signal_period) {
		this.fast_ema_period = _fast_ema_period;
		this.slow_ema_period = _slow_ema_period;
		this.signal_period = _signal_period;

	}

	public double value(int mode, int shift) {
		if (mode == MODE_MAIN) {
			return macd.getValue(shift);
		} else if (mode == MODE_SIGNAL) {
			return signal.getValue(shift);
		} else {
			return fast_line.value(shift) - slow_line.value(shift);
		}
	}

	public void start() {
		TimeSeriseConfig config = this.getContext();

		fast_line.start();
		slow_line.start();
		int sumbars = config.bars() < signal_period ? config.bars()
				: signal_period;

		int countIndex = this.unCountedBars();

		if (countIndex == 0) {
			macd.setValue(0, fast_line.value(0) - slow_line.value(0));

			double sum = 0;
			for (int j = 0; j < sumbars; j++) {
				sum = sum + macd.getValue(j);
			}
			signal.setValue(0, sum / signal_period);
		} else {

//			System.out.println("fast_line.value(0) - slow_line.value(0)="
//					+ (slow_line.value(0)));
			macd.newValue(fast_line.value(0) - slow_line.value(0));

			double sum = 0;
			for (int j = 0; j < sumbars; j++) {
				sum = sum + macd.getValue(j);
			}
			signal.newValue(sum / signal_period);

		}

	}

	public void init() {
		TimeSeriseConfig config = this.getContext();
		int tCount = config.maxTickCount();

		fast_line = new EMA(this.fast_ema_period);
		fast_line.init(config);

		slow_line = new EMA(this.slow_ema_period);
		slow_line.init(config);

		// signal_line = new EMA(this.signal_period);
		// signal_line.init(config);

		macd = new BarValue(tCount);
		signal = new BarValue(tCount);
		// hist = new double[tCount];
	}
}
