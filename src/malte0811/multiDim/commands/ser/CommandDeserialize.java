package malte0811.multiDim.commands.ser;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.solids.Solid;

public class CommandDeserialize extends Command {

	@Override
	public String getCommandName() {
		return "DESERIALIZE";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		String s = DimRegistry.getFileSeperator();
		File f = new File(DimRegistry.getUserDir() + s + "solids" + s + args[0]);
		FileInputStream fos = new FileInputStream(f);
		ObjectInputStream oos = new ObjectInputStream(fos);
		DimRegistry.getCalcThread().setSolid((Solid) oos.readObject());
		oos.close();
	}

	@Override
	public String getCommandUsage() {
		return "\"deserialize <file>\" loads the solid stored in file (relative path)";
	}

	@Override
	public ArrayList<String> getCompletion(int i, String toComplete) {
		if (i == 0) {
			String s = DimRegistry.getFileSeperator();
			return getFiles(new File(DimRegistry.getUserDir() + s + "solids"),
					toComplete);
		}
		return new ArrayList<>();
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}
}
