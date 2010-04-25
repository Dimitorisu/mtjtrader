package forex.auto.trade.core;


public class TradeMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TradeService ts = TradeService.getInstance();
		FilePriceProvider dp = new FilePriceProvider();
		dp.init();
		ts.addDataProvider(dp);
		//ts.addEa(new MyEA());
		ts.run();

	}

}
