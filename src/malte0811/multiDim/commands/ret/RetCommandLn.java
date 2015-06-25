package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;

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
	public double processCommand(String[] args, HashMap<String, Double> var) {
		return Math.log(MathHelper.calculate(args[0], var));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
