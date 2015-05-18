package malte0811.multiDim.addons;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import malte0811.multiDim.CalcThread;
import malte0811.multiDim.render.CentralOne;
import malte0811.multiDim.render.CentralThree;
import malte0811.multiDim.render.CentralTwo;
import malte0811.multiDim.render.ParallelRender;
import malte0811.multiDim.render.RenderAlgo;
import malte0811.multiDim.solids.HyperCube;
import malte0811.multiDim.solids.Solid;
import malte0811.multiDim.solids.TestingSolid;

public class DimRegistry {

	private static HashMap<Integer, Class<? extends RenderAlgo>> renderAlgos = new HashMap<>();
	private static HashMap<String, Class<? extends Solid>> staticSolids = new HashMap<>();
	private static String userDir;
	private static String sep;

	public static CalcThread instance = null;
	static {
		renderAlgos.put(0, ParallelRender.class);
		renderAlgos.put(1, CentralOne.class);
		renderAlgos.put(2, CentralTwo.class);
		renderAlgos.put(3, CentralThree.class);
		staticSolids.put("HyperCube", HyperCube.class);
		staticSolids.put("TestingSolid", TestingSolid.class);

		sep = System.getProperty("file.separator");
		userDir = System.getProperty("user.dir");
	}

	public static void addRenderAlgo(Class<? extends RenderAlgo> newAlgo,
			int index) {
		renderAlgos.put(index, newAlgo);
	}

	public static RenderAlgo getAlgoInstance(int i) throws Exception {
		Class<? extends RenderAlgo> c = renderAlgos.get(i);
		Constructor<? extends RenderAlgo> constr = c.getConstructor();
		RenderAlgo instance = constr.newInstance();
		return instance;
	}

	public static void addCommand(Command c, boolean forceOverride) {
		if (!Command.commands.containsKey(c.getCommandName()) || forceOverride) {
			Command.register(c);
		} else {
			System.out.println("This command already exists: "
					+ c.getCommandName());
			System.exit(1);
		}
	}

	public static void addRetCommand(ReturningCommand c, boolean forceOverride) {
		if (!ReturningCommand.retCommands.containsKey(c.getRetCommandName())
				|| forceOverride) {
			ReturningCommand.register(c);
		} else {
			System.out
					.println("This command with return value already exists: "
							+ c.getRetCommandName());
			System.exit(1);
		}
	}

	public static CalcThread getCalcThread() {
		return instance;
	}

	public static void addTickHandler(TickHandler th) {
		getCalcThread().getTickHandlers().add(th);
	}

	public static void addStaticSolid(String name, Class<? extends Solid> c) {
		staticSolids.put(name, c);
	}

	public static HashMap<String, Class<? extends Solid>> getStaticSolids() {
		return staticSolids;
	}

	public static String getUserDir() {
		return userDir;
	}

	public static String getFileSeperator() {
		return sep;
	}
}
