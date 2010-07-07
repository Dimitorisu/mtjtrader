package forex.auto.trade.core;

import forex.auto.trade.TradeHelper;
import forex.auto.trade.lib.Candle;

public class TimeSeriseConfig {

	Candle[] candles;
	int bars = 0;
	int updatedBars = 0;

	public TimeSeriseConfig(Candle[] _candles) {
		this.candles = _candles;
	}

	public int maxTickCount() {
		return candles.length;
	}

	public int getNewBars() {
		return updatedBars;
	}

	public void clearNewBars() {
		updatedBars = 0;
	}

	public Candle getCandle(int index) {
		try {
			int size = candles.length;
			if (index < size) {
				int reIndex = ((bars - index) % size);
				return candles[reIndex];
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("index:" + index);
			e.printStackTrace();
		}
		return null;
	}

	public void newCandle(Candle newOne) {
		increaseBar();
		int reIndex = bars % candles.length;
		candles[reIndex] = newOne;

	}

	// public Candle[] getCandles() {
	// return candles;
	// }
	//	
	public void increaseBar() {
		updatedBars++;
		bars++;
	}

	public void setBars(int _new) {
		this.bars = _new;
	}

	public int bars() {
		return bars;
	}

	public Candle iHighest(int type, int count, int start) {

		int end = count + start > bars ? bars : count + start;
		double highest = Double.MIN_VALUE;

		int highestIndex = 0;
		if (type == TradeHelper.PRICE_HIGH) {
			for (int i = start; i < end; i++) {
				double each = getCandle(i).getHigh();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_OPEN) {
			for (int i = start; i < end; i++) {
				double each = getCandle(i).getOpen();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_CLOSE) {
			for (int i = start; i < end; i++) {
				double each = getCandle(i).getClose();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_LOW) {
			for (int i = start; i < end; i++) {
				double each = getCandle(i).getLow();
				if (each > highest) {
					highest = each;
					highestIndex = i;
				}
			}
		}

		return getCandle(highestIndex);
	}

	public Candle iLowest(int type, int count, int start) {

		int end = count + start > bars ? bars : count + start;
		double lowest = Double.MAX_VALUE;

		int highestIndex = 0;
		if (type == TradeHelper.PRICE_LOW) {
			for (int i = start; i < end; i++) {
				double each = getCandle(i).getLow();
				if (each < lowest) {
					lowest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_CLOSE) {
			for (int i = start; i < end; i++) {
				double each = getCandle(i).getClose();
				if (each < lowest) {
					lowest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_OPEN) {
			for (int i = start; i < end; i++) {
				double each = getCandle(i).getOpen();
				if (each < lowest) {
					lowest = each;
					highestIndex = i;
				}
			}
		} else if (type == TradeHelper.PRICE_HIGH) {
			for (int i = start; i < end; i++) {
				double each = getCandle(i).getHigh();
				if (each < lowest) {
					lowest = each;
					highestIndex = i;
				}
			}
		}

		return getCandle(highestIndex);
	}

}
