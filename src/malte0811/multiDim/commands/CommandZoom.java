package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;

public class CommandZoom extends Command {

	@Override
	public String getCommandName() {
		return "ZOOM";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		double max = Programm.getDoubleValue(args[0]);
		double step = Programm.getDoubleValue(args[1]);
		DimRegistry.getCalcThread().setZoomMax(max);
		DimRegistry.getCalcThread().setZoomStep(step);
	}

	@Override
	public String getCommandUsage() {
		return "\"zoom <max> <step>\" zooms in/out in steps of step 10 times per second until max is reached";
	}

	@Override
	public int getMinParameterCount() {
		return 2;
	}
}
