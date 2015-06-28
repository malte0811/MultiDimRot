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
		Solid oldSphere = new NDSphere(dim - 1, res);
		double[][] backup = oldSphere.getCopyOfVertices(0);
		System.arraycopy(oldSphere.vertices, 0, oldSphere.vertices, 0,
				oldSphere.vertices.length / 2);
		if (oldSphere.vertices.length > 2) {
			oldSphere.vertices = Arrays.copyOf(oldSphere.vertices,
					backup.length / 2 + 1);
		} else {
			oldSphere.vertices = Arrays.copyOf(oldSphere.vertices,
					backup.length / 2);
		}
		if (oldSphere.edges.length > 0) {
			System.arraycopy(oldSphere.edges, 0, oldSphere.edges, 0,
					oldSphere.edges.length / 2);
			oldSphere.edges = Arrays.copyOf(oldSphere.edges,
					oldSphere.edges.length / 2);
		}
		oldSphere.sides = Arrays.copyOf(oldSphere.getSides(),
				oldSphere.getSides().length / 2);

		Solid tmp = new TMPSolid(new int[0][2], new double[0][dim],
				new int[0][3]);
		int iterations = 180 / res;
		// DEBUG
		for (int rot = 0; rot < 360; rot += res) {
			tmp = Solid.add(tmp, oldSphere, true);
			// if (dim == 2)
			oldSphere.rotate(dim - 2, dim - 1, res);
		}
		this.vertices = tmp.getCopyOfVertices(dim);
		// this.edges = tmp.getEdges();
		this.sides = tmp.getSides();
		int oldLength = oldSphere.getCopyOfVertices(0).length;
		int oldELength = edges.length;
		int oldSLength = sides.length;
		sides = Arrays.copyOf(sides, 2 * oldELength + 2 * oldELength
				/ iterations);
		int z = 0;
		// DEBUG sanity check
		for (int i = 0; i < edges.length; i++) {
			int[] s = edges[i];
			if (s == null || s.length != 2 || s[0] >= vertices.length
					|| s[1] >= vertices.length) {
				System.out.print("Erroring edge: " + i);
				if (s != null && s.length > 1) {
					System.out.print(" " + s[0] + "->" + s[1]);
				}
				System.out.println();
			}
		}
		edges = new int[dim * vertices.length][2];
		int eId = 0;
		for (int i = 0; i < vertices.length; i++) {
			for (int d = 0; d < dim - 1; d++) {
				edges[eId][0] = i;
				edges[eId][1] = (int) (i + Math.pow(iterations + 1, d))
						% vertices.length;
				eId++;
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