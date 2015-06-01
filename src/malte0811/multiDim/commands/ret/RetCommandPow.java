package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandPow extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "POW";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"pow(a, b)\" return the a to the power of b (a^b)";
	}

	@Override
	public double processCommand(String[] args) {
		return Math.pow(Programm.getDoubleValue(args[0]),
				Programm.getDoubleValue(args[1]));
	}

	@Override
	public int getMinParameterCount() {
		return 2;
	}

}
