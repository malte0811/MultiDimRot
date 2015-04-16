package malte0811.multiDim.commands.show;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.solids.NDSphere;

public class CommandShowSphere extends Command {

	@Override
	public String getCommandName() {
		return "SHOWSPHERE";
	}

	@Override
	public void processCommand(String[] args) {
		int dim = (int) Programm.getValue(args[0]);
		int res = (int) Programm.getValue(args[1]);
		DimRegistry.getCalcThread().setSolid(new NDSphere(dim, res));
	}

	@Override
	public String getCommandUsage() {
		return "\"showsphere <n> <res>\" shows an n-dimensional sphere with a resolution of res degrees";
	}

	@Override
	public int getMinParameterCount() {
		return 2;
	}
}
