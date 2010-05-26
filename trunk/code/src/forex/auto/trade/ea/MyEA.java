package forex.auto.trade.ea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.TradeHelper;
import forex.auto.trade.core.MACD;

public class MyEA extends TradeHelper {
	private static Log log = LogFactory.getLog(MyEA.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {

	}

	@Override
	public void start() {
		
		double macd = iMACD(null, FOUR_HOUR, 12, 26, 9, PRICE_CLOSE,
				MACD.MODE_MAIN, 0);
		
//		if (log.isInfoEnabled()) {
//			log.info("macd:" + macd);
//		}
	}
	
	

}
