package org.jtrader.data.provider;

public class Price {

	long[] time;
	double[] open;
	double[] close;
	double[] high;
	double[] low;

	public Price(int size) {
		this.time = new long[size];
		this.open = new double[size];
		this.close = new double[size];
		this.high = new double[size];
		this.low = new double[size];
	}

	public void setPrice(int index, long _time, double _open, double _close,
			double _high, double _low) {
		this.time[index] = _time;
		this.open[index] = _open;
		this.close[index] = _close;
		this.high[index] = _high;
		this.low[index] = _low;
	}

	public long[] getTime() {
		return time;
	}

	public double[] getOpen() {
		return open;
	}

	public double[] getClose() {
		return close;
	}

	public double[] getHigh() {
		return high;
	}

	public double[] getLow() {
		return low;
	}

}
