package multiDimRot.gui.transformations;

public class Scaling extends Transformation {
	private int dim;
	private float amount;
	/**
	 * d=-1 means scale in all directions
	 */
	public Scaling(int d, float a) {
		dim = d;
		amount = a;
	}
	@Override
	public String getHumanString(boolean multiple) {
		if (dim!=-1) {
			return "Scale in direction "+dim+" by "+amount;
		} else {
			return "Scale by "+amount;
		}
	}

	@Override
	public String getParameterString(boolean multiple) {
		if (dim==-1) {
			return "scaleAll "+amount;
		} else {
			return "scale "+dim+" "+amount;
		}
	}

	@Override
	public boolean isValid(int dims) {
		return dim<dims;
	}

}
