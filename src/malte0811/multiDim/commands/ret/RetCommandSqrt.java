package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandSqrt extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "SQRT";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"sqrt(x)\" returns the square root of x.";
	}

	@Override
	public double processCommand(String[] args) {
		return Math.sqrt(Programm.getDoubleValue(args[0]));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
