package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;

public class CommandResizeSolid extends Command {

	@Override
	public String getCommandName() {
		return "RESIZE";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("At least 1 argument is required");
			return;
		}
		double m = Programm.getValue(args[0]);
		if (args.length == 2) {
			int x = (int) Programm.getValue(args[1]);
			DimRegistry.getCalcThread().getSolid().resize(x, m);
		} else {
			DimRegistry.getCalcThread().getSolid().resize(m);
		}
	}

	@Override
	public String getCommandUsage() {
		return "\"resize <f> [axis]\" resizes the solid by the factor f. Optional: axis: the direction in which to stretch, otherwise all";
	}

}
