package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.commands.programs.Programm;

public class CommandPrint extends Command {

	@Override
	public String getCommandName() {
		return "PRINT";
	}

	@Override
	public String getCommandUsage() {
		return "\"print <v>\" prints out the value of v (variable or command with return value)";
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		System.out.println(args[0]);
		System.out.println(Programm.getValue(args[0]));
	}

}
