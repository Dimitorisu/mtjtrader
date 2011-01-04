package forex.auto.trade.ea;

import org.jtrader.core.Order;
import org.jtrader.core.TimeSerise;
import org.jtrader.core.TradeService;


public class TradeSelect {

	private TimeSerise five_min;
	private TimeSerise one_min;
	private MacdWatcher mw_five = null;
	private MacdWatcher mw_one = null;
	

	public TradeSelect() {
		TradeService ts = TradeService.getInstance();
		five_min = ts.getTimeSerise(TimeSerise.FIVE_MIN);
		one_min = ts.getTimeSerise(TimeSerise.ONE_MIN);
		mw_five = new MacdWatcher(five_min);
		mw_one = new MacdWatcher(one_min);
		
		

	}

	public int penddingBuy() {
		
		mw_five.watch(1);
		if(mw_five.report() == 1) {
			//can buy.
			mw_one.watch(1);
			if(mw_one.report() ==1) {
				return 1;
			}
			
		}
		

		return 0;

	}

	public int penddingSell() {
		
		return 0;

	}

	public Order caculateOrder() {
		return null;
	}

}
