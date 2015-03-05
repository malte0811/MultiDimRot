package malte0811.multiDim.solids;

import java.util.Arrays;

public class NDSphere extends Solid {
	public NDSphere(int dim, int res) {
		edges = new int[1][2];
		if (dim == 1) {
			Solid tmp = new TMPSolid(new int[0][2], new double[][] { { 1 },
					{ -1 } });
			edges = tmp.getEdges();
			vertices = tmp.getCopyOfVertices(dim);
			return;
		}
		final Solid oldSphere = new NDSphere(dim - 1, res);
		Solid tmp = new TMPSolid(new int[0][2], new double[0][dim]);
		for (int rot = 0; rot < 360; rot += res) {
			oldSphere.rotate(0, dim - 1, res);
			tmp = Solid.add(tmp, oldSphere, true);
		}
		this.vertices = tmp.getCopyOfVertices(dim);
		this.edges = tmp.getEdges();
		int oldLength = oldSphere.getCopyOfVertices(0).length;
		int oldELength = edges.length;
		edges = Arrays.copyOf(edges, edges.length + vertices.length);
		for (int i = 0; i < vertices.length; i++) {
			edges[i + oldELength] = new int[2];
			edges[i + oldELength][0] = i;
			edges[i + oldELength][1] = (i + oldLength) % vertices.length;
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
