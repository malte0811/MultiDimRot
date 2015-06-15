package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;
import malte0811.multiDim.commands.programs.Programm;

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
	public double processCommand(String[] args) {
		double start = Integer.parseInt(args[0]);
		double max = Integer.parseInt(args[1]);
		double ret = 1;
		Programm c = DimRegistry.getCalcThread().getCurrentProgram();
		HashMap<String, Double> var = (c == null) ? new HashMap<String, Double>()
				: c.getDoubleVariables();
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
