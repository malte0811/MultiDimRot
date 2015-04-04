package malte0811.multiDim.commands.ser;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.solids.Solid;

public class CommandAdd extends Command {

	@Override
	public String getCommandName() {
		return "ADD";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("2 arguments are required");
			return;
		}
		String sep = System.getProperty("file.separator");
		File f = new File(System.getProperty("user.dir") + sep + "solids" + sep
				+ args[0]);
		FileInputStream fos = new FileInputStream(f);
		ObjectInputStream oos = new ObjectInputStream(fos);
		Solid a = (Solid) oos.readObject();
		oos.close();
		f = new File(System.getProperty("user.dir") + sep + "solids" + sep
				+ args[1]);
		fos = new FileInputStream(f);
		oos = new ObjectInputStream(fos);
		Solid b = (Solid) oos.readObject();
		oos.close();
		DimRegistry.getCalcThread().solid = Solid.add(a, b, true);
	}

	@Override
	public String getCommandUsage() {
		return "\"add <file1> <file2>\" adds the solids stored in file1 and file2 (relative paths) using serialize";
	}

	@Override
	public ArrayList<String> getCompletion(int i, String toComplete) {
		if (i == 0 || i == 1) {
			String s = System.getProperty("file.separator");
			return getFiles(new File(System.getProperty("user.dir") + s
					+ "solids"), toComplete);
		}
		return new ArrayList<>();
	}
}
