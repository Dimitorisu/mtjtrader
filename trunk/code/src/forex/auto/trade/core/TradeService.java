package forex.auto.trade.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.ea.MyEA;
import forex.auto.trade.lib.Candle;

public class TradeService {

	private static Log log = LogFactory.getLog(TradeService.class);
	private PriceProvider priceProvider;
	volatile boolean exit = true;
	int candleCount = 1440;

	TimeSerise ONE_MIN = TimeSerise.createTimeSerise(TimeSerise.ONE_MIN,
			candleCount);
	TimeSerise FIVE_MIN = TimeSerise.createTimeSerise(TimeSerise.FIVE_MIN,
			candleCount);
	TimeSerise FIFTTH_MIN = TimeSerise.createTimeSerise(TimeSerise.FIFTTH_MIN,
			candleCount);
	TimeSerise HALF_HOUR = TimeSerise.createTimeSerise(TimeSerise.HALF_HOUR,
			candleCount);
	TimeSerise ONE_HOUR = TimeSerise.createTimeSerise(TimeSerise.ONE_HOUR,
			candleCount);
	TimeSerise FOUR_HOUR = TimeSerise.createTimeSerise(TimeSerise.FOUR_HOUR,
			candleCount);
	TimeSerise ONE_DAY = TimeSerise.createTimeSerise(TimeSerise.ONE_DAY,
			candleCount);

	private MyEA ea;

	public static TradeService instance = new TradeService();

	private TradeService() {
		start();

	}

	public void start() {

		if (ea != null) {
			ea.init();
		}
	}

	public void stop() {
		if (ea != null) {
			ea.destroy();
		}
	}

	public static TradeService getInstance() {
		return instance;
	}

	public TimeSerise getTimeSerise(int timeFrame) {
		TimeSerise ts = null;
		if (TimeSerise.ONE_MIN == timeFrame) {
			ts = ONE_MIN;
		} else if (TimeSerise.FIVE_MIN == timeFrame) {
			ts = FIVE_MIN;
		} else if (TimeSerise.FIFTTH_MIN == timeFrame) {
			ts = FIFTTH_MIN;
		} else if (TimeSerise.HALF_HOUR == timeFrame) {
			ts = HALF_HOUR;
		} else if (TimeSerise.ONE_HOUR == timeFrame) {
			ts = ONE_HOUR;
		} else if (TimeSerise.FOUR_HOUR == timeFrame) {
			ts = FOUR_HOUR;
		} else if (TimeSerise.ONE_DAY == timeFrame) {
			ts = ONE_DAY;
		}
		return ts;
	}

	public void addDataProvider(PriceProvider _priceProvider) {
		this.priceProvider = _priceProvider;

	}

	public void addData(int timeFrame, Candle c) {
		TimeSerise ts = getTimeSerise(timeFrame);
		if (ts != null) {

			ts.updateCandle(c);

		} else {
			if (log.isWarnEnabled()) {
				log.warn("Time frame :" + timeFrame + " is not found!");
			}
		}
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
			ONE_MIN.updateCandle(candle);
			FIVE_MIN.updateCandle(candle);
			FIFTTH_MIN.updateCandle(candle);
			HALF_HOUR.updateCandle(candle);
			ONE_HOUR.updateCandle(candle);
			FOUR_HOUR.updateCandle(candle);
			ONE_DAY.updateCandle(candle);

			if (ea != null) {
				ea.start();
			}

		}

		if (log.isInfoEnabled()) {
			log.info("end trade service");
		}
	}

	public void addEa(MyEA myEA) {

		this.ea = myEA;
	}

}
