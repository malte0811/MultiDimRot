package malte0811.multiDim.commands.show;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.solids.LorenzSystem;

public class CommandShowLorenz extends Command {

	@Override
	public String getCommandName() {
		return "SHOWLORENZ";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length != 2 && args.length != 5 && args.length != 8) {
			System.out.println("2, 5 or 8 argumets are required");
			return;
		}
		double res = Programm.getValue(args[0]);
		int steps = (int) Programm.getValue(args[1]);
		double a = 28, b = 10, c = 8D / 3D;
		double x = 0, y = 1, z = 0;
		if (args.length >= 5) {
			x = Programm.getValue(args[2]);
			y = Programm.getValue(args[3]);
			z = Programm.getValue(args[4]);
		}
		if (args.length == 8) {
			a = Programm.getValue(args[5]);
			b = Programm.getValue(args[6]);
			c = Programm.getValue(args[7]);
		}
		DimRegistry.getCalcThread().setSolid(
				new LorenzSystem(x, y, z, a, b, c, res, steps));
	}

	@Override
	public String getCommandUsage() {
		return "\"showlorenz <res> <steps> [<x> <y> <z> [<a> <b> <c>]]\"shows the first steps steps of a lorenz system with resolution res. Optional: x, y, z: initial value, a, b, c: parameters for the system";
	}

}
