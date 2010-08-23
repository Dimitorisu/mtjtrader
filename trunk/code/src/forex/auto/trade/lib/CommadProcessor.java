package forex.auto.trade.lib;

import java.util.HashMap;

public class CommadProcessor {

	private static CommadProcessor cp = new CommadProcessor();
	HashMap<String, Command> cmds = new HashMap<String, Command>();

	private CommadProcessor() {
		cmds.put("lc", new ListCandleCommand());

	}

	public static CommadProcessor getInstance() {
		return cp;
	}

	public String executeCommand(String input) {
		String[] inputString = input.split(" ");
		String cmdString = inputString[0];
		Command cmd = cmds.get(cmdString);
		if (cmd != null) {
			String param = input.substring(input.indexOf(cmdString) + 1);

			return cmd.run(param);
		} else {
			return "Unknown command:" + cmdString + "!";
		}

	}

}
