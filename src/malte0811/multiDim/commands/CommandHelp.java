package malte0811.multiDim.commands;

import java.util.ArrayList;
import java.util.Set;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.ReturningCommand;

public class CommandHelp extends Command {

	@Override
	public String getCommandName() {
		return "HELP";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length >= 1) {
			if (Command.commands.containsKey(args[0].toUpperCase())) {
				System.out.println(((Command) Command.commands.get(args[0]
						.toUpperCase())).getCommandUsage());
			} else if (ReturningCommand.retCommands.containsKey(args[0]
					.toUpperCase())) {
				System.out
						.println(((ReturningCommand) ReturningCommand.retCommands
								.get(args[0].toUpperCase()))
								.getRetCommandUsage());
			}
		} else {
			String[] comms = Command.commands.keySet().toArray(new String[0]);
			System.out.println("Commands without a return value:");
			for (String s : comms) {
				System.out.println(s.toLowerCase());
			}
			String[] retcomms = ReturningCommand.retCommands.keySet().toArray(
					new String[0]);
			System.out.println("Commands with a return value:");
			for (String s : retcomms) {
				System.out.println(s.toLowerCase());
			}

		}
	}

	@Override
	public String getCommandUsage() {
		return "\"help <command>\" shows help for <command>\r\n \"help\" shows a list of all commands";
	}

	@Override
	public ArrayList<String> getCompletion(int i, String toComplete) {
		if (i == 0) {
			Set<String> keys = Command.commands.keySet();
			ArrayList<String> possible = new ArrayList<>();
			String out = "";
			for (String c : keys) {
				if (c.length() >= toComplete.length()
						&& c.substring(0, toComplete.length())
								.equalsIgnoreCase(toComplete)) {
					possible.add(c);
					if (out.equals("")) {
						out += c;
					} else {
						out += ", " + c;
					}
				}
			}
			keys = ReturningCommand.retCommands.keySet();
			out = "";
			for (String c : keys) {
				if (c.length() >= toComplete.length()
						&& c.substring(0, toComplete.length())
								.equalsIgnoreCase(toComplete)) {
					possible.add(c);
					if (out.equals("")) {
						out += c;
					} else {
						out += ", " + c;
					}
				}
			}

			return possible;
		}
		return new ArrayList<>();
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}
}
