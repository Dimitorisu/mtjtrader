package forex.auto.trade.ea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.Order;
import forex.auto.trade.OrderManager;
import forex.auto.trade.TradeHelper;
import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TradeService;
import forex.auto.trade.lib.Candle;

public class MyEA extends TradeHelper {
	private static Log log = LogFactory.getLog(MyEA.class);
	long lasttime = 0;
	private TrendSelect tread;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

		TradeService ts = TradeService.getInstance();
		TimeSerise times = ts.getTimeSerise(TimeSerise.FOUR_HOUR);
		this.tread = new TrendSelect(times);

	}

	@Override
	public void start() {

		tread.watch();
		int state = tread.report();
		if (state == 1) {
			OrderManager om = OrderManager.getInstance();
			int count = om.getOrderCount();
			if (count == 0) {

				TradeService ts = TradeService.getInstance();
				double ask = ts.getAsk();
				Order order = new Order(Order.OP_BUY, 0.1, ask, ask - 0.004,
						ask + 0.004);

				om.sendOrder(order);
				
				if (log.isInfoEnabled()) {
					TradeService tss = TradeService.getInstance();
					TimeSerise times = tss.getTimeSerise(TimeSerise.ONE_MIN);
					Candle c = times.getCandle(0);
					log.info("Trend:" + state + ",candle:" + c);
				}
			}

			
		}

	}

}
