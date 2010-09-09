package forex.auto.trade.lib;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommadProcessor {

	private static Log log = LogFactory.getLog(CommadProcessor.class);

	private static CommadProcessor cp = new CommadProcessor();
	HashMap<String, Command> cmds = new HashMap<String, Command>();

	private CommadProcessor() {
		cmds.put("lc", new ListCandleCommand());

	}

	public static CommadProcessor getInstance() {
		return cp;
	}

	public String executeCommand(String input) {

		try {
			String[] inputString = input.split(" ");
			String cmdString = inputString[0];
			String[] param = Arrays.copyOfRange(inputString, 1,
					inputString.length);
			Command cmd = cmds.get(cmdString);
			if (cmd != null) {
				return cmd.run(param);
			} else {
				return "Unknown command: " + cmdString + "!";
			}

		} catch (Throwable t) {
			if (log.isErrorEnabled()) {
				log.error("Execute command:\"" + input + "\" error.", t);
			}
			return "error:" + t.getMessage();
		}

	}

}
