package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;

public class CommandRotCon extends Command {

	@Override
	public String getCommandName() {
		return "ROTCON";
	}

	@Override
	public void processCommand(String[] args) {
		int a1 = (int) Programm.getDoubleValue(args[0]);
		int a2 = (int) Programm.getDoubleValue(args[1]);
		double deg = Programm.getDoubleValue(args[2]);
		if (a1 == a2) {
			System.out.println("Can not rotate with a single axis");
			return;
		}
		if (a1 < 0) {
			System.out.println("The axis " + a1
					+ " does not exist. The smallest number for an axis is 0.");
			return;
		} else if (a2 < 0) {
			System.out.println("The axis " + a2
					+ " does not exist. The smallest number for an axis is 0.");
			return;
		}

		DimRegistry.getCalcThread().addRotCon(new double[] { a1, a2, deg });
	}

	@Override
	public String getCommandUsage() {
		return "\"rotcon <axis1> <axis2> <degrees>\" rotates the solid around degrees degrees in the plane defined by the axes axis1 and axis2 every 1/10 second";
	}

	@Override
	public int getMinParameterCount() {
		return 3;
	}
}
