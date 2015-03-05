package malte0811.multiDim.render;

import java.util.Arrays;

public class CentralTwo extends ZoomableRender {
	@Override
	public void render(double[][] vertices, int[][] edges, double[] options,
			boolean renderVertices, float[][] colors) {
		int length = vertices[0].length;
		for (int i = 0; i < vertices.length; i++) {
			while (vertices[i] != null && vertices[i].length > 2) {
				vertices[i] = zentralDownOne(vertices[i], true, options);
			}
		}
		float[][] ret = new float[vertices.length][2];
		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i] != null) {
				ret[i][0] = (float) vertices[i][0];
				ret[i][1] = (float) vertices[i][1];
			} else {
				ret[i] = null;
			}
		}

		if (colors != null) {
			renderColor(ret, edges, colors, RenderAlgo.mainShaderId);
		} else {
			renderNoColor(ret, edges, RenderAlgo.mainShaderId);
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
