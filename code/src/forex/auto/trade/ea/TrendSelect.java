package forex.auto.trade.ea;

import forex.auto.trade.core.MACD;
import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TimeSeriseContext;
import forex.auto.trade.lib.Candle;

public class TrendSelect {

	private TimeSerise ts;
	private long lastTime = -1;
	private MACD macd;
	int state = 0;

	public TrendSelect(TimeSerise times) {

		this.macd = new MACD();
		times.registerIndicator(macd);
		this.ts = times;
	}

	void watch() {

		TimeSeriseContext ctx = ts.getContext();

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

					if (state >= 1)
						state = 2;
					else {
						
						state = 1;
						//System.out.println("now:" + now);
					}
				}

			} else if (vNow < 0) {// go down
				double vLast = macd.value(MACD.MODE_MAIN, 1);
				double vLastLast = macd.value(MACD.MODE_MAIN, 2);
				if (vLastLast < vLast && vLast > vNow) { // change point. A
					// sharp.
					if (state <= -1)
						state = -2;
					else {
						
						state = -1;
						//System.out.println("now:" + now);
					}
				}

			} else

				state = 0;

		} else {
			lastTime = now.getTime();
		}

	}

	int report() {
		return state;
	}

}
