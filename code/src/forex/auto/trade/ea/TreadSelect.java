package forex.auto.trade.ea;

import forex.auto.trade.core.MACD;
import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TimeSeriseContext;
import forex.auto.trade.lib.Candle;

public class TreadSelect {

	private TimeSerise ts;
	private long lastTime = -1;
	private MACD macd;
	String state = null;

	public TreadSelect(TimeSerise times) {

		this.macd = new MACD();
		times.registerIndicator(macd);
		this.ts = times;
	}

	void watch() {

		state = null;

		TimeSeriseContext ctx = ts.getContext();
		System.out.println("ctx:" + ctx.bars());
		if (ctx.bars() < 100) {
			return;
		}
		
		

		Candle now = ctx.getCandle(0);
		if (lastTime != now.getTime()) { // first new bar. then check the trend.

			double vNow = macd.value(MACD.MODE_MAIN, 0);
			if (vNow > 0) { // go up.

				double vLast = macd.value(MACD.MODE_MAIN, 1);
				double vLastLast = macd.value(MACD.MODE_MAIN, 2);
				if (vLastLast > vLast && vLast < vNow) { // change point. V
															// sharp.
					state = "up";
				}

			} else if (vNow < 0) {// go down
				double vLast = macd.value(MACD.MODE_MAIN, 1);
				double vLastLast = macd.value(MACD.MODE_MAIN, 2);
				if (vLastLast < vLast && vLast > vNow) { // change point. A
															// sharp.
					state = "down";
				}

			}

		} else {
			lastTime = now.getTime();
		}

	}

	String report() {
		return state;
	}

}
