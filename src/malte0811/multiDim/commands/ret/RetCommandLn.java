package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandLn extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "LN";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"ln(a)\" returns the natural logarithm of a";
	}

	@Override
	public double processCommand(String[] args) {
		return Math.log(Programm.getDoubleValue(args[0]));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
