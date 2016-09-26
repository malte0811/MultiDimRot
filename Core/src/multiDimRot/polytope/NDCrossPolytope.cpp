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
	vertices = std::vector<VecN>(2*dimensions);
	edges = std::vector<Edge>(2*dim*(dim-1));
	int edgeId = 0;
	for (int i = 0;i<dimensions;i++) {
		VecN v(dim);
		v.setElement(i, dist);
		vertices[2*i] = v;
		v.setElement(i, -dist);
		vertices[2*i+1] = v;
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

int NDCrossPolytope::getDimensions() {
	return dimensions;
}

std::vector<Edge>& NDCrossPolytope::getEdges() {
	return edges;
}

std::vector<VecN>& NDCrossPolytope::getVertices() {
	return vertices;
}
std::vector<Triangle>& NDCrossPolytope::getFaces() {
	return faces;
}
std::vector<VecN>& NDCrossPolytope::getNormals() {
	return normals;
}

void NDCrossPolytope::update() {}
