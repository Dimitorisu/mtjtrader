package forex.auto.trade.ea;

import forex.auto.trade.Order;
import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TradeService;

public class TradeSelect {

	private TimeSerise five_min;
	private TimeSerise one_min;

	public TradeSelect() {
		TradeService ts = TradeService.getInstance();
		five_min = ts.getTimeSerise(TimeSerise.FIVE_MIN);
		one_min = ts.getTimeSerise(TimeSerise.ONE_MIN);

	}

	public int penddingBuy() {

		return 0;

	}

	public int penddingSell() {
		
		return 0;

	}

	public Order caculateOrder() {
		return null;
	}

}
