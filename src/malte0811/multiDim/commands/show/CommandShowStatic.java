package malte0811.multiDim.commands.show;

import java.lang.reflect.Constructor;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.solids.Solid;

public class CommandShowStatic extends Command {

	@Override
	public String getCommandName() {
		return "SHOWSTATIC";
	}

	@Override
	public void processCommand(String[] args) {
		try {
			if (args.length != 1) {
				System.out.println("1 argument is required");
			}
			Class<Solid> c = (Class<Solid>) Class
					.forName("malte0811.multiDim.solids." + args[0]);
			Constructor<Solid> con = c.getConstructor();
			DimRegistry.getCalcThread().setSolid(con.newInstance());
		} catch (Error e) {
			e.printStackTrace();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	@Override
	public String getCommandUsage() {
		return "\"showStatic <className>\" shows the solid defined by malte0811.multidim.solids.className. Currently only for \"HyperCube\"";
	}

}
