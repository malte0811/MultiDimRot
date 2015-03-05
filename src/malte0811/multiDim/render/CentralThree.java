package malte0811.multiDim.render;

import java.util.Arrays;

import malte0811.multiDim.tickHandlers.DebugHandler;

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

			// if (length == 3) {
			// length--;
			// continue;
			// }

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
						int oldLength = vertices.length;
						vertices = Arrays.copyOf(vertices, oldLength + 1);
						vertices[oldLength] = zentralDownOne(
								achsenabschnitt(oldVertices[edges[i][0]],
										oldVertices[edges[i][1]], options),
								false, options);
						edges[i][0] = oldLength;
						extraVertices = Arrays.copyOf(extraVertices,
								oldLength + 1);
						extraVertices[oldLength] = true;
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
						int oldLength = vertices.length;
						vertices = Arrays.copyOf(vertices, oldLength + 1);
						vertices[oldLength] = zentralDownOne(
								achsenabschnitt(oldVertices[edges[i][0]],
										oldVertices[edges[i][1]], options),
								false, options);
						edges[i][1] = oldLength;
						extraVertices = Arrays.copyOf(extraVertices,
								oldLength + 1);
						extraVertices[oldLength] = true;
						am++;
					}
				}

			}
			if (length == 3) {
				DebugHandler.getInstance().addTime(1, am);
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

	private double[] achsenabschnitt(double[] v1, double[] v2, double[] options) {
		double[] ret = new double[v1.length];
		for (int i = 0; i < v1.length - 1; i++) {
			ret[i] = achsenabschnitt(v1[v1.length - 1], v2[v2.length - 1],
					v1[i], v2[i]);
		}
		if (options != null) {
			ret[v1.length - 1] = Math.abs(options[0]) < 0.001 ? 0.001
					: options[0];
		}

		return ret;
	}

	private double achsenabschnitt(double x1, double x2, double y1, double y2) {
		double steigung = (y2 - y1) / (x2 - x1);

		double d = y2 - steigung * x2;
		return d;
	}

	private float[] valueAt(float x, float[] ret2, float[] ret3) {
		float[] ret = new float[ret2.length];
		double[] s1D = new double[ret2.length];
		double[] s2D = new double[ret2.length];
		for (int i = 0; i < s1D.length; i++) {
			s1D[i] = ret2[i];
			s2D[i] = ret3[i];
		}
		double[] achse = achsenabschnitt(s1D, s2D, null);
		for (int i = 0; i < ret2.length - 1; i++) {
			double steigung = (s1D[i] - s2D[i])
					/ (s1D[s1D.length - 1] - s2D[ret3.length - 1]);
			ret[i] = (int) (achse[i] + steigung * (double) x);
		}
		ret[ret.length - 1] = x;
		return ret;
	}
}
