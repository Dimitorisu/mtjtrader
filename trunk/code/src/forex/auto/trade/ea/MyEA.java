package forex.auto.trade.ea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.TradeHelper;
import forex.auto.trade.core.MACD;
import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TradeService;

public class MyEA extends TradeHelper {
	private static Log log = LogFactory.getLog(MyEA.class);
	private TimeSerise times;
	private MACD ema;
	long lasttime = 0;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {

		TradeService ts = TradeService.getInstance();
		times = ts.getTimeSerise(TimeSerise.ONE_HOUR);
		ema = new MACD();
		times.registerIndicator(ema);
	}

	@Override
	public void start() {

		

		if (times.getCandle(0).getTime() != lasttime) {
			if (log.isDebugEnabled()) {
				System.out.println("canle:" + times.getCandle(1) + ",ema:"
						+ ema.value(MODE_SIGNAL,1));
			}
			lasttime = times.getCandle(0).getTime();
		}
		// if (log.isInfoEnabled()) {
		// log.info("macd:" + macd);
		// }
	}

}
