package malte0811.multiDim.commands.show;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.solids.euclidND.NDTetrahedron;

public class CommandShowTetrahedron extends Command {

	@Override
	public String getCommandName() {
		return "SHOWTETRAHEDRON";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		int dim = (int) Programm.getDoubleValue(args[0]);
		DimRegistry.getCalcThread().setSolid(new NDTetrahedron(dim));
	}

	@Override
	public String getCommandUsage() {
		return "\"showTetrahedron <dim>\" shows an n-dimensional tetrahedron";
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}
}
