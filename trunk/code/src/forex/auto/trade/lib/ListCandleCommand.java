package forex.auto.trade.lib;

public class ListCandleCommand implements Command {
	
	
	
	/* (non-Javadoc)
	 * @see forex.auto.trade.lib.Command#run(java.lang.String)
	 */
	public String run(String param) {
		
		
		StringBuffer output = new StringBuffer();
		
		output.append("lc value show...");
		return output.toString();
	}

}
