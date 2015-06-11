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
		try {
			System.out.println(Programm.getDoubleValue(args[0]));
		} catch (IllegalArgumentException x) {
			try {
				System.out.println(Programm.getStringValue(args[0]));
			} catch (IllegalArgumentException x2) {
				System.out.println(x.getMessage());
				System.out.println(x2.getMessage());
			}
		}
	}

}
