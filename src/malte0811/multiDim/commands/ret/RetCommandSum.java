package malte0811.multiDim.commands.ret;

import java.util.HashMap;

import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.MathHelper;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandSum extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "SUM";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"sum(start, end, term)\" returns the value of the sum defined by these three values. "
				+ "_sumI is used as an index variable "
				+ "(see http://en.wikipedia.org/wiki/Summation, start is the value below the sigma, "
				+ "end the value above the sigma and term is what is right of the sigma)";
	}

	@Override
	public double processCommand(String[] args) {
		double start = Integer.parseInt(args[0]);
		double max = Integer.parseInt(args[1]);
		double ret = 0;
		Programm c = DimRegistry.getCalcThread().getCurrentProgram();
		HashMap<String, Double> var = (c == null) ? new HashMap<String, Double>()
				: c.getDoubleVariables();
		for (double i = start; i <= max; i++) {
			var.put("_sumI", i);
			ret += MathHelper.calculate(args[2], var);
		}
		var.remove("_sumI");
		return ret;
	}

	@Override
	public int getMinParameterCount() {
		return 3;
	}

}
