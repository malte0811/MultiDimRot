package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;

public class RetCommandProd extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "PROD";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"prod(start, end, term)\" returns the value of the product defined by these three values. "
				+ "_prodI is used as an index variable "
				+ "(this command works similiar to \"sum\")";
	}

	@Override
	public double processCommand(String[] args, HashMap<String, Double> var) {
		double start = MathHelper.calculate(args[0], var);
		double max = MathHelper.calculate(args[1], var);
		double ret = 1;
		for (double i = start; i <= max; i++) {
			var.put("_prodI", i);
			ret *= MathHelper.calculate(args[2], var);
		}
		var.remove("_prodI");
		return ret;
	}

	@Override
	public int getMinParameterCount() {
		return 3;
	}

}
