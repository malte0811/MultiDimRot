package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.ReturningCommand;

public class RetCommandToDouble extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "TODOUBLE";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"toDouble(<s>)\" returns the double/numeric value of s";
	}

	@Override
	public double processCommand(String[] args) {
		return Double.parseDouble(args[0]);
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
