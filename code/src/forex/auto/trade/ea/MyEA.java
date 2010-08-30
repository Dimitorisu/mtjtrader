package forex.auto.trade.ea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.TradeHelper;
import forex.auto.trade.core.Stochastic;
import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TradeService;
import forex.auto.trade.lib.Candle;

public class MyEA extends TradeHelper {
	private static Log log = LogFactory.getLog(MyEA.class);
	private Stochastic ema;
	long lasttime = 0;
	private TreadSelect tread;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

		TradeService ts = TradeService.getInstance();
		TimeSerise times = ts.getTimeSerise(TimeSerise.FOUR_HOUR);
		this.tread = new TreadSelect(times);

	}

	@Override
	public void start() {

		tread.watch();
		String state = tread.report();
		if (state != null) {
			if (log.isInfoEnabled()) {
				TradeService ts = TradeService.getInstance();
				TimeSerise times = ts.getTimeSerise(TimeSerise.ONE_MIN);
				Candle c = times.getCandle(0);
				log.info("Trend:" + state + ",candle:" + c);
			}
		}

	}

}
