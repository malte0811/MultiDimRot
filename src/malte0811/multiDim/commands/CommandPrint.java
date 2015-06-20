package malte0811.multiDim.commands;

import java.util.ArrayList;
import java.util.Set;

import malte0811.multiDim.CommandListener;
import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class CommandPrint extends Command {

	@Override
	public String getCommandName() {
		return "PRINT";
	}

	@Override
	public String getCommandUsage() {
		return "\"print <v>\" prints out the value of v (variable or command with return value)";
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		try {
			System.out.println(Programm.getDoubleValue(args[0]));
		} catch (IllegalArgumentException x) {
			try {
				System.out.println(Programm.getStringValue(args[0]));
			} catch (IllegalArgumentException x2) {
				if (!(x instanceof NumberFormatException)) {
					System.out.println(x.getMessage());
				} else {
					System.out.println(x.getMessage().substring(18)
							+ " is not a number.");
				}
				CommandListener.out.logException(x);
				System.out.println(x2.getMessage());
				CommandListener.out.logException(x2);
			}
		}
	}

	@Override
	public ArrayList<String> getCompletion(int i, String toComplete) {
		ArrayList<String> possible = new ArrayList<>();
		Set<String> keys = ReturningCommand.retCommands.keySet();
		String out = "";
		for (String c : keys) {
			if (c.length() >= toComplete.length()
					&& c.substring(0, toComplete.length()).equalsIgnoreCase(
							toComplete)) {
				possible.add(c + "(");
				if (out.equals("")) {
					out += c;
				} else {
					out += ", " + c;
				}
			}
		}
		System.out.println(out);
		return possible;
	}
}
