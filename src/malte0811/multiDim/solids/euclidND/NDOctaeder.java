package malte0811.multiDim.solids.euclidND;

import java.util.Arrays;

import malte0811.multiDim.solids.Solid;
import malte0811.multiDim.solids.TMPSolid;

public class NDOctaeder extends Solid {
	public NDOctaeder(int d) {
		if (d == 1) {
			vertices = new double[2][1];
			edges = new int[0][2];
			vertices[0][0] = -1D;
			vertices[1][0] = 1D;
			sides = new int[0][3];
			return;
		}
		double[][] tV = new double[2][d];
		tV[0][d - 1] = -1D;
		tV[1][d - 1] = 1D;
		Solid old = new NDOctaeder(d - 1);
		Solid a = Solid.add(old, new TMPSolid(new int[0][2], tV), true);
		this.vertices = a.getCopyOfVertices(d);
		int l = this.vertices.length;
		int lE = a.getEdges().length;
		this.edges = Arrays.copyOf(a.getEdges(), lE + 2 * (l - 2));
		for (int i = 0; i < l - 2; i++) {
			this.edges[lE + i] = new int[2];
			this.edges[lE + i][0] = i;
			this.edges[lE + i][1] = l - 2;
		}
		for (int i = 0; i < l - 2; i++) {
			this.edges[lE + l - 2 + i] = new int[2];
			this.edges[lE + l - 2 + i][0] = i;
			this.edges[lE + l - 2 + i][1] = l - 1;
		}
		int oldL = old.getCopyOfVertices(0).length;
		sides = new int[2 * oldL][3];
		int sideI = 0;
		for (int i = 0; i < oldL; i++) {
			sides[sideI][0] = oldL;
			sides[sideI][1] = i;
			sides[sideI][2] = (i + 1) % oldL;
			sides[sideI + 1][0] = oldL + 1;
			sides[sideI + 1][1] = i;
			sides[sideI + 1][2] = (i + 1) % oldL;

			sideI += 2;
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
