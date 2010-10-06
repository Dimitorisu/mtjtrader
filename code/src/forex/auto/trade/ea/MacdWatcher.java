package forex.auto.trade.ea;

import forex.auto.trade.core.MACD;
import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TimeSeriseContext;
import forex.auto.trade.lib.Candle;

public class MacdWatcher {

	public static int V = 1;
	public static int DIFFV = 2;
	public static int UP = 3;

	private TimeSerise ts;
	private long lastTime = -1;
	private MACD macd;
	int state = 0;
	int lastState = 0;

	public MacdWatcher(TimeSerise times) {

		this.macd = new MACD();
		times.registerIndicator(macd);
		this.ts = times;
	}

	void watch(int buyOrSell) {

		TimeSeriseContext ctx = ts.getContext();

		if (ctx.bars() < 100) {
			return;
		}

		Candle now = ctx.getCandle(0);

		double vNow = macd.value(MACD.MODE_MAIN, 0);

		double vLast = macd.value(MACD.MODE_MAIN, 1);

		if (vLast > vNow) { // change point. A
			double vLastLast = macd.value(MACD.MODE_MAIN, 2);
			if (vLastLast < vLast) {
				
				if (state <= -1)
					state = -2;
				else {

					state = -1;
					// System.out.println("now:" + now);
				}
				// sharp.
				
			}
		} else if (vLast < vNow) { // change point. V

			double vLastLast = macd.value(MACD.MODE_MAIN, 2);
			// sharp.
			if (vLastLast > vLast) {
				
				if (state >= 1)
					state = 2;
				else {
					state = 1;
					// System.out.println("now:" + now);
				}
			}
		}

	
	}

	int report() {
		return state;
	}

}
