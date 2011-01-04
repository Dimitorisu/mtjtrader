package org.jtrader.core;



public interface Indicator {
	
	public void init(TimeSeriseContext config);
	public void start();
}
