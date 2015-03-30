package malte0811.multiDim.addons;

import malte0811.multiDim.solids.Solid;
import malte0811.multiDim.tickHandlers.DebugHandler;
import malte0811.multiDim.tickHandlers.MediaHandler;

public abstract class TickHandler {
	static {
		DimRegistry.addTickHandler(new DebugHandler());
		DimRegistry.addTickHandler(new MediaHandler());
	}

	/**
	 * Called once every Tick, so 10x per second
	 */
	public abstract void handleTick(Solid d, double[] renderoptions);
}
