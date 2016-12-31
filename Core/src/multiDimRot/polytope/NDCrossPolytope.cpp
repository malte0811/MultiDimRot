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

#include <NDCrossPolytope.h>
#include <Util.h>
#include <VecN.h>
#include <cmath>
#include <iostream>

using namespace MultiDimRot;

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
					Util::addFace(faces, normals, vertices, 2*i, j, k, faceId);
					Util::addFace(faces, normals, vertices, 2*i+1, j, k, faceId);
				}
			}
		}
	}
	std::cout << fCount << ":" << faceId << "\n";
}

NDCrossPolytope::~NDCrossPolytope() {}
