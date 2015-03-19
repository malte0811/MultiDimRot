package malte0811.multiDim.commands;

import java.util.Arrays;
import java.util.HashSet;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.render.ParallelRender;
import malte0811.multiDim.solids.Solid;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class CommandTmp extends Command {

	@Override
	public String getCommandName() {
		return "TMP";
	}

	@Override
	public String getCommandUsage() {
		return "This commands effect and usage will change over time. it is only available in the dev-branch";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		Solid hc = DimRegistry.getCalcThread().solid;
		render(hc.getCopyOfVertices(4), hc.getEdges(),
				DimRegistry.getCalcThread().renderOptions, false, null,
				hc.getSides(), Mouse.getX(), Mouse.getY());
	}

	// From rendering
	public void render(double[][] vertices, int[][] edges, double[] options,
			boolean renderVertices, float[][] colors, int[][] sides,
			int checkX, int checkY) {
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
		int[][][] sInt = new int[sides.length][3][2];
		for (int i = 0; i < sides.length; i++) {
			for (int i2 = 0; i2 < 3; i2++) {
				if (ret[sides[i][i2]] == null) {
					sides[i] = null;
					break;
				}
				sInt[i][i2][0] = (int) ((ret[sides[i][i2]][0] + 1) / 2 * Display
						.getWidth());
				sInt[i][i2][1] = (int) ((ret[sides[i][i2]][1] + 1) / 2 * Display
						.getHeight());
			}
		}
		System.out.println("density at x:" + checkX + " y:" + checkY + ": "
				+ new ParallelRender().getDensity(sInt)[checkX][checkY]);
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

	protected double[] achsenabschnitt(double[] v1, double[] v2,
			double[] options) {
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

	protected double achsenabschnitt(double x1, double x2, double y1, double y2) {
		double steigung = (y2 - y1) / (x2 - x1);

		double d = y2 - steigung * x2;
		return d;
	}

	protected float[] valueAt(float x, float[] ret2, float[] ret3) {
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

	protected float valueAt(float f, float x1, float y1, float x2, float y2) {
		float steig = (y2 - y1) / (x2 - x1);
		return (float) (steig * f + achsenabschnitt(x1, x2, y1, y2));
	}
}
