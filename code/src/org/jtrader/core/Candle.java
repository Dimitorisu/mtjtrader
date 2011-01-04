package org.jtrader.core;

import java.util.Date;

public class Candle {
	long time = 0;
	double open = 0;
	double high = 0;
	double low = 0;
	double close = 0;

	public Candle() {

	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void updateHigh(double _high) {
		if (this.high < _high)
			this.high = _high;
	}

	public double getLow() {
		return low;
	}
	
	public void setLow(double _low) {
		this.low = _low;
	}
	
	public void setHigh(double _high) {
		this.high = _high;
	}
	

	public void updateLow(double _low) {
		if (this.low > _low)
			this.low = _low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}
	


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("time=").append((new Date(time)).toLocaleString());
		sb.append(",open=").append(open);
		sb.append(",high=").append(high);
		sb.append(",low=").append(low);
		sb.append(",close=").append(close);
		return sb.toString();
	}

}
