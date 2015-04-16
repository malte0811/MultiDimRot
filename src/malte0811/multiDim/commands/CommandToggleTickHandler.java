package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;

public class CommandToggleTickHandler extends Command {

	@Override
	public String getCommandName() {
		return "TOGGLETICKHANDLER";
	}

	@Override
	public String getCommandUsage() {
		return "\"toggleTickHandler <id> [true/false]\" activates/deactivates tick handler id";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		int id = Integer.parseInt(args[0]);
		if (args.length == 1) {
			DimRegistry.getCalcThread().toggleTickHandler(id);
		} else {
			boolean b = Boolean.parseBoolean(args[1]);
			DimRegistry.getCalcThread().getTickHandlers().get(id).setActive(b);
		}
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}
}
