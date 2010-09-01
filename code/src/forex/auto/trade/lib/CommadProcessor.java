package forex.auto.trade.lib;

import java.util.Arrays;
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
		String[] param = Arrays.copyOfRange(inputString, 1, inputString.length);
		Command cmd = cmds.get(cmdString);
		if (cmd != null) {
			return cmd.run(param);
		} else {
			return "Unknown command: " + cmdString + "!";
		}

	}

}
