package forex.auto.trade.core;

import forex.auto.trade.lib.Candle;

public class TimeSeriseConfig {

	Candle[] candles;
	int bars = 0;

	public TimeSeriseConfig(Candle[] _candles) {
		this.candles = _candles;
	}

	public int maxTickCount() {
		return candles.length;
	}

	public Candle[] getCandles() {
		return candles;
	}
	
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
