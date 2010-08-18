package forex.auto.trade.lib;

import java.util.HashMap;

public class PriceData {

	public static int PRICE_CLOSE = 0;
	public static int PRICE_OPEN = 1;
	public static int PRICE_HIGH = 2;
	public static int PRICE_LOW = 3;

	private HashMap<Integer, Price> priceData = new HashMap<Integer, Price>();

	private static HashMap<String, PriceData> datas = new HashMap<String, PriceData>();

	public PriceData() {

	}

	public static PriceData getDatas(String symbol) {
		return datas.get(symbol);
	}

	public static void addPrice(String symbol, int timeframe, Price price) {
		PriceData pdata = datas.get(symbol);
		if (pdata == null) {
			pdata = new PriceData();
			datas.put(symbol, pdata);
		}
		pdata.addPrice(timeframe, price);
	}

	public Price getPrice(int timeframe) {
		return priceData.get(timeframe);
	}

	public void addPrice(int timeframe, Price price) {
		priceData.put(timeframe, price);
	}

}
