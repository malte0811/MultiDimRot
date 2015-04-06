package malte0811.multiDim.commands.ser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;

public class CommandSerialize extends Command {

	@Override
	public String getCommandName() {
		return "SERIALIZE";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("1 argument is required");
			return;
		}
		String s = System.getProperty("file.separator");
		File f = new File(System.getProperty("user.dir") + s + "solids" + s
				+ args[0]);
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(DimRegistry.getCalcThread().getSolid());
		oos.close();
	}

	@Override
	public String getCommandUsage() {
		return "\"serialize <file>\" stores the current solid in file (relative path)";
	}

}
