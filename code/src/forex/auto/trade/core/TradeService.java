package forex.auto.trade.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.TradeHelper;
import forex.auto.trade.lib.Candle;
import forex.auto.trade.ui.CandleChart;

public class TradeService  implements Runnable {

	private static Log log = LogFactory.getLog(TradeService.class);
	private PriceProvider priceProvider;
	private TradeHelper tradeHelper;
	volatile boolean exit = true;
	TimeSerise ONE_MIN = TimeSerise.createTimeSerise(TimeSerise.ONEMIN);
	TimeSerise FIVE_MIN = TimeSerise.createTimeSerise(TimeSerise.FIVE_MIN);
	TimeSerise FIFTTH_MIN = TimeSerise.createTimeSerise(TimeSerise.FIFTTH_MIN);
	TimeSerise HALF_HOUR = TimeSerise.createTimeSerise(TimeSerise.HALF_HOUR);
	TimeSerise ONE_HOUR = TimeSerise.createTimeSerise(TimeSerise.ONE_HOUR);
	TimeSerise FOUR_HOUR = TimeSerise.createTimeSerise(TimeSerise.FOUR_HOUR);
	TimeSerise ONE_DAY = TimeSerise.createTimeSerise(TimeSerise.ONE_DAY);
	

	public TradeService() {

	}

	
	
	public void addDataProvider(PriceProvider _priceProvider) {
		this.priceProvider = _priceProvider;

	}

	public void addEa(TradeHelper _ea) {

		this.tradeHelper = _ea;

	}

	public void run() {
		if (log.isInfoEnabled()) {
			log.info("Starting trade service");
		}

		while (exit) {
			Candle candle = priceProvider.read();
			if (candle == null) {
				break;
			}
			//System.out.println("calculate:" + candle);
			
			ONE_MIN.updateCandle(candle);
			FIVE_MIN.updateCandle(candle);
			FIFTTH_MIN.updateCandle(candle);
			HALF_HOUR.updateCandle(candle);
			ONE_HOUR.updateCandle(candle);
			FOUR_HOUR.updateCandle(candle);
			ONE_DAY.updateCandle(candle);
		}

		CandleChart.display(ONE_DAY);

	}
	
//	public static void main(String[] args) {
//		Date t = new Date();
//		long time = t.getTime();
//		System.out.println("time:" + time);
//		int ONE_DAY = 60*60000;
//		long diff = time +(8*60*60000);
//		System.out.println("diff:" + diff);
//
//		long tt = diff - (diff %ONE_DAY);
//		System.out.println("tt:"+ tt);
//		
//		long t2 = diff-16*60*60000;
//		System.out.println("t2=" + (new Date(tt -8*60*60000 )).toLocaleString());
//		
//		
//		
//	}

}
