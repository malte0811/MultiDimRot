package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;

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
	public double processCommand(String[] args, HashMap<String, Double> var) {
		return Math.pow(MathHelper.calculate(args[0], var),
				MathHelper.calculate(args[1], var));
	}

	@Override
	public int getMinParameterCount() {
		return 2;
	}

}
