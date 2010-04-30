package forex.auto.trade.core;

import forex.auto.trade.lib.Candle;

public class TimeSeriseConfig {
	
	Candle[] candles;

	public TimeSeriseConfig(Candle[] _candles) {
		this.candles = _candles;
	}
	
	public int maxTickCount(){
		return candles.length;
	}

	public Candle[] getCandles() {
		return candles;
	}

}
