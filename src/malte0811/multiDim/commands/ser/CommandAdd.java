package malte0811.multiDim.commands.ser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InvalidClassException;
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
		String sep = System.getProperty("file.separator");
		Solid a = null, b = null;

		try {
			File f = new File(System.getProperty("user.dir") + sep + "solids"
					+ sep + args[0]);
			FileInputStream fos = new FileInputStream(f);
			ObjectInputStream oos = new ObjectInputStream(fos);
			a = (Solid) oos.readObject();
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("The file " + System.getProperty("user.dir")
					+ sep + "solids" + sep + args[0] + " does not exist.");
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("The file: " + System.getProperty("user.dir")
					+ sep + "solids" + sep + args[0]
					+ " does not contain a valid solid.");
			return;
		} catch (InvalidClassException x) {
			System.out
					.println("The file: "
							+ System.getProperty("user.dir")
							+ sep
							+ "solids"
							+ sep
							+ args[0]
							+ " does not contain a valid solid. It may have been created by an older version of MultiDimRot.");
			return;
		}

		try {
			File f = new File(System.getProperty("user.dir") + sep + "solids"
					+ sep + args[1]);
			FileInputStream fos = new FileInputStream(f);
			ObjectInputStream oos = new ObjectInputStream(fos);
			b = (Solid) oos.readObject();
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("The file " + System.getProperty("user.dir")
					+ sep + "solids" + sep + args[1] + " does not exist.");
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("The file: " + System.getProperty("user.dir")
					+ sep + "solids" + sep + args[1]
					+ " does not contain a valid solid.");
			return;
		} catch (InvalidClassException x) {
			System.out
					.println("The file: "
							+ System.getProperty("user.dir")
							+ sep
							+ "solids"
							+ sep
							+ args[1]
							+ " does not contain a valid solid. It may have been created by an older version of MultiDimRot.");
			return;
		}

		DimRegistry.getCalcThread().setSolid(Solid.add(a, b, true));
	}

	@Override
	public String getCommandUsage() {
		return "\"add <file1> <file2>\" adds the solids stored in file1 and file2 (relative paths) using serialize";
	}

	@Override
	public ArrayList<String> getCompletion(int i, String toComplete) {
		if (i == 0 || i == 1) {
			String s = DimRegistry.getFileSeperator();
			return getFiles(new File(DimRegistry.getUserDir() + s + "solids"),
					toComplete);
		}
		return new ArrayList<>();
	}

	@Override
	public int getMinParameterCount() {
		return 2;
	}
}
