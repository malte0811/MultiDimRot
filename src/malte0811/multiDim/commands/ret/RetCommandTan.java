package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;

public class RetCommandTan extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "TAN";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"tan(x)\" returns the tangens of x (x in radians)";
	}

	@Override
	public double processCommand(String[] args, HashMap<String, Double> var) {
		return Math.tan(MathHelper.calculate(args[0], var));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
