package malte0811.multiDim.solids;

import java.util.Arrays;

public class NDSphere extends Solid {
	public NDSphere(int dim, int res) {
		if (dim < 1 || res <= 0) {
			System.out
					.println("The dimension and resolution has to be positive.");
			vertices = new double[0][0];
			edges = new int[0][0];
			return;
		}
		edges = new int[1][2];
		if (dim == 1) {
			edges = new int[0][2];
			vertices = new double[2][1];
			vertices[0][0] = -1;
			vertices[1][0] = 1;
			sides = new int[0][3];
			return;
		}
		final Solid oldSphere = new NDSphere(dim - 1, res);
		Solid tmp = new TMPSolid(new int[0][2], new double[0][dim],
				new int[0][3]);
		int iterations = 180 / res;
		for (int rot = 0; rot < 180; rot += res) {
			oldSphere.rotate(0, dim - 1, res);
			tmp = Solid.add(tmp, oldSphere, true);
		}
		this.vertices = tmp.getCopyOfVertices(dim);
		this.edges = tmp.getEdges();
		this.sides = tmp.getSides();
		int oldLength = oldSphere.getCopyOfVertices(0).length;
		int oldELength = edges.length;
		int oldSLength = sides.length;
		sides = Arrays.copyOf(sides, 2 * oldELength + 2 * oldELength
				/ iterations);
		int z = 0;
		// DEBUG
		// for (int i = 0; i < edges.length; i++) {
		// // DEBUG
		// if (edges[i][0] < vertices.length - oldLength) {
		// if (2 * z + oldSLength + 1 < vertices.length) {
		// sides[2 * z + oldSLength] = new int[3];
		// sides[2 * z + oldSLength + 1] = new int[3];
		// sides[2 * z + oldSLength][0] = edges[i][0];
		// sides[2 * z + oldSLength][1] = edges[i][1];
		// sides[2 * z + oldSLength][2] = edges[i][0] + oldLength;
		// sides[2 * z + oldSLength + 1][0] = edges[i][1];
		// sides[2 * z + oldSLength + 1][1] = edges[i][1] + oldLength;
		// sides[2 * z + oldSLength + 1][2] = edges[i][0] + oldLength;
		// } else {
		// sides[2 * z + oldSLength] = new int[3];
		// sides[2 * z + oldSLength + 1] = new int[3];
		// sides[2 * z + oldSLength][0] = edges[i][0];
		// sides[2 * z + oldSLength][1] = edges[i][1];
		// sides[2 * z + oldSLength][2] = (edges[i][0] + oldLength)
		// % vertices.length;
		// sides[2 * z + oldSLength + 1][0] = edges[i][1];
		// sides[2 * z + oldSLength + 1][1] = (edges[i][1] + oldLength)
		// % vertices.length;
		// sides[2 * z + oldSLength + 1][2] = (edges[i][0] + oldLength)
		// % vertices.length;
		//
		// }
		// z++;
		// }
		// }

		edges = Arrays.copyOf(edges, oldELength + vertices.length);

		for (int i = 0; i < vertices.length; i++) {
			edges[i + oldELength] = new int[2];
			edges[i + oldELength][0] = i;
			if (i + oldLength < vertices.length) {
				edges[i + oldELength][1] = i + oldLength;
			} else {
				edges[i + oldELength][1] = (i + oldLength) % vertices.length;
				// if (dim == 2) {
				// edges[i + oldELength][1] += 1;
				// edges[i + oldELength][1] %= oldLength;
				// } else if (dim == 3) {
				int d = oldLength - edges[i + oldELength][1];
				if (edges[i + oldELength][1] % 2 == 1) {
					edges[i + oldELength][1] = 2 * oldLength + d - dim + 1;
				} else {
					edges[i + oldELength][1] = 2 * oldLength + d - dim - 1;
				}
				edges[i + oldELength][1] %= oldLength;
				// }
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
			for (double[] v : vertices) {
				v = Arrays.copyOf(v, dim);
			}
		}
	}
}