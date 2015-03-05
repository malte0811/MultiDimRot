package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.tickHandlers.DebugHandler;

public class CommandDebugRender extends Command {

	@Override
	public String getCommandName() {
		return "DEBUG";
	}

	@Override
	public String getCommandUsage() {
		return "\"DEBUG\" prints out rendering debug information for on rendering tick";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("This command requires exactly 1 argument");
			return;
		}
		DebugHandler.getInstance().newDebug(Integer.parseInt(args[0]), 4);
	}

}
