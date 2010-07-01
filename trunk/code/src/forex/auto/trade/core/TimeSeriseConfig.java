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
		try {
		int size = candles.length;
		if (index < size) {
			int reIndex = ((bars - index) % size);
			return candles[reIndex];
		} else {
			return null;
		}
		}catch(Exception e) {
			System.out.println("index:"+ index);
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
		bars++;
	}

	public void setBars(int _new) {
		this.bars = _new;
	}

	public int bars() {
		return bars;
	}

}
