/*
 * NBCube.cpp
 *
 *  Created on: 25.07.2016
 *      Author: malte
 */

#include <NDCube.h>
#include <Solid.h>
#include <vector>
#include <iostream>

Solid::Solid() {}

Solid::~Solid() {}

NDCube::NDCube(int dims) {
	int vCount = 1<<dims;
	int eCount = dims*(vCount>>1);
	dimensions = dims;
	edges = std::vector<Edge>(eCount);
	vertices = std::vector<VecN>(vCount);
	int edgeId = 0;
	for (int i = 0;i<vCount;i++) {
		//vertex
		VecN next(dims);
		for (int digit = 0;digit<dims;digit++) {
			int tmp = i&(1<<digit);
			next.setElement(digit, tmp>0?1:-1);
			//edge
			if (tmp==0) {
				edges[edgeId].start = i;
				edges[edgeId].end = i+(1<<digit);
				edgeId++;
			}
		}
		vertices[i] = next;
	}
}

NDCube::~NDCube() {}

int NDCube::getDimensions() {
	return dimensions;
}

std::vector<Edge>& NDCube::getEdges() {
	return edges;
}

std::vector<VecN>& NDCube::getVertices() {
	return vertices;
}

void NDCube::update() {}
