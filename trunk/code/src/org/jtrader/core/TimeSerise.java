package org.jtrader.core;

import java.util.ArrayList;



public class TimeSerise {
	public final static int ONE_MIN = 60000;
	public final static int FIVE_MIN = 300000;
	public final static int FIFTTH_MIN = 15 * 60000;
	public final static int HALF_HOUR = 30 * 60000;
	public final static int ONE_HOUR = 60 * 60000;
	public final static int FOUR_HOUR = 240 * 60000;
	public final static int ONE_DAY = 24 * 60 * 60000;

	private int timeFrame;

	private ArrayList<Indicator> indicators = null;
	TimeSeriseContext cfg = null;

	private TimeSerise(int _timeFrame, int _size) {
		this.timeFrame = _timeFrame;
		Candle[] candles = new Candle[_size];
		cfg = new TimeSeriseContext(candles);

	}
	
	public TimeSeriseContext getContext() {
		return cfg;
	}

	public static TimeSerise createTimeSerise(int _timeFrame, int _size) {
		return new TimeSerise(_timeFrame, _size);
	}

	public static TimeSerise createTimeSerise(int _timeFrame) {
		return new TimeSerise(_timeFrame, 2000);
	}
	

	public Candle getCandle(int index) {
		return cfg.getCandle(index);
	}

	private void newCandle(Candle newOne) {
		cfg.newCandle(newOne);
	}

	public long updateCandle(Candle newOne) {

		long newTime = newOne.getTime();
		long difftime = newTime + (8 * 60 * 60000); // the offset of java time.
		long newDiffTime = difftime - (difftime % timeFrame);

		long inputTime = newDiffTime - (8 * 60 * 60000);

		Candle recordNow = getCandle(0);
		// System.out.println("recordNow time:"
		// + new Date(recordNow.getTime()) + ",the input time:"
		// + new Date(inputTime));

		if (recordNow == null) {

			Candle newCandel = new Candle();
			newCandel.setTime(inputTime);
			newCandel.setOpen(newOne.getOpen());
			newCandel.setClose(newOne.getClose());
			newCandel.setHigh(newOne.getHigh());
			newCandel.setLow(newOne.getLow());
			newCandle(newCandel);

		} else if (recordNow.getTime() == inputTime) { // mergin
			// the
			// candle data.
			// recordNow.setOpen(newOne.getOpen());
			recordNow.setClose(newOne.getClose());
			recordNow.updateHigh(newOne.getHigh());
			recordNow.updateLow(newOne.getLow());
			// candleCount++;

		} else if (recordNow.getTime() < inputTime) {

			Candle newCandel = new Candle();
			newCandel.setTime(inputTime);
			newCandel.setOpen(newOne.getOpen());
			newCandel.setClose(newOne.getClose());
			newCandel.setHigh(newOne.getHigh());
			newCandel.setLow(newOne.getLow());
			newCandle(newCandel);

		} else {
			return -recordNow.getTime();

			// a time of history, must not happen. seek a right position.
			// int i = 1;
			// while (i < candles.length) {
			// long t = candles[i].getTime();
			// if (t > inputTime) {
			// i++;
			// continue;
			// } else if (t == inputTime) {
			//
			// recordNow.setClose(newOne.getClose());
			// recordNow.updateHigh(newOne.getHigh());
			// recordNow.updateLow(newOne.getLow());
			// // candleCount = i + 1;
			//
			// counted = i + 1;
			// break;
			// } else {
			//
			// int j = candles.length - 1;
			//
			// while (j > i) {
			// candles[j] = candles[j - 1];
			// j--;
			// }
			//
			// Candle newCandel = new Candle();
			// newCandel.setTime(inputTime);
			// newCandel.setOpen(newOne.getOpen());
			// newCandel.setClose(newOne.getClose());
			// newCandel.setHigh(newOne.getHigh());
			// newCandel.setLow(newOne.getLow());
			// candles[i] = newCandel;
			//
			// cfg.increaseBar();
			//
			// counted = candleCount;
			// break;
			// }
			//
			// }
		}

		return inputTime;
	}

	public void start() {
		if (indicators != null) {
			for (Indicator idc : indicators) {
				idc.start();
			}
		}
		cfg.countedBars();
	}


	public void registerIndicator(Indicator _indicator) {
		if (indicators == null) {
			indicators = new ArrayList<Indicator>();
		}
		_indicator.init(cfg);
		indicators.add(_indicator);
	}

	public Indicator getIndicator() {
		return indicators.get(0);
	}

}
