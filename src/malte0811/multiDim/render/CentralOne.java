package malte0811.multiDim.render;

import java.util.Arrays;

public class CentralOne extends ZoomableRender {

	@Override
	public void render(double[][] vertices, int[][] edges, double[] options,
			boolean renderVertices, float[][] colors) {
		for (double[] d : vertices) {
			while (d.length > 2) {
				d = zentralDownOne(d, false, options);
			}
		}
		float[][] rV = new float[vertices.length][2];

		for (int i = 0; i < rV.length; i++) {
			rV[i][0] = (float) vertices[i][0];
			rV[i][1] = (float) vertices[i][1];
		}
		if (colors != null) {
			renderColor(rV, edges, colors, RenderAlgo.mainShaderId);
		} else {
			renderNoColor(rV, edges, RenderAlgo.mainShaderId);
		}
	}

	private double[] zentralDownOne(double[] in, boolean clean, double[] options) {
		int dim = in.length;
		in[dim - 1] += options[0];
		if (in[dim - 1] <= 0 && clean) {
			return null;
		}
		for (int i = 0; i < dim - 1; i++) {
			in[i] = options[1] * in[i] / in[dim - 1];
		}
		in = Arrays.copyOf(in, in.length - 1);
		return in;
	}

}
