package malte0811.multiDim.render;

public class ParallelRender extends RenderAlgo {

	@Override
	public void render(double[][] vertices, int[][] edges, double[] options,
			float[][] colors, int[][] sides) {
		float[][] rV = new float[vertices.length][2];
		for (int i = 0; i < vertices.length; i++) {
			double[] vertex = vertices[i];
			rV[i][0] = (float) vertex[0] / 2;
			rV[i][1] = (float) vertex[1] / 2;
		}
		if (colors == null) {
			renderNoColor(rV, edges, RenderAlgo.mainShaderId);
		} else {
			renderColor(rV, edges, colors, RenderAlgo.mainShaderId);
		}
	}

	@Override
	public double[] getInitialParams() {
		return new double[] { 0, 1 };
	}

}