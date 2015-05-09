package malte0811.multiDim.commands.show;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.solids.euclidND.NDOctahedron;

public class CommandShowOctahedron extends Command {

	@Override
	public String getCommandName() {
		return "SHOWOCTAHEDRON";
	}

	@Override
	public void processCommand(String[] args) {
		int dims;
		dims = (int) Programm.getDoubleValue(args[0]);
		DimRegistry.getCalcThread().setSolid(new NDOctahedron(dims));
	}

	@Override
	public String getCommandUsage() {
		return "\"showOctahedron <n>\" shows an n-dimensional octahedron";
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}
}
