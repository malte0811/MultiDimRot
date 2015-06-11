package malte0811.multiDim.commands.show;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.solids.NDTorus;

public class CommandShowTorus extends Command {

	@Override
	public String getCommandName() {
		return "SHOWTORUS";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		int dim = (int) Programm.getDoubleValue(args[0]);
		double rad = Programm.getDoubleValue(args[1]);
		int res = (int) Programm.getDoubleValue(args[2]);
		DimRegistry.getCalcThread().setSolid(new NDTorus(dim, rad, res));
	}

	@Override
	public String getCommandUsage() {
		return "\"showTorus <n> <rad> <res>\" shows an n-dimensional torus with a radius of rad and a resolution of res degrees";
	}

	@Override
	public int getMinParameterCount() {
		return 3;
	}
}
