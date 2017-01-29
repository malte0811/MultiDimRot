/*******************************************************************************
 * This file is part of MultiDimRot2.0.
 * Copyright (C) 2016, 2017 malte0811
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

#include <multiDimRot/math/VecN.h>
#include <multiDimRot/polytope/NDSimplex.h>
#include <multiDimRot/Util.h>
#include <cmath>
#include <vector>

using namespace MultiDimRot::Polytope;

NDSimplex::NDSimplex(int dim) {
	dimensions = dim;
	vertices = std::vector<Math::VecN>(dimensions+1, Math::VecN(dim));
	edges = std::vector<Edge>((dim*(dim+1))/2);
	int fCount = ((dim-1)*dim*(dim+1))/2;
	faces = std::vector<Triangle>(fCount);
	normals = std::vector<Math::VecN>(fCount, Math::VecN(dim));
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
				Util::addFace(faces, normals, vertices, i, j, k, fId);
			}
		}
	}

}

NDSimplex::~NDSimplex() {}
