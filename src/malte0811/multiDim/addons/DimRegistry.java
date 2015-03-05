package malte0811.multiDim.addons;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import malte0811.multiDim.CalcThread;
import malte0811.multiDim.render.CentralOne;
import malte0811.multiDim.render.CentralThree;
import malte0811.multiDim.render.CentralTwo;
import malte0811.multiDim.render.ParallelRender;
import malte0811.multiDim.render.RenderAlgo;

public class DimRegistry {

	private static HashMap<Integer, Class<? extends RenderAlgo>> renderAlgos = new HashMap<>();
	public static CalcThread instance = null;
	static {
		renderAlgos.put(0, ParallelRender.class);
		renderAlgos.put(1, CentralOne.class);
		renderAlgos.put(2, CentralTwo.class);
		renderAlgos.put(3, CentralThree.class);
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

	public static void addCommand(Command c) {
		Command.register(c);
	}

	public static CalcThread getCalcThread() {
		return instance;
	}

	public static void addTickHandler(TickHandler th) {
		getCalcThread().handlers.add(th);
	}

}
