/*
 * NDCrossPolytope.cpp
 *
 *  Created on: 29.07.2016
 *      Author: malte
 */

#include <NDCrossPolytope.h>
#include <vector>
#include <VecN.h>
#include <Solid.h>

NDCrossPolytope::NDCrossPolytope(int dim) {
	dimensions = dim;
	vertices = std::vector<VecN>(2*dimensions);
	edges = std::vector<Edge>(2*dim*(dim-1));
	int edgeId = 0;
	for (int i = 0;i<dimensions;i++) {
		VecN v(dim);
		v.setElement(i, 1);
		vertices[2*i] = v;
		v.setElement(i, -1);
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

void NDCrossPolytope::update() {}
