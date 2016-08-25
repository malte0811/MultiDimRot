package multiDimRot.gui.transformations;

public class Rotation extends Transformation {
	final int a0, a1;
	final float angle;
	final boolean inc;
	public Rotation(int ax0, int ax1, float a, boolean i) {
		a0 = ax0;
		a1 = ax1;
		angle = a;
		inc = i;
	}
	@Override
	public String getHumanString(boolean multible) {
		return "Rotate in the plane "+a0+"|"+a1+"\n by "+angle+"°"+(inc?"\n per frame (40 FPS)":"");
	}
	@Override
	public String getParameterString(boolean matVec) {
		if (matVec) {
			return (inc?"rotInc ":"rotConst ")+a0+" "+a1+" "+angle;
		} else {
			return "rotate "+a0+" "+a1+" "+angle;
		}
	}
	@Override
	public boolean isValid(int dims) {
		return a0<dims&&a1<dims;
	}
}
