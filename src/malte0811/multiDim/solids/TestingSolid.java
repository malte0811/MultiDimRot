package malte0811.multiDim.solids;

import java.util.Arrays;

public class TestingSolid extends Solid {
	public double[][] vertices1 = { { -1, -1, -1, -1 },
			{ -1, -1, -1, 1 },
			{ -1, -1, 1, -1 },
			{ -1, -1, 1, 1 }, // -1113
			{ -1, 1, -1, -1 }, { -1, 1, -1, 1 },
			{ -1, 1, 1, -1 },
			{ -1, 1, 1, 1 }, // 4567
			{ 1, -1, -1, -1 }, { 1, -1, -1, 1 }, { 1, -1, 1, -1 },
			{ 1, -1, 1, 1 },// 89AB
			{ 1, 1, -1, -1 }, { 1, 1, -1, 1 }, { 1, 1, 1, -1 }, { 1, 1, 1, 1 } // CDEF
	};
	public int[][] edges = { { 0, 4 }, /* { 4, 12 }, { 8, 12 }, { 8, 0 }, */
	{ 0, 2 }, { 4, 6 }, /* { 8, 10 }, { 12, 14 }, */{ 2, 6 }, /*
															 * { 6, 14 }, { 10,
															 * 14 }, { 10, 2 },
															 */
	{ 1, 5 },/* { 5, 13 }, { 9, 13 }, { 9, 1 }, */{ 1, 3 }, { 5, 7 },
	/* { 9, 11 }, { 13, 15 }, */{ 3, 7 }, /* { 7, 15 }, { 11, 15 }, { 11, 3 }, */
	{ 0, 1 }, { 2, 3 }, { 4, 5 }, { 6, 7 }, /*
											 * { 8, 9 }, { 10, 11 }, { 12, 13 },
											 * { 14, 15 }
											 */};
	public float[][] colors = { { 0F, 0F, 1F }, { 1F, 0F, 0F }, { 0F, 0F, 1F },
			{ 1F, 0F, 0F }, { 1F, 1F, 1F }, { 1F, 1F, 1F }, { 1F, 1F, 1F },
			{ 1F, 1F, 1F }, { 0F, 0F, 1F }, { 1F, 0F, 0F }, { 0F, 0F, 1F },
			{ 1F, 0F, 0F }, { 0F, 0F, 1F }, { 1F, 0F, 0F }, { 0F, 0F, 1F },
			{ 1F, 0F, 0F }, { 1F, 1F, 1F }, { 1F, 1F, 1F }, { 1F, 1F, 1F },
			{ 1F, 1F, 1F }, { 0F, 0F, 1F }, { 1F, 0F, 0F }, { 0F, 0F, 1F },
			{ 1F, 0F, 0F }, { 0F, 1F, 0F }, { 0F, 1F, 0F }, { 0F, 1F, 0F },
			{ 0F, 1F, 0F }, { 0F, 1F, 0F }, { 0F, 1F, 0F }, { 0F, 1F, 0F },
			{ 0F, 1F, 0F } };
	int[][] sides1 = { { 0, 1, 2 }, { 1, 2, 3 }, { 0, 1, 4 }, { 1, 4, 5 },
			{ 2, 3, 6 }, { 3, 6, 7 }, { 1, 3, 5 }, { 3, 5, 7 }, { 0, 2, 4 },
			{ 2, 4, 6 }, /* { 4, 5, 6 }, { 5, 6, 7 } */};

	public TestingSolid() {
		vertices = vertices1;
		sides = sides1;
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
