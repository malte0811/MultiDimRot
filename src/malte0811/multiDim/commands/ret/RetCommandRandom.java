package malte0811.multiDim.commands.ret;

import java.util.Random;

import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandRandom extends ReturningCommand {
	Random r = new Random();

	@Override
	public String getRetCommandName() {
		return "RANDOM";
	}

	@Override
	public String getRetCommandUsage() {
		// TODO Auto-generated method stub
		return "\"random([<min>, <max>])\" creates a random number (double) between 0 and 1. If min and max are given, the random number is generated between min and max.";
	}

	@Override
	public double processCommand(String[] args) {
		if (args.length == 2) {
			double min = Programm.getValue(args[0]);
			double max = Programm.getValue(args[1]);
			return min + (max - min) * r.nextDouble();
		}
		return r.nextDouble();
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}

}
