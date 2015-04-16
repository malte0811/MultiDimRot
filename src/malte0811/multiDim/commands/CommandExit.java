package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;

public class CommandExit extends Command {

	@Override
	public String getCommandName() {
		return "EXIT";
	}

	@Override
	public void processCommand(String[] args) {
		System.exit(0);
	}

	@Override
	public String getCommandUsage() {
		return "\"exit\" Stops MultiDimRot";
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}
}
