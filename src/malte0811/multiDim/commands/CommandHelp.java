package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;

public class CommandHelp extends Command {

	@Override
	public String getCommandName() {
		return "HELP";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		System.out.println("sleep");
		if (args.length >= 1) {
			System.out.println(((Command) Command.commands.get(args[0]
					.toUpperCase())).getCommandUsage());
		} else {
			String[] comms = Command.commands.keySet().toArray(new String[0]);
			for (String s : comms) {
				System.out.println(s.toLowerCase());
			}
		}
	}

	@Override
	public String getCommandUsage() {
		return "\"help <command>\" shows help for <command>\r\n \"help\" shows a list of all commands";
	}

}
