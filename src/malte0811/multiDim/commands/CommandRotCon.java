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
		if (args.length != 3) {
			System.out.println("3 arguments are required");
			return;
		}
		if (args[0] == args[1]) {
			System.out.println("Can not rotate with a single axis");
			return;
		}
		DimRegistry.getCalcThread().addRotCon(
				new int[] { (int) Programm.getValue(args[0]),
						(int) Programm.getValue(args[1]),
						(int) Programm.getValue(args[2]) });
	}

	@Override
	public String getCommandUsage() {
		return "\"rotcon <axis1> <axis2> <degrees>\" rotates the solid around degrees degrees in the plane defined by the axes axis1 and axis2 every 1/10 second";
	}

}
