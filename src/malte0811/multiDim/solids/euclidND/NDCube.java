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
		int ed = 0;
		for (int i = 0; i < d; i++) {
			ed *= 2;
			ed += Math.pow(2, i);
		}
		edges = new int[ed][2];

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
	}

	public void genNDCubeold(int d) {
		edges = new int[1][2];
		edges[0][0] = 0;
		edges[0][0] = 1;

		double[][] vtmp = { { -1 }, { 1 } };

		for (int i = 1; i < d; i++) {
			double[][] tmp = new double[2 * vtmp.length][i + 1];
			for (int i2 = 0; i2 < vtmp.length; i2++) {
				for (int i3 = 0; i3 < i; i3++) {
					tmp[i2][i3] = vtmp[i2][i3];
					tmp[i2 + vtmp.length][i3] = vtmp[i2][i3];
				}
				tmp[i2][i] = -1;
				tmp[i2 + vtmp.length][i] = 1;
			}
			// edges
			int[][] nedges = new int[2 * edges.length + tmp.length / 2][2];
			for (int i2 = 0; i2 < edges.length; i2++) {
				nedges[i2] = edges[i2];
				nedges[i2 + edges.length][0] = edges[i2][0] + vtmp.length;
				nedges[i2 + edges.length][1] = edges[i2][1] + vtmp.length;
			}
			for (int i2 = 0; i2 < vtmp.length; i2++) {
				nedges[2 * edges.length + i2][0] = i2;
				nedges[2 * edges.length + i2][1] = vtmp.length + i2;
			}
			edges = nedges;
			vtmp = tmp;
		}
		vertices = vtmp;
		for (double[] i : vertices) {
			String coords = "" + i[0];
			for (double i2 : i) {
				coords += "|" + i2;
			}
			System.out.println(coords);
		}
		for (int[] i : edges) {
			System.out.println(i[0] + "|" + i[1]);
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
