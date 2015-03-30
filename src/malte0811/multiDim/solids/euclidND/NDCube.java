package malte0811.multiDim.solids.euclidND;

import java.util.Arrays;

import malte0811.multiDim.solids.Solid;

public class NDCube extends Solid {
	public NDCube(int d) {
		vertices = new double[(int) Math.pow(2, d)][d];
		boolean[] vertex = new boolean[d];
		int ind = 0;
		// add vertices
		do {
			addVertex(vertex, ind);
			ind = ind + 1;
		} while (!increment(vertex));
		// init edges
		int[] ed = new int[d + 1];
		for (int i = 1; i < d + 1; i++) {
			ed[i] = 2 * ed[i - 1];
			ed[i] = (int) (ed[i] + Math.pow(2, i - 1));
		}
		int edM = ed[d];
		edges = new int[edM][2];

		// add edges
		vertex = new boolean[d];
		ind = 0;
		int vID = 0;
		do {
			for (int i = d - 1; i >= 0; i--) {
				int stelle = d - i - 1;
				if (!vertex[i]) {
					edges[ind][0] = vID;
					edges[ind][1] = ((int) (vID + Math.pow(2, stelle)))
							% vertices.length;
					ind++;
				}
			}
			vID++;
		} while (!increment(vertex));
		// add sides
		int sd = 0;
		for (int i = 0; i < d; i++) {
			sd *= 2;
			sd += ed[i + 1];
		}
		sides = new int[sd][3];
		int index = 0;
		int l = vertices.length;
		vertex = new boolean[d];
		do {
			for (int i1 = 0; i1 < d; i1++) {
				for (int i2 = i1 + 1; i2 < d; i2++) {
					int i = getValue(vertex);
					int ind1 = d - i1 - 1;
					int ind2 = d - i2 - 1;
					if (vertex[ind1] && vertex[ind2]) {
						sides[index][0] = i;
						sides[index][1] = (int) (i + (vertex[ind1] ? -1 : 1)
								* Math.pow(2, i1))
								% l;
						sides[index][2] = (int) (i + (vertex[ind2] ? -1 : 1)
								* Math.pow(2, i2))
								% l;

						sides[index + 1][0] = (int) (i
								+ (vertex[ind1] ? -1 : 1) * Math.pow(2, i1) + (vertex[ind2] ? -1
								: 1)
								* Math.pow(2, i2))
								% l;
						sides[index + 1][1] = (int) (i + (vertex[ind1] ? -1 : 1)
								* Math.pow(2, i1))
								% l;
						sides[index + 1][2] = (int) (i + (vertex[ind2] ? -1 : 1)
								* Math.pow(2, i2))
								% l;
						index += 2;
					}
				}
			}
		} while (!increment(vertex));
		System.out.print("");
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

	private boolean increment(boolean[] number) {
		for (int i = number.length - 1; i >= 0; i--) {
			if (number[i]) {
				number[i] = false;
			} else {
				number[i] = true;
				return false;
			}
		}
		return true;
	}

	private void addVertex(boolean[] id, int vID) {
		for (int i = 0; i < id.length; i++) {
			vertices[vID][i] = id[i] ? 1 : -1;
		}
	}

	private int getValue(boolean[] num) {
		int l = num.length;
		int ret = 0;
		for (int i = l - 1; i >= 0; i--) {
			int stelle = l - i - 1;
			if (num[i]) {
				ret += Math.pow(2, stelle);
			}
		}
		return ret;
	}
}
