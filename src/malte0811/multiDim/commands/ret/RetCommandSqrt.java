package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;

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
	public double processCommand(String[] args, HashMap<String, Double> var) {
		return Math.sqrt(MathHelper.calculate(args[0], var));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
