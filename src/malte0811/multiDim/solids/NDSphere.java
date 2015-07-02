package malte0811.multiDim.solids;

import java.util.Arrays;

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
		NDSphere old = new NDSphere(dim - 1, res);
		old.vertices = Arrays.copyOf(old.getVertices(),
				old.getVertices().length / 2 + (dim == 2 ? 0 : 1));
		old.edges = Arrays.copyOf(old.getEdges(), old.getEdges().length / 2);
		old.setMinDim(dim);
		int iter = (int) (360 / res);
		int oldVLength = old.vertices.length;
		int oldELength = old.edges.length;
		vertices = new double[oldVLength * iter][dim];
		edges = new int[oldELength * iter
				+ (int) (Math.pow(iter - 1, dim - 1) * iter)][2];
		for (int i = 0; i * res < 360; i++) {
			for (int i2 = 0; i2 < oldVLength; i2++) {
				vertices[i2 + i * oldVLength] = Arrays.copyOf(old.vertices[i2],
						dim);
			}
			old.rotate(dim - 2, dim - 1, res);
		}
		int edgeId = 0;
		for (int i2 = 0; i2 < dim - 1; i2++) {
			for (int i = 0; i < vertices.length; i++) {
				int to = (int) (i + Math.pow(iter / 2, i2 - 1)
						* (double) (iter / 2 + 1));
				if (i2 == dim - 2 || to < vertices.length) {
					edges[edgeId][0] = i;
					edges[edgeId][1] = to % vertices.length;
					edgeId++;
				}

			}
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
}