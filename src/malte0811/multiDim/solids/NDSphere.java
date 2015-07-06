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
		for (int i = 0; i < vertices.length; i++) {
			for (int d = 0; d < dim - 1; d++) {
				int prod = prod(iter, d);
				int mod = prod(iter, d + 1);
				// i + prod < vertices.length
				// && (d == var - 1 || (i + prod) / mod == i / mod)
				if (((i + prod) / mod == i / mod && i + prod < vertices.length)
						|| d == dim - 2) {
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

	private int prod(int iter, int max) {
		int ret = 1;
		for (int i = 0; i < max; i++) {
			ret *= iter / 2 + Math.pow(iter / 2, i);
		}
		return ret;
	}
}