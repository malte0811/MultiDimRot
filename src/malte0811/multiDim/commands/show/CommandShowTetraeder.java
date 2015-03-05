package malte0811.multiDim.commands.show;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.solids.euclidND.NDTetraeder;

public class CommandShowTetraeder extends Command {

	@Override
	public String getCommandName() {
		return "SHOWTETRAEDER";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("1 argument is required");
			return;
		}
		int dim = (int) Programm.getValue(args[0]);
		DimRegistry.getCalcThread().solid = new NDTetraeder(dim);
	}

	@Override
	public String getCommandUsage() {
		return "\"showTetraeder <dim>\" shows an n-dimensional tetrahedron";
	}

}
