package forex.auto.trade.ea;

import forex.auto.trade.Order;
import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TradeService;

public class Expert {
	int TREND_FOUND = 1;
	int TREND_LOST = 0;

	int trendState = 0;
	private TrendSelect trend;
	private TradeSelect trader;
	private Order outputOrder;

	public Expert() {
		TradeService ts = TradeService.getInstance();
		TimeSerise times = ts.getTimeSerise(TimeSerise.FOUR_HOUR);
		this.trend = new TrendSelect(times);
		this.trader = new TradeSelect();
	}

	public void work() {
		
		int buyOrSell = 0;
		if (trendFound()) {
			buyOrSell = traderPendding();
		} else {
			trend.watch();
			int state = trend.report();
			if (state != 0) {
				trendState = state;
				buyOrSell = traderPendding();
			}
		}
		
		if(buyOrSell !=0) {
			Order order = trader.caculateOrder();
			if(order != null) {
				this.outputOrder = order;
			}
			
		}
		
		
	}

	private int traderPendding() {
		
		if (trendState > 0) {
			return trader.penddingBuy();
		} else {
			return trader.penddingSell();
		}
		
	}

	private boolean trendFound() {

		return trendState != 0;
	}

	public int buyOrSell() {
		return 0;
	}

	public Order getOrder() {
		return outputOrder;
		
	}

}
