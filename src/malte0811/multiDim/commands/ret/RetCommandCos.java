package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;

public class RetCommandCos extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "COS";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"cos(x)\" returns the cosinus of x (x in radians)";
	}

	@Override
	public double processCommand(String[] args, HashMap<String, Double> var) {
		return Math.cos(MathHelper.calculate(args[0], var));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
