package malte0811.multiDim.solids;

import java.util.Arrays;
import java.util.HashMap;

public class NDSphere extends Solid {

	public NDSphere(int dim, double res) {
		if (dim < 1 || res <= 0) {
			System.out
					.println("The dimension and resolution has to be positive.");
			vertices = new double[0][0];
			edges = new int[0][0];
			return;
		}
		if (dim == 1) {
			edges = new int[0][2];
			vertices = new double[2][1];
			vertices[0][0] = -1;
			vertices[1][0] = 1;
			sides = new int[0][3];
			return;
		}
		int iter = (int) (360 / res);
		NDSphere old = new NDSphere(dim - 1, res);
		old.vertices = Arrays.copyOf(
				old.getVertices(),
				old.getVertices().length / 2
						+ (int) Math.pow(iter / 2, dim - 3));
		old.setMinDim(dim);
		int oldVLength = old.vertices.length;
		vertices = new double[oldVLength * iter][dim];

		edges = new int[edgeCount(iter, dim - 1, dim - 1, true)][2];

		for (int i = 0; i * res < 360; i++) {
			for (int i2 = 0; i2 < oldVLength; i2++) {
				vertices[i2 + i * oldVLength] = Arrays.copyOf(old.vertices[i2],
						dim);
			}
			old.rotate(dim - 2, dim - 1, res);
		}
		int edgeId = 0;
		for (int d = 0; d < dim - 1; d++) {
			int prod = verticesForDimension(d + 1, iter);
			for (int i = 0; i < vertices.length; i++) {
				int[] a = gridPos(dim - 1, i, iter);
				int[] b = gridPos(dim - 1, i + prod, iter);
				// DEBUG
				if (dim > 4) {
					System.out.println("a:");
					for (int j : a) {
						System.out.print(j + " ");
					}
					System.out.println();
					System.out.println("b:");
					for (int j : b) {
						System.out.print(j + " ");
					}
					System.out.println();
					System.out.println();
				}
				if (isEqual(a, b, d) && a[d] + 1 == b[d] || d == dim - 2) {
					edges[edgeId][0] = i;
					edges[edgeId][1] = (i + prod) % vertices.length;
					edgeId++;
				}
			}
		}
		// DEBUG
		System.out.println("Dimensions: " + dim);
		System.out.println("Vertices: " + old.vertices.length);
		HashMap<Integer, Integer> dif = new HashMap<>();
		for (int[] e : edges) {
			int d = e[1] - e[0];
			if (dif.containsKey(d)) {
				dif.put(d, dif.get(d) + 1);
			} else {
				dif.put(d, 1);
			}
		}
		for (int d : dif.keySet()) {
			System.out.println("Difference " + d + " found: " + dif.get(d)
					+ " times");
		}
	}

	@Override
	public double[][] getCopyOfVertices(int minDim) {
		double[][] newVertices = new double[vertices.length][vertices[0].length];
		if (minDim < vertices[0].length) {
			minDim = vertices[0].length;
		}
		for (int i = 0; i < vertices.length; i++) {
			newVertices[i] = Arrays.copyOf(vertices[i], minDim);
		}
		return newVertices;
	}

	@Override
	public int[][] getEdges() {
		return edges;
	}

	@Override
	public float[][] getColors() {
		return null;
	}

	@Override
	public boolean isColored() {
		return false;
	}

	@Override
	public void setMinDim(int dim) {
		if (vertices[0].length < dim) {
			for (int i = 0; i < vertices.length; i++) {
				vertices[i] = Arrays.copyOf(vertices[i], dim);
			}
		}
	}

	private int edgeCount(int iter, int dim, int oldDim, boolean first) {
		if (iter <= 0 || dim <= 0) {
			return 0;
		}
		if (first) {
			return iter * (int) Math.pow(iter / 2, dim - 1)
					+ edgeCount(iter, dim - 1, dim, false);
		} else {
			return iter * iter * (int) Math.pow(iter / 2, oldDim - 2)
					+ edgeCount(iter, dim - 1, oldDim, false);
		}
	}

	private int verticesForDimension(int d, int iter) {
		if (d <= 1) {
			return 1;
		}
		return verticesForDimension(d - 1, iter) * iter / 2
				+ (int) Math.pow(iter / 2, d - 2);
	}

	private int[] gridPos(int d, int pos, int iter) {
		int[] ret = new int[d];
		for (int i = 0; i < d; i++) {
			int v = verticesForDimension(i + 1, iter);
			int v1 = verticesForDimension(i + 2, iter);
			ret[i] = (pos % v1) / v;
		}
		return ret;
	}

	private boolean isEqual(int[] a, int[] b, int exclude) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i] && i != exclude) {
				return false;
			}
		}
		return true;
	}
}