package malte0811.multiDim.addons;

import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import malte0811.multiDim.commands.ret.RetCommandCos;
import malte0811.multiDim.commands.ret.RetCommandGetDimensions;
import malte0811.multiDim.commands.ret.RetCommandRandom;
import malte0811.multiDim.commands.ret.RetCommandSin;
import malte0811.multiDim.commands.ret.RetCommandSqrt;
import malte0811.multiDim.commands.ret.RetCommandTan;
import malte0811.multiDim.commands.ret.RetCommandToDegrees;
import malte0811.multiDim.commands.ret.RetCommandToDouble;
import malte0811.multiDim.commands.ret.RetCommandToRadians;

public abstract class ReturningCommand {
	public static HashMap<String, ReturningCommand> retCommands = new HashMap<>();

	public abstract String getRetCommandName();

	public abstract String getRetCommandUsage();

	public abstract double processCommand(String[] args);

	public abstract int getMinParameterCount();

	static {
		register(new RetCommandGetDimensions());
		register(new RetCommandRandom());
		register(new RetCommandToDouble());
		register(new RetCommandSin());
		register(new RetCommandCos());
		register(new RetCommandTan());
		register(new RetCommandToDegrees());
		register(new RetCommandToRadians());
		register(new RetCommandSqrt());

	}

	public static void register(ReturningCommand c) {
		retCommands.put(c.getRetCommandName(), c);
	}

	public static double processCommand(String s)
			throws IllegalArgumentException {
		StringTokenizer st = new StringTokenizer(s, "(");
		String cmd = st.nextToken();
		if (retCommands.containsKey(cmd.toUpperCase())) {
			String[] args = getRetCommandParameters(s);
			ReturningCommand c = retCommands.get(cmd.toUpperCase());
			if (args.length < c.getMinParameterCount()) {
				throw new IllegalArgumentException(
						"Not enough parameters: required: "
								+ c.getMinParameterCount() + ", found: "
								+ args.length);
			}
			return c.processCommand(args);
		}
		throw new IllegalArgumentException(cmd + " is not a valid command");
	}

	protected static boolean isValid(String s) {
		// check brackets
		char[] c = s.toCharArray();
		int layer = 0;
		for (char ch : c) {
			if (ch == '(') {
				layer++;
			} else if (ch == ')') {
				layer--;
				if (layer < 0) {
					return false;
				}
			}
		}
		return layer == 0;
	}

	protected static String[] getRetCommandParameters(String s)
			throws IllegalArgumentException {
		if (s == null || !isValid(s)) {
			throw new IllegalArgumentException(
					"Command is not valid, too many opening/closing brackets");
		}
		String[] ret = new String[0];
		int currPos = s.indexOf("(") + 1;
		while (currPos < s.length() - 1) {

			char cChar = s.charAt(currPos);
			if (cChar != ',' && cChar != ' ' && cChar != '	') {
				int layer = 0;
				String c = "";
				while ((cChar != ',' && cChar != ')') || layer != 0) {
					c += cChar;
					if (cChar == '(') {
						layer++;
					}
					if (cChar == ')') {
						layer--;
					}
					currPos++;
					if (currPos == s.length() - 1 && layer == 0) {
						break;
					}
					cChar = s.charAt(currPos);
				}
				ret = Arrays.copyOf(ret, ret.length + 1);
				ret[ret.length - 1] = c;
			} else {
				currPos++;
			}

		}
		return ret;
	}
}
