package forex.auto.trade.ea;

import java.util.ArrayList;

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
	private TrendSelect trend;
	Expert exp = null;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

		exp = new Expert();

	}

	@Override
	public void start() {
		OrderManager om = OrderManager.getInstance();
		ArrayList<Order> orders = om.getActiveOrders();
		if (orders.size() > 0) {
			//keep order going successful.
			

		} else {

			exp.work();
			Order o = exp.getOrder();
			if (o != null) {
				om.sendOrder(o);
			}

		}

		trend.watch();
		int state = trend.report();
		if (state == 1) {

			int count = om.getActiveOrders().size();
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
