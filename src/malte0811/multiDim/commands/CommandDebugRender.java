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
		return "\"DEBUG <t>\" prints out rendering debug information for t rendering ticks";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		DebugHandler.getInstance().newDebug(Integer.parseInt(args[0]), 4);
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}
}
