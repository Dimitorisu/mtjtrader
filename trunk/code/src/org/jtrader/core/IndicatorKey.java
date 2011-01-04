package org.jtrader.core;

public class IndicatorKey {
	
	private int id;

	public IndicatorKey(int _id) {
		this.id =_id;
	}

	
	@Override
	public int hashCode() {
		return this.id;
	}
	@Override
	public boolean equals(Object obj) {
		return obj.hashCode()== this.id;
	}

}
