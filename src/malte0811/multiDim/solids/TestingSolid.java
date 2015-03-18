package malte0811.multiDim.solids;

import java.util.Arrays;

public class TestingSolid extends Solid {
	public double[][] vertices1 = { { -1, 0, 0 }, { 0, 0, 0 }, { 0, 1, 0 } };
	public int[][] edges = { { 0, 1 }, { 0, 2 } };
	public float[][] colors = { { 0F, 0F, 1F }, { 1F, 0F, 0F } };

	public TestingSolid() {
		vertices = vertices1;
		sides = new int[1][3];
		sides[0][0] = 0;
		sides[0][1] = 1;
		sides[0][2] = 2;
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
		return colors;
	}

	@Override
	public boolean isColored() {
		return true;
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
