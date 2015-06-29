package malte0811.multiDim.commands;

import malte0811.multiDim.CommandListener;
import malte0811.multiDim.addons.Command;

public class CommandClear extends Command {

	@Override
	public String getCommandName() {
		return "CLEAR";
	}

	@Override
	public String getCommandUsage() {
		return "\"clear\" clears the output from the gui. The log file will not be cleared.";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		CommandListener.output.setText("");
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}
}
