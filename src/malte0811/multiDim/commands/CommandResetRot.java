package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;

public class CommandResetRot extends Command {

	@Override
	public String getCommandName() {
		return "RESETROT";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		DimRegistry.getCalcThread().setRotations(new double[0][3]);
	}

	@Override
	public String getCommandUsage() {
		return "\"resetrot\" stops the rotation started by \"rotcon\"";
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}
}
