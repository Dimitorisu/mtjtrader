package forex.auto.trade.core;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

import forex.auto.trade.lib.Candle;
import forex.auto.trade.lib.Price;
import forex.auto.trade.lib.PriceData;

public class MACD implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	double[] macd = null;
	double[] signal = null;
	double[] hist = null;
	Candle[] candles = null;
	Core core = new Core();
	int fast_ema_period = 12;
	int slow_ema_period = 26;
	int signal_period = 9;
	
	public MACD() {
	}

	public MACD(int _fast_ema_period, int _slow_ema_period, int _signal_period) {
		this.fast_ema_period = _fast_ema_period;
		this.slow_ema_period = _slow_ema_period;
		this.signal_period = _signal_period;
	}

	public double value(int mode,int shift) {
		if (mode == MODE_MAIN) {
			return macd[shift];
		} else if (mode == MODE_SIGNAL) {
			return signal[shift];
		}
		return 0;
	}
	
	public double iMACD(String symbol, int timeframe, int fast_ema_period,
			int slow_ema_period, int signal_period, int applied_price,
			int mode, int shift) {

		PriceData oPriceData = PriceData.getDatas(symbol);
		Price oPrice = oPriceData.getPrice(timeframe);
		double[] price = new double[] {};

		if (applied_price == PriceData.PRICE_CLOSE) {
			price = oPrice.getClose();
		}

		double[] signal = new double[price.length];
		double[] hist = new double[price.length];
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

	public void update(int size) {
		double[] price = new double[size];
		for (int i = 0; i < size; i++) {
			price[i] = candles[i].getClose();
		}
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		outBegIdx.value = -1;
		outNbElement.value = -1;
		core.macd(0, price.length - 1, price, fast_ema_period, slow_ema_period,
				signal_period, outBegIdx, outNbElement, macd, signal, hist);
	}

	public void init(TimeSeriseConfig config) {
		candles = config.getCandles();
		int tCount = config.maxTickCount();
		macd = new double[tCount];
		signal = new double[tCount];
		hist = new double[tCount];
	}
}
