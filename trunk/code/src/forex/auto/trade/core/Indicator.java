package forex.auto.trade.core;


public interface Indicator {
	
	public void init(TimeSeriseContext config);
	public void start();
}
