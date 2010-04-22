package forex.auto.trade.core;

import forex.auto.trade.lib.Candle;

public class TimeSerise {
	public final static int ONEMIN = 60000;
	public final static int FIVE_MIN = 300000;
	public final static int FIFTTH_MIN = 15 * 60000;
	public final static int HALF_HOUR = 30 * 60000;
	public final static int ONE_HOUR = 60 * 60000;
	public final static int FOUR_HOUR = 240 * 60000;
	public final static int ONE_DAY = 24 * 60 * 60000;

	Candle[] candles = null;
	private int timeFrame;
	int point = 0;
	private TimeSerise parent;

	private TimeSerise(int _timeFrame, int size, TimeSerise parent) {
		this.timeFrame = _timeFrame;
		candles = new Candle[size];
		this.parent = parent;
	}

	public static TimeSerise createTimeSerise(int _timeFrame) {
		return new TimeSerise(_timeFrame, 2000, null);
	}

	public static TimeSerise createTimeSerise(int _timeFrame, TimeSerise parent) {
		return new TimeSerise(_timeFrame, 2000, parent);
	}

	public Candle[] getCandles() {
		int max = 20;

		if (point < max) {
			max = point;
		}

		Candle[] ret = new Candle[max];
		for (int i = 0; i < max; i++) {
			ret[i] = candles[i];
		}
		return ret;

	}

	public void updateCandle(Candle newOne) {

		long newTime = newOne.getTime();
		long difftime = newTime + (8 * 60 * 60000); // the offset of java time.
		long newDiffTime = difftime - (difftime % timeFrame);

		long inputTime = newDiffTime - (8 * 60 * 60000);

		if (candles[0] != null) {
			Candle recordNow = candles[0];
			// System.out.println("recordNow time:"
			// + new Date(recordNow.getTime()) + ",the input time:"
			// + new Date(inputTime));

			if (recordNow.getTime() == inputTime) { // mergin the
				// candle data.
				//recordNow.setOpen(newOne.getOpen());
				recordNow.setClose(newOne.getClose());
				recordNow.updateHigh(newOne.getHigh());
				recordNow.updateLow(newOne.getLow());

			} else {
				for (int i = candles.length - 1; i >= 1; i--) {
					candles[i] = candles[i - 1];
				}
				Candle newCandel = new Candle();
				newCandel.setTime(inputTime);
				newCandel.setOpen(newOne.getOpen());
				newCandel.setClose(newOne.getClose());
				newCandel.setHigh(newOne.getHigh());
				newCandel.setLow(newOne.getLow());
				candles[0] = newCandel;
				point++;
			}

		} else {
			candles[0] = newOne;
		}

		if (parent != null)
			parent.updateCandle(newOne);
	}

}
