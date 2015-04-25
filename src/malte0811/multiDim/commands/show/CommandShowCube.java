package malte0811.multiDim.commands.show;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.solids.euclidND.NDCube;

public class CommandShowCube extends Command {

	@Override
	public String getCommandName() {
		return "SHOWCUBE";
	}

	@Override
	public void processCommand(String[] args) {
		int dims;
		dims = (int) Programm.getValue(args[0]);
		DimRegistry.getCalcThread().setSolid(new NDCube(dims));
	}

	@Override
	public String getCommandUsage() {
		return "\"showcube <n>\" zeigt einen n-dimensionalen W\u00FCrfel";
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}
}
