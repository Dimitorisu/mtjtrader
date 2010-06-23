package forex.auto.trade.core;

import java.util.ArrayList;

import forex.auto.trade.TradeHelper;
import forex.auto.trade.lib.Candle;

public class TimeSerise {
	public final static int ONE_MIN = 60000;
	public final static int FIVE_MIN = 300000;
	public final static int FIFTTH_MIN = 15 * 60000;
	public final static int HALF_HOUR = 30 * 60000;
	public final static int ONE_HOUR = 60 * 60000;
	public final static int FOUR_HOUR = 240 * 60000;
	public final static int ONE_DAY = 24 * 60 * 60000;

	Candle[] candles = null;
	private int timeFrame;
	int point = 0;
	int arrayIndex = 0;
	private ArrayList<Indicator> indicators = null;

	private TimeSerise(int _timeFrame, int _size) {
		this.timeFrame = _timeFrame;
		this.arrayIndex = _size - 1;
		candles = new Candle[_size];
	}

	public static TimeSerise createTimeSerise(int _timeFrame, int _size) {
		return new TimeSerise(_timeFrame, _size);
	}

	public static TimeSerise createTimeSerise(int _timeFrame) {
		return new TimeSerise(_timeFrame, 2000);
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

	public Candle getCandles(int index) {
		return candles[index];
	}

	public void updateCandle(Candle newOne) {

		long newTime = newOne.getTime();
		long difftime = newTime + (8 * 60 * 60000); // the offset of java time.
		long newDiffTime = difftime - (difftime % timeFrame);

		long inputTime = newDiffTime - (8 * 60 * 60000);

		Candle recordNow = candles[0];
		// System.out.println("recordNow time:"
		// + new Date(recordNow.getTime()) + ",the input time:"
		// + new Date(inputTime));

		boolean newTick = false;

		if (recordNow == null) {
			Candle newCandel = new Candle();
			newCandel.setTime(inputTime);
			newCandel.setOpen(newOne.getOpen());
			newCandel.setClose(newOne.getClose());
			newCandel.setHigh(newOne.getHigh());
			newCandel.setLow(newOne.getLow());
			candles[0] = newCandel;
		} else if (recordNow.getTime() == inputTime) { // mergin
			// the
			// candle data.
			// recordNow.setOpen(newOne.getOpen());
			recordNow.setClose(newOne.getClose());
			recordNow.updateHigh(newOne.getHigh());
			recordNow.updateLow(newOne.getLow());

		} else if (recordNow.getTime() < inputTime) {

			int i = point;
			while (i >= 1) {
				candles[i] = candles[i - 1];
				i--;
			}

			Candle newCandel = new Candle();
			newCandel.setTime(inputTime);
			newCandel.setOpen(newOne.getOpen());
			newCandel.setClose(newOne.getClose());
			newCandel.setHigh(newOne.getHigh());
			newCandel.setLow(newOne.getLow());
			candles[0] = newCandel;
			newTick = true;

			if (point < arrayIndex)
				point++;
		} else { // a time of history, must not happen. seek a right position.
			int i = 1;
			while (i < candles.length) {
				long t = candles[i].getTime();
				if(t>inputTime) {
					i++;
					continue;
				} else if(t==inputTime) {
					
					recordNow.setClose(newOne.getClose());
					recordNow.updateHigh(newOne.getHigh());
					recordNow.updateLow(newOne.getLow());
					
					break;
				} else {
					
					int j = point;
					while (j >= i) {
						candles[j] = candles[j - 1];
						j--;
					}
					
					Candle newCandel = new Candle();
					newCandel.setTime(inputTime);
					newCandel.setOpen(newOne.getOpen());
					newCandel.setClose(newOne.getClose());
					newCandel.setHigh(newOne.getHigh());
					newCandel.setLow(newOne.getLow());
					candles[i] = newCandel;
					point++;
				}

			}
		}

		if (indicators != null) {

			for (Indicator idc : indicators) {
				idc.update(point, newTick);
			}

		}

	}


	public Candle iHighest(int type, int count, int start) {

		int end = count + start > point ? point : count + start;
		double highest = Double.MIN_VALUE;

		int highestIndex = 0;
		if (type == TradeHelper.PRICE_HIGH) {
			for (int i = start; i < end; i++) {
				double each = candles[i].getHigh();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_OPEN) {
			for (int i = start; i < end; i++) {
				double each = candles[i].getOpen();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_CLOSE) {
			for (int i = start; i < end; i++) {
				double each = candles[i].getClose();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_LOW) {
			for (int i = start; i < end; i++) {
				double each = candles[i].getLow();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		}

		return candles[highestIndex];
	}

	public Candle iLowest(int type, int count, int start) {

		int end = count + start > point ? point : count + start;
		double highest = Double.MIN_VALUE;

		int highestIndex = 0;
		if (type == TradeHelper.PRICE_LOW) {
			for (int i = start; i < end; i++) {
				double each = candles[i].getLow();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_CLOSE) {
			for (int i = start; i < end; i++) {
				double each = candles[i].getClose();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_OPEN) {
			for (int i = start; i < end; i++) {
				double each = candles[i].getOpen();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_HIGH) {
			for (int i = start; i < end; i++) {
				double each = candles[i].getHigh();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		}

		return candles[highestIndex];
	}

	public void registerIndicator(Indicator _indicator) {
		if (indicators == null) {
			indicators = new ArrayList<Indicator>();
		}
		TimeSeriseConfig cfg = new TimeSeriseConfig(candles);
		_indicator.init(cfg);
		indicators.add(_indicator);
	}

	public Indicator getIndicator() {
		return indicators.get(0);
	}

}
