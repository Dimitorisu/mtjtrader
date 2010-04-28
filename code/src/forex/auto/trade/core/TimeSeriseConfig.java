package forex.auto.trade.core;

import forex.auto.trade.lib.Candle;

public class TimeSeriseConfig {
	
	private int tickCount;

	public TimeSeriseConfig(int count) {
		this.tickCount = count;
	}
	
	public int maxTickCount(){
		return tickCount;
	}

	public Candle[] getCandles() {
		// TODO Auto-generated method stub
		return null;
	}

}
