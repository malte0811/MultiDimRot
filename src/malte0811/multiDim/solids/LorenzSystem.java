package malte0811.multiDim.solids;

import java.util.Arrays;

public class LorenzSystem extends Solid {

	public LorenzSystem(double x, double y, double z, double a, double b,
			double c, double res, int steps) {
		vertices = new double[steps][3];
		edges = new int[steps - 1][2];
		if (res <= 0) {
			System.out.println("can't use a step size lower or equal to 0");
			return;
		}
		for (int i = 0; i < steps; i++) {
			double dX = b * (y - x);
			double dY = x * (a - z) - y;
			double dZ = x * y - c * z;
			x += dX * res;
			y += dY * res;
			z += dZ * res;
			vertices[i][0] = x;
			vertices[i][1] = y;
			vertices[i][2] = z;
			if (i != steps - 1) {
				edges[i][0] = i;
				edges[i][1] = i + 1;
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
