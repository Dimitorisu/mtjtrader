package forex.auto.trade.ui;

import java.util.Date;

import org.jfree.data.xy.DefaultHighLowDataset;

import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.lib.Candle;

public class ForexDatasetFactory {

	public static DefaultHighLowDataset createHighLowDataset(TimeSerise ts) {
		
		
		Candle[] candles = ts.getCandles();
		int len = candles.length;
		Date[] date = new Date[len];
		double[] high = new double[len];
		double[] low = new double[len];
		double[] open = new double[len];
		double[] close = new double[len];
		double[] volumn = new double[len];

		for (int i = 0; i < len; i++) {
			Candle c = candles[i];
			System.out.println("c:" + c);
			if (c != null) {
				
				date[i] = new Date(c.getTime());
				high[i] = c.getHigh();
				low[i] = c.getLow();
				open[i] = c.getOpen();
				close[i] = c.getClose();
				volumn[i] = 0;
			}
		}

		DefaultHighLowDataset ds = new DefaultHighLowDataset("Time", date,
				high, low, open, close, volumn);

		return ds;
	}
}
