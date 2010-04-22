package forex.auto.trade;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

import forex.auto.trade.lib.Price;
import forex.auto.trade.lib.PriceData;

public abstract class TradeHelper {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	
	
	public abstract void start();
	public abstract void init();
	public abstract void destroy();

	public static double iMACD(String symbol, int timeframe,
			int fast_ema_period, int slow_ema_period, int signal_period,
			int applied_price, int mode, int shift) {

		PriceData oPriceData = PriceData.getDatas(symbol);
		Price oPrice = oPriceData.getPrice(timeframe);
		double[] price = new double[] {};

		if (applied_price == PriceData.PRICE_CLOSE) {
			price = oPrice.getClose();
		}

		Core core = new Core();
		double macd[] = new double[price.length];
		double signal[] = new double[price.length];
		double hist[] = new double[price.length];
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		outBegIdx.value = -1;
		outNbElement.value = -1;
		core.macd(0, price.length - 1, price, fast_ema_period, slow_ema_period,
				signal_period, outBegIdx, outNbElement, macd, signal, hist);
		if (mode == MODE_MAIN) {
			return macd[shift];
		} else if (mode == MODE_SIGNAL) {
			return signal[shift];
		}
		return 0;
	}
}
