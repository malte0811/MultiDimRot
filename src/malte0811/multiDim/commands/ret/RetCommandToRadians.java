package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;

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
	public double processCommand(String[] args, HashMap<String, Double> var) {
		return Math.toRadians(MathHelper.calculate(args[0], var));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
