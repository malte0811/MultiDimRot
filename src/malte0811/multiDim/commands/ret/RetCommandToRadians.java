package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandToRadians extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "TORADIANS";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"toRadians(x)\" returns the value of x in radians (x in degrees)";
	}

	@Override
	public double processCommand(String[] args) {
		return Math.toRadians(Programm.getDoubleValue(args[0]));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
