package multiDimRot.gui.transformations;

public abstract class Transformation {
	public abstract String getHumanString(boolean multible);
	public abstract String getParameterString(boolean multible);
	public abstract boolean isValid(int dims);
}
