package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandSin extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "SIN";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"sin(x)\" returns the sinus of x (x in radians)";
	}

	@Override
	public double processCommand(String[] args) {
		return Math.sin(Programm.getDoubleValue(args[0]));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
