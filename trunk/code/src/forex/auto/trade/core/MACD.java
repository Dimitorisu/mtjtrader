package forex.auto.trade.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

import forex.auto.trade.lib.Candle;

public class MACD implements Indicator {

	public static int MODE_MAIN = 0;
	public static int MODE_SIGNAL = 1;
	double[] macd = null;
	double[] signal = null;
	double[] hist = null;
	Candle[] candles = null;
	int fast_ema_period = 12;
	int slow_ema_period = 26;
	int signal_period = 9;
	
	EMA fast_line = null;
	EMA slow_line = null;
	//EMA signal_line = null;

	public MACD() {
	}

	public MACD(int _fast_ema_period, int _slow_ema_period, int _signal_period) {
		this.fast_ema_period = _fast_ema_period;
		this.slow_ema_period = _slow_ema_period;
		this.signal_period = _signal_period;

	}

	public double value(int mode, int shift) {
		if (mode == MODE_MAIN) {
			return macd[shift];
		} else if (mode == MODE_SIGNAL) {
			return signal[shift];
		} else {
			return fast_line.value(shift) - slow_line.value(shift);
		}
	}

	public double iMACD(String symbol, int timeframe, int fast_ema_period,
			int slow_ema_period, int signal_period, int applied_price,
			int mode, int shift) {

		return 0;

	}

	public void update(int size) {
		fast_line.update(size);
		slow_line.update(size);
		//signal_line.update(size);
		
		int i = size;
		while (i >= 1) {
			macd[i] = macd[i - 1];
			signal[i] = signal[i - 1];
			i--;
		}
		macd[0] = fast_line.value(0) - slow_line.value(0);
		
		
		double sum =0;
		for(int j=0;j<signal_period;j++) {
			sum = sum +  macd[j];
		}
		BigDecimal a = new BigDecimal(sum);
		BigDecimal b = new BigDecimal(signal_period);
		BigDecimal signal_curr = a.divide(b,7,RoundingMode.HALF_UP);
		signal[0]= signal_curr.doubleValue();
	}

	public void init(TimeSeriseConfig config) {
		candles = config.getCandles();
		int tCount = config.maxTickCount();

		fast_line = new EMA(this.fast_ema_period);
		fast_line.init(config);

		slow_line = new EMA(this.slow_ema_period);
		slow_line.init(config);

		//signal_line = new EMA(this.signal_period);
		//signal_line.init(config);

		macd = new double[tCount];
		signal = new double[tCount];
		//hist = new double[tCount];
	}
}
