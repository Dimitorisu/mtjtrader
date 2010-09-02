package forex.auto.trade.lib;

import forex.auto.trade.core.TimeSerise;
import forex.auto.trade.core.TimeSeriseContext;
import forex.auto.trade.core.TradeService;

public class ListCandleCommand implements Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see forex.auto.trade.lib.Command#run(java.lang.String)
	 */
	public String run(String[] param) {

		TradeService ts = TradeService.getInstance();

		String tsString = param.length > 0 ? param[0] : "1";
		int timeSeriseType = TimeSerise.ONE_MIN;

		if ("1".equals(tsString)) {
			timeSeriseType = TimeSerise.ONE_MIN;
		} else if ("5".equals(tsString)) {
			timeSeriseType = TimeSerise.FIFTTH_MIN;
		}

		TimeSerise times = ts.getTimeSerise(timeSeriseType);
		TimeSeriseContext ctx = times.getContext();

		StringBuffer output = new StringBuffer();

		output.append("candle:\n");
		for (int i = 0; i < 3; i++) {
			Candle c = ctx.getCandle(i);
			output.append(c);
			output.append("\n");
		}

		return output.toString();
	}

}
