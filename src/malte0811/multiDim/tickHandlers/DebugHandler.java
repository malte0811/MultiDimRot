package malte0811.multiDim.tickHandlers;

import malte0811.multiDim.addons.TickHandler;
import malte0811.multiDim.solids.Solid;

import org.lwjgl.Sys;

public class DebugHandler extends TickHandler {
	private static DebugHandler instance;
	int tickTot = -1;
	int tickLeft = -1;
	int[][] tickTimes;

	@Override
	public void handleTick(Solid d, double[] renderoptions) {
		instance = this;
		if (tickLeft >= 0) {
			tickLeft--;
		} else if (tickLeft == -1 && tickTot > 0) {
			tickLeft--;
			System.out.println("Finished data collection. Timer resolution: "
					+ Sys.getTimerResolution());
			for (int i = 0; i < tickTimes.length; i++) {
				double tot = 0;
				for (int i2 : tickTimes[i]) {
					tot += i2;
				}
				System.out.println("Avrg. delay at " + i + " meassuered "
						+ tickTot + " times:" + (tot / (double) tickTot));
			}
		}
	}

	public void newDebug(int dur, int am) {
		tickTimes = new int[am][dur + 1];
		tickTot = dur;
		tickLeft = dur;
	}

	public void addTime(int i, int val) {
		if (tickLeft < 0 || i >= tickTimes.length) {
			return;
		}
		tickTimes[i][tickLeft] = val;
	}

	public static DebugHandler getInstance() {
		if (instance == null) {
			// has not been initialized yet, return fake
			return new DebugHandler();
		}
		return instance;
	}
}
