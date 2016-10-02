#include <Polytope.h>
#include <NDSimplex.h>
#include <Util.h>
#include <vector>
#include <VecN.h>
#include <cmath>
#include <iostream>

NDSimplex::NDSimplex(int dim) {
	dimensions = dim;
	vertices = std::vector<VecN>(dimensions+1, VecN(dim));
	edges = std::vector<Edge>((dim*(dim+1))/2);
	int fCount = ((dim-1)*dim*(dim+1))/2;
	faces = std::vector<Triangle>(fCount);
	normals = std::vector<VecN>(fCount, VecN(dim));
	std::vector<float> heights(dim, 0);
	heights[0] = 1;
	for (int i = 1;i<dim;i++) {
		float tmp = heights[i-1]*i/(i+1);
		heights[i] = std::sqrt(1-tmp*tmp);
	}
	for (int i = 0;i<dimensions+1;i++) {
		for (int j = 0;j<dimensions;j++) {
			if (i<=j) {
				vertices[i][j] = -heights[j]/(j+2);
			} else if (i==j+1) {
				vertices[i][j] = heights[j]*(j+1)/(j+2);
			}
		}
	}
	int eId = 0;
	int fId = 0;
	for (int i = 0;i<dimensions;i++) {
		for (int j = i+1;j<dimensions+1;j++) {
			edges[eId].start = i;
			edges[eId].end = j;
			eId++;
			for (int k = j+1;k<dimensions+1;k++) {
				util::addFace(faces, normals, vertices, i, j, k, fId);
			}
		}
	}

}

NDSimplex::~NDSimplex() {}

int NDSimplex::getDimensions() const {
	return dimensions;
}

const std::vector<Edge>& NDSimplex::getEdges() const {
	return edges;
}

const std::vector<VecN>& NDSimplex::getVertices() const {
	return vertices;
}
const std::vector<Triangle>& NDSimplex::getFaces() const {
	return faces;
}
const std::vector<VecN>& NDSimplex::getNormals() const {
	return normals;
}

void NDSimplex::update() {}
