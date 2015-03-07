package malte0811.multiDim.render;

import java.util.Arrays;
import java.util.HashSet;

public class CentralThree extends ZoomableRender {

	@Override
	public void render(double[][] vertices, int[][] edges, double[] options,
			boolean renderVertices, float[][] colors) {
		int length = vertices[0].length;
		boolean[] extraVertices = new boolean[vertices.length];
		while (length > 2) {
			double[][] oldVertices = new double[vertices.length][length];
			for (int i = 0; i < vertices.length; i++) {
				if (vertices[i] != null) {
					oldVertices[i] = Arrays.copyOf(vertices[i], length);
				}
			}
			for (int i = 0; i < vertices.length; i++) {
				if (vertices[i] != null) {
					vertices[i] = zentralDownOne(vertices[i], true, options);

				}
			}
			int currIndex = vertices.length;
			HashSet<Integer> fix = new HashSet<>();
			int am = 0;
			for (int i = 0; i < edges.length; i++) {
				if (vertices[edges[i][0]] == null
						&& vertices[edges[i][1]] == null) {
					continue;
				}

				if (vertices[edges[i][0]] == null) {
					boolean inF = true;
					for (int i2 = 0; i2 < length - 1; i2++) {
						if (Math.abs(vertices[edges[i][1]][i2]) > 1) {
							inF = false;
							break;
						}
					}
					if (inF) {
						fix.add(i);
						am++;
					}
				} else if (vertices[edges[i][1]] == null) {
					boolean inF = true;
					for (int i2 = 0; i2 < length - 1; i2++) {
						if (Math.abs(vertices[edges[i][0]][i2]) > 1) {
							inF = false;
							break;
						}
					}
					if (inF) {
						fix.add(i);
						am++;
					}
				}
			}
			vertices = Arrays.copyOf(vertices, vertices.length + am);

			for (int i = 0; i < edges.length; i++) {
				if (fix.contains(i)) {
					if (vertices[edges[i][0]] == null) {
						vertices[currIndex] = zentralDownOne(
								achsenabschnitt(oldVertices[edges[i][0]],
										oldVertices[edges[i][1]], options),
								false, options);
						edges[i][0] = currIndex;
						extraVertices = Arrays.copyOf(extraVertices,
								currIndex + 1);
						extraVertices[currIndex] = true;
					} else if (vertices[edges[i][1]] == null) {
						vertices[currIndex] = zentralDownOne(
								achsenabschnitt(oldVertices[edges[i][0]],
										oldVertices[edges[i][1]], options),
								false, options);
						edges[i][1] = currIndex;
						extraVertices = Arrays.copyOf(extraVertices,
								currIndex + 1);
						extraVertices[currIndex] = true;
					}
					currIndex++;
				}

			}
			length--;

		}

		float[][] ret = new float[vertices.length][length];
		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i] != null) {
				ret[i][0] = (float) vertices[i][0];
				ret[i][1] = (float) vertices[i][1];

			} else {
				ret[i] = null;
			}
		}
		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i] != null) {
				if (extraVertices[i]) {
					boolean edit = true;
					for (int i2 = 0; i2 < ret[i].length; i2++) {
						float d = ret[i][i2];
						if (Math.abs(d) > 1) {
							edit = false;
							break;
						}
					}
					if (edit) {
						int i2 = 0;
						for (; i2 < edges.length; i2++) {
							if (edges[i2][0] == i || edges[i2][1] == i) {
								break;
							}
						}
						if (renderVertices) {
							System.out.println(ret[i][0] + "|" + ret[i][1]);
						}
						if (edges[i2][0] == i) {
							ret[i] = valueAt(ret[i][1] > 0 ? 2 : -2,
									ret[edges[i2][1]], ret[i]);
						} else {
							ret[i] = valueAt(ret[i][1] > 0 ? 2 : -2,
									ret[edges[i2][0]], ret[i]);
						}

					}
				}
			}
		}
		if (colors != null) {
			renderColor(ret, edges, colors, RenderAlgo.mainShaderId);
		} else {
			renderNoColor(ret, edges, RenderAlgo.mainShaderId);
		}

		// NYI
		// int[][][] sInt = new int[sides.length][3][2];
		// for (int i = 0;i<sides.length;i++) {
		// for (int i2 = 0;i2<3;i2++) {
		// sInt[i][i2][0] = (int)
		// ((ret[sides[i][i2]][0]+1)/2*Display.getWidth());
		// sInt[i][i2][1] = (int)
		// ((ret[sides[i][i2]][1]+1)/2*Display.getHeight());
		// }
		// }

	}

	private double[] zentralDownOne(double[] in, boolean clean, double[] options) {

		int dim = in.length;
		in[dim - 1] += options[0];
		boolean inFrustrum = true;
		for (int i = 0; i < dim - 1; i++) {
			in[i] = options[1] * in[i] / in[dim - 1];
			if (Math.abs(in[i]) > 1) {
				inFrustrum = false;
			}
			if (in[in.length - 1] < 0) {
				in[i] *= -1;
			}
		}
		if (in[dim - 1] <= 0 && inFrustrum && clean) {
			return null;
		}
		in = Arrays.copyOf(in, in.length - 1);
		return in;
	}

}
