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

#include <multiDimRot/polytope/Polytope.h>
#include <cmath>

using namespace MultiDimRot::Polytope;

Polytope::Polytope() {}

Polytope::~Polytope() {}

float Polytope::getLength(Edge e) {
	const std::vector<Math::VecN>& vertices = getVertices();
	float sum = 0;
	Math::VecN a = vertices[e.start];
	Math::VecN b = vertices[e.end];
	for (int i = 0;i<getDimensions();i++) {
		sum += (a[i]-b[i])*(a[i]-b[i]);
	}
	return std::sqrt(sum);
}

const std::vector<MultiDimRot::Math::VecN>& Polytope::getVertices() const {
	return vertices;
}

const std::vector<MultiDimRot::Math::VecN>& Polytope::getNormals() const {
	return normals;
}

const std::vector<Edge>& Polytope::getEdges() const {
	return edges;
}

const std::vector<Triangle>& Polytope::getFaces() const {
	return faces;
}

int Polytope::getDimensions() const {
	return dimensions;
}

void Polytope::update() {}
