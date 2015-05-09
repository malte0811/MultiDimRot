package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;

public class CommandChangeRenderOption extends Command {

	@Override
	public String getCommandName() {
		return "CHANGERENDEROPTION";
	}

	@Override
	public void processCommand(String[] args) {
		int i = (int) Programm.getDoubleValue(args[0]);
		double v = Programm.getDoubleValue(args[1]);
		DimRegistry.getCalcThread().getRenderOptions()[i] = v;
	}

	@Override
	public String getCommandUsage() {
		return "\"changerenderoption <index> <newValue>\" sets the rendering option index to newValue. In most cases, zoom can be used instead";
	}

	@Override
	public int getMinParameterCount() {
		return 2;
	}
}
