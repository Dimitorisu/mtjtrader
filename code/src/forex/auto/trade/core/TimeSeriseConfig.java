package forex.auto.trade.core;

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

	public int getUpdatedBars() {
		return updatedBars;
	}

	public void increaseUpdatedBars() {
		updatedBars++;
	}

	public void clearUpdateBars() {
		updatedBars = 0;
	}

	public Candle getCandle(int index) {
		int size = candles.length;
		if (index < size) {
			int reIndex = ((bars - index - 1) % size);
			return candles[reIndex];
		} else {
			return null;
		}
	}

	public void newCandle(Candle newOne) {
		int reIndex = bars % candles.length;
		candles[reIndex] = newOne;
		increaseBar();
	}

	// public Candle[] getCandles() {
	// return candles;
	// }
	//	
	public void increaseBar() {
		bars++;
	}

	public void setBars(int _new) {
		this.bars = _new;
	}

	public int bars() {
		return bars;
	}

}
