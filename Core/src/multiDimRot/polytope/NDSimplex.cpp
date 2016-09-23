/*
 * NDSimplex.cpp
 *
 *  Created on: 29.07.2016
 *      Author: malte
 */

#include <Polytope.h>
#include "NDSimplex.h"
#include <vector>
#include <VecN.h>
#include <cmath>
#include <iostream>

NDSimplex::NDSimplex(int dim) {
	dimensions = dim;
	vertices = std::vector<VecN>(dimensions+1);
	edges = std::vector<Edge>((dim*(dim+1))/2);
	std::vector<float> heights(dim, 0);
	heights[0] = 1;
	for (int i = 1;i<dim;i++) {
		float tmp = heights[i-1]*i/(i+1);
		heights[i] = std::sqrt(1-tmp*tmp);
	}
	for (int i = 0;i<dimensions+1;i++) {
		VecN next(dimensions);
		for (int j = 0;j<dimensions;j++) {
			if (i<=j) {
				next.setElement(j, -heights[j]/(j+2));
			} else if (i==j+1) {
				next.setElement(j, heights[j]*(j+1)/(j+2));
			}
		}
		vertices[i] = next;
	}
	int eId = 0;
	for (int i = 0;i<dimensions;i++) {
		for (int j = i+1;j<dimensions+1;j++) {
			edges[eId].start = i;
			edges[eId].end = j;
			eId++;
		}
	}
}

NDSimplex::~NDSimplex() {}

int NDSimplex::getDimensions() {
	return dimensions;
}

std::vector<Edge>& NDSimplex::getEdges() {
	return edges;
}

std::vector<VecN>& NDSimplex::getVertices() {
	return vertices;
}
std::vector<Triangle>& NDSimplex::getFaces() {
	return faces;
}
std::vector<VecN>& NDSimplex::getNormals() {
	return normals;
}

void NDSimplex::update() {}
