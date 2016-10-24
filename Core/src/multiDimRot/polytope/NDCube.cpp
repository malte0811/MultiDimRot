/*******************************************************************************
 * This file is part of MultiDimRot2.0.
 * Copyright (C) 2016 malte0811
 *
 * MultiDimRot2.0 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MultiDimRot2.0 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MultiDimRot2.0.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
#include <NDCube.h>
#include <Polytope.h>
#include <Util.h>
#include <vector>
#include <iostream>

bool getBit(int num, int bitId) {
	return (num&(1<<bitId))!=0;
}
NDCube::NDCube(int dims) {
	int vCount = 1<<dims;
	int eCount = dims*(vCount>>1);
	int fCount = (dims*(dims-1))/2*(vCount>>2);
	//int nCount = (dims*(dims-1))/2;
	dimensions = dims;
	edges = std::vector<Edge>(eCount);
	vertices = std::vector<VecN>(vCount, VecN(dims));
	faces = std::vector<Triangle>(fCount*2);
	normals = std::vector<VecN>(fCount, VecN(dims));
	int edgeId = 0;
	int faceId = 0;
	for (int i = 0;i<vCount;i++) {
		//vertex
		for (int digit = 0;digit<dims;digit++) {
			bool is0 = getBit(i, digit);
			vertices[i][digit] = is0?-.5:.5;
			//edge
			if (!is0) {
				edges[edgeId].start = i;
				edges[edgeId].end = i+(1<<digit);
				edgeId++;
				for (int digit2 = digit+1;digit2<dims;digit2++) {
					if (!getBit(i, digit2)) {
						for (int digit3 = 0;digit3<dims;digit3++) {
							if (digit3!=digit&&digit3!=digit2) {
								if (getBit(i, digit3)) {
									normals[faceId/2][digit3] = -1;
								} else {
									normals[faceId/2][digit3] = 1;
								}
							}
						}
						normals[faceId/2].scaleToLength(1, false);
						util::initQuad(faces, faceId, i, i+(1<<digit), i+(1<<digit2), i+(1<<digit)+(1<<digit2), faceId/2);
					}
				}
			}
		}
	}
}

NDCube::~NDCube() {}

int NDCube::getDimensions() const {
	return dimensions;
}

const std::vector<Edge>& NDCube::getEdges() const {
	return edges;
}

const std::vector<VecN>& NDCube::getVertices() const {
	return vertices;
}
const std::vector<Triangle>& NDCube::getFaces() const {
	return faces;
}
const std::vector<VecN>& NDCube::getNormals() const {
	return normals;
}

void NDCube::update() {}
