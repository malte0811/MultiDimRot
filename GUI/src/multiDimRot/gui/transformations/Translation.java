package multiDimRot.gui.transformations;

public class Translation extends Transformation {
	private int dim;
	private float amount;
	public Translation(int d, float a) {
		dim = d;
		amount = a;
	}
	@Override
	public String getHumanString(boolean multiple) {
		return "Translate into direction "+dim+" by "+amount;
	}

	@Override
	public String getParameterString(boolean multiple) {
		return "translate "+dim+" "+amount;
	}

	@Override
	public boolean isValid(int dims) {
		return dim<dims;
	}

}
