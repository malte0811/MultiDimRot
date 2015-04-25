package malte0811.multiDim.addons;

import malte0811.multiDim.solids.Solid;
import malte0811.multiDim.tickHandlers.DebugHandler;
import malte0811.multiDim.tickHandlers.MediaHandler;

public abstract class TickHandler {
	private boolean active = true;
	static {
		DimRegistry.addTickHandler(new DebugHandler());
		DimRegistry.addTickHandler(new MediaHandler());
	}

	/**
	 * Called once every tick, so 10x per second
	 */
	public abstract void handleTick(Solid d, double[] renderoptions);

	public void setActive(boolean ac) {
		active = ac;
	}

	public boolean isActive() {
		return active;
	}
}
