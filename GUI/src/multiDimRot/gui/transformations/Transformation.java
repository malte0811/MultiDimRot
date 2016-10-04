package multiDimRot.gui.transformations;

public abstract class Transformation {
	public abstract String getHumanString(boolean multiple);
	public abstract String getParameterString(boolean multiple);
	public abstract boolean isValid(int dims);
}
