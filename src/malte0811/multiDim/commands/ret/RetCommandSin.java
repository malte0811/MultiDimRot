package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;

public class RetCommandSin extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "SIN";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"sin(x)\" returns the sinus of x (x in radians)";
	}

	@Override
	public double processCommand(String[] args, HashMap<String, Double> var) {
		return Math.sin(MathHelper.calculate(args[0], var));
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
