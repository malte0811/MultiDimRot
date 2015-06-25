package malte0811.multiDim.tickHandlers;

import malte0811.multiDim.addons.TickHandler;
import malte0811.multiDim.solids.Solid;

import org.lwjgl.input.Mouse;

public class MouseHandler extends TickHandler {
	@Override
	public void handleTick(Solid d, double[] renderoptions) {
		float dWheel = Mouse.getDWheel();
		if (dWheel != 0) {
			float dim = d.getVertices()[0].length;
			float zoom = (float) (dWheel / (dim * 40) * renderoptions[0] * 0.1F);
			renderoptions[0] -= zoom;
		}
	}

}
