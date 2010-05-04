package forex.auto.trade.core;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.lib.Candle;
import forex.auto.trade.ui.CandleChart;

public class TradeService {

	private static Log log = LogFactory.getLog(TradeService.class);
	private PriceProvider priceProvider;
	volatile boolean exit = true;
	int candleCount =2000;
	
	TimeSerise ONE_MIN = TimeSerise.createTimeSerise(TimeSerise.ONEMIN,candleCount);
	TimeSerise FIVE_MIN = TimeSerise.createTimeSerise(TimeSerise.FIVE_MIN,candleCount);
	TimeSerise FIFTTH_MIN = TimeSerise.createTimeSerise(TimeSerise.FIFTTH_MIN,candleCount);
	TimeSerise HALF_HOUR = TimeSerise.createTimeSerise(TimeSerise.HALF_HOUR,candleCount);
	TimeSerise ONE_HOUR = TimeSerise.createTimeSerise(TimeSerise.ONE_HOUR,candleCount);
	TimeSerise FOUR_HOUR = TimeSerise.createTimeSerise(TimeSerise.FOUR_HOUR,candleCount);
	TimeSerise ONE_DAY = TimeSerise.createTimeSerise(TimeSerise.ONE_DAY,candleCount);

	public static TradeService instance = new TradeService();
	
	private TradeService() {

	}
	
	public static TradeService getInstance() {
		return instance;
	}

	public void addDataProvider(PriceProvider _priceProvider) {
		this.priceProvider = _priceProvider;

	}

	
	public void run() {
		if (log.isInfoEnabled()) {
			log.info("Starting trade service");
		}

		MACD ema = new MACD();
		ONE_MIN.registerIndicator(ema);
		
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
		double value = ema.value(MACD.MODE_SIGNAL,4);
		System.out.println("candle:" + ONE_MIN.getCandles(4)+ " macd value:" + (new BigDecimal(value)).toString());
		
		if (log.isInfoEnabled()) {
			log.info("end trade service");
		}
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
