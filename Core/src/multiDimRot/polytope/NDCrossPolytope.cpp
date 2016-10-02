/*
 * NDCrossPolytope.cpp
 *
 *  Created on: 29.07.2016
 *      Author: malte
 */

#include <NDCrossPolytope.h>
#include <Polytope.h>
#include <vector>
#include <VecN.h>
#include <Util.h>
#include <cmath>
#include <iostream>

NDCrossPolytope::NDCrossPolytope(int dim) {
	float dist = std::sqrt(2.0)/2;
	dimensions = dim;
	vertices = std::vector<VecN>(2*dimensions, VecN(dimensions));
	edges = std::vector<Edge>(2*dim*(dim-1));
	int edgeId = 0;
	for (int i = 0;i<dimensions;i++) {
		vertices[2*i][i] = dist;
		vertices[2*i+1][i] = -dist;
		for (int j = 0;j<2*i;j++) {
			Edge e;
			e.start = j;
			e.end = 2*i;
			edges[edgeId] = e;
			e.start = j;
			e.end = 2*i+1;
			edges[edgeId+1] = e;
			edgeId+=2;
		}
	}
	int fCount = 0;
	for (int i = 2;i<dimensions;i++) {
		fCount+=4*i*(i-1);
	}
	faces = std::vector<Triangle>(fCount);
	normals = std::vector<VecN>(fCount);
	int faceId = 0;
	for (int i = 0;i<dimensions;i++) {
		for (int j = 0;j<2*i;j++) {
			for (int k = j+1;k<2*i;k++) {
				if (k/2!=j/2) {
					util::addFace(faces, normals, vertices, 2*i, j, k, faceId);
					util::addFace(faces, normals, vertices, 2*i+1, j, k, faceId);
				}
			}
		}
	}
	std::cout << fCount << ":" << faceId << "\n";
}

NDCrossPolytope::~NDCrossPolytope() {}

int NDCrossPolytope::getDimensions() const {
	return dimensions;
}

const std::vector<Edge>& NDCrossPolytope::getEdges() const {
	return edges;
}

const std::vector<VecN>& NDCrossPolytope::getVertices() const {
	return vertices;
}
const std::vector<Triangle>& NDCrossPolytope::getFaces() const {
	return faces;
}
const std::vector<VecN>& NDCrossPolytope::getNormals() const {
	return normals;
}

void NDCrossPolytope::update() {}
