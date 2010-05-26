package forex.auto.trade.core;


public interface Indicator {
	
	public void init(TimeSeriseConfig config);
	
	public void update(int size,boolean update);

}
