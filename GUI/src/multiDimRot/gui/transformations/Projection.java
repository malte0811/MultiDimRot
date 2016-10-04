package multiDimRot.gui.transformations;

public class Projection extends Transformation {
	int dimension;
	float distance;
	// d<=0 means project down to 2d
	public Projection(int d, float dist) {
		dimension = d;
		distance = dist;
	}
	@Override
	public String getHumanString(boolean multiple) {
		if (dimension>0) {
			return "Project onto a "+(dimension-1)+" dimensional space from distance "+distance;
		} else {
			return "Project down to 2 dimensions from distance "+distance;
		}
	}

	@Override
	public String getParameterString(boolean multiple) {
		if (dimension>0) {
			return "project "+dimension+" "+distance;
		} else {
			return "projectAll "+distance;
		}
	}

	@Override
	public boolean isValid(int dims) {
		return dimension<=dims&&distance>0;
	}

}
