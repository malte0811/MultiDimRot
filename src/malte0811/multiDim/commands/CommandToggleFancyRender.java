package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.render.CentralThree;

public class CommandToggleFancyRender extends Command {

	@Override
	public String getCommandName() {
		return "TOGGLEFANCYSIDES";
	}

	@Override
	public String getCommandUsage() {
		return "\"toggleFancySides [true/false]\" turns the fancy rendering of sides on/off. This command will be removed once the fancy rendering is working better.";
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length == 1) {
			CentralThree.fancy = Boolean.parseBoolean(args[0]);
		} else {
			CentralThree.fancy = !CentralThree.fancy;
		}
	}

}
