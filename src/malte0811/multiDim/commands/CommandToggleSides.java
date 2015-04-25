package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;

public class CommandToggleSides extends Command {

	@Override
	public String getCommandName() {
		return "TOGGLESIDES";
	}

	@Override
	public String getCommandUsage() {
		return "\"togglesides\" switches side rendering off/on if the solid has sides";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length == 1) {
			DimRegistry.getCalcThread().setSidesActive(
					args[0].equalsIgnoreCase("on") ? true : false);
		} else {
			DimRegistry.getCalcThread().setSidesActive(
					!DimRegistry.getCalcThread().areSidesActive());
		}
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}
}
