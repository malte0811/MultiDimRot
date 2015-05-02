package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.addons.ReturningCommand;

public class RetCommandGetDimensions extends ReturningCommand {

	@Override
	public double processCommand(String[] args) {
		return DimRegistry.getCalcThread().getSolid().getCopyOfVertices(0)[0].length;
	}

	@Override
	public String getRetCommandName() {
		return "GETDIMENSIONS";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"getDimensions()\" returns the number of dimensions the current solid has";
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}

}
