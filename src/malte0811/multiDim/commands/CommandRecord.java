package malte0811.multiDim.commands;

import java.io.File;
import java.util.ArrayList;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.tickHandlers.MediaHandler;

public class CommandRecord extends Command {

	@Override
	public String getCommandName() {
		return "RECORD";
	}

	@Override
	public String getCommandUsage() {
		return "\"record t p o\" starts the programm p and records the first t ticks to the file o";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		int ticks = Integer.parseInt(args[0]);
		DimRegistry.getCalcThread().addCommand(args[1]);
		String sep = System.getProperty("file.separator");
		File f = new File(System.getProperty("user.dir") + sep + "videos" + sep
				+ args[2]);
		MediaHandler.instance.record(ticks, f);
	}

	@Override
	public ArrayList<String> getCompletion(int i, String toComplete) {
		if (i == 1) {
			return getFiles(
					new File(System.getProperty("user.dir")
							+ System.getProperty("file.separator") + "scripts"),
					toComplete);
		}
		return new ArrayList<>();
	}
}
