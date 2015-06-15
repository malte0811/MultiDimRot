package malte0811.multiDim.commands.show;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

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
			Class<Solid> c = (Class<Solid>) DimRegistry.getStaticSolids().get(
					args[0]);
			Constructor<Solid> con = c.getConstructor();
			DimRegistry.getCalcThread().setSolid(con.newInstance());
		} catch (Exception x) {
			System.out.println("The static solid " + args[0]
					+ " does not exist.");
		}
	}

	@Override
	public String getCommandUsage() {
		return "\"showStatic <className>\" shows the solid defined by malte0811.multidim.solids.className. Currently only for \"HyperCube\"";
	}

	@Override
	public ArrayList<String> getCompletion(int i, String toComplete) {
		ArrayList<String> ret = new ArrayList<>();
		HashMap<String, Class<? extends Solid>> poss = DimRegistry
				.getStaticSolids();
		for (String k : poss.keySet()) {
			if (k.startsWith(toComplete)) {
				ret.add(k);
			}
		}
		return ret;
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}
}