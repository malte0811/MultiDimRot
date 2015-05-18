package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandCos extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "COS";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"cos(x)\" returns the cosinus of x (x in radians)";
	}

	@Override
	public double processCommand(String[] args) {
		return Math.cos(Programm.getDoubleValue(args[0]));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
