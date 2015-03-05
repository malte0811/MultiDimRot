package malte0811.multiDim.commands.ser;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

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
		if (args.length != 1) {
			System.out.println("1 argument is required");
			return;
		}
		File f = new File(System.getProperty("user.dir") + "\\" + args[0]);
		FileInputStream fos = new FileInputStream(f);
		ObjectInputStream oos = new ObjectInputStream(fos);
		DimRegistry.getCalcThread().solid = (Solid) oos.readObject();
		oos.close();
	}

	@Override
	public String getCommandUsage() {
		return "\"deserialize <file>\" loads the solid stored in file (relative path)";
	}

}
