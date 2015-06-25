package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;

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
	public double processCommand(String[] args, HashMap<String, Double> var) {
		return Math.toDegrees(MathHelper.calculate(args[0], var));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
