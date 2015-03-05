package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.tickHandlers.MediaHandler;

public class CommandScreenShot extends Command {

	@Override
	public String getCommandName() {
		return "SCREENSHOT";
	}

	@Override
	public String getCommandUsage() {
		return "\"screenshot\" saves a screenshot";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		MediaHandler.instance.screenShot();
	}

}
