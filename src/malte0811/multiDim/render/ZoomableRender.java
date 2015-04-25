package malte0811.multiDim.render;

public abstract class ZoomableRender extends RenderAlgo {

	@Override
	public abstract void render(double[][] vertices, int[][] edges,
			double[] options, float[][] colors, int[][] sides);

	@Override
	public double[] getInitialParams() {
		return new double[] { 5, 3 };
	}

}
