package org.jtrader.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jtrader.core.Candle;
import org.jtrader.core.TimeSerise;



public class TradeSymbol {
	private static Log log = LogFactory.getLog(TradeSymbol.class);
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

	public void initData(int timeFrame, Candle[] candles) {
		TimeSerise ts = getTimeSerise(timeFrame);
		if (ts != null) {

			for (Candle c : candles) {
				ts.updateCandle(c);
			}
		} else {
			if (log.isWarnEnabled()) {
				log.warn("Time frame :" + timeFrame + " is not found!");
			}
		}
	}
}
