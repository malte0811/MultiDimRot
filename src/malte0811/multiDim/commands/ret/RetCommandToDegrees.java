package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandToDegrees extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "TODEGREES";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"toDegrees(x)\" returns the value of x in degrees (x in radians)";
	}

	@Override
	public double processCommand(String[] args) {
		return Math.toDegrees(Programm.getDoubleValue(args[0]));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
