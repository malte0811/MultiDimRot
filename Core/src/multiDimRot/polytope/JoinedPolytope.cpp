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



#include <multiDimRot/polytope/JoinedPolytope.h>

namespace MultiDimRot {
namespace Polytope {
JoinedPolytope::JoinedPolytope(const std::vector<Polytope*>& parts) {

}

JoinedPolytope::~JoinedPolytope() {
	if (a!=0) {
		delete a;
	}
	if (b!=0) {
		delete b;
	}
}

const std::vector<MultiDimRot::Math::VecN>& JoinedPolytope::getVertices() const {
	return retV;
}

const std::vector<Triangle>& JoinedPolytope::getFaces() const {
	return retF;
}

const std::vector<Edge>& JoinedPolytope::getEdges() const {
	return retE;
}

const std::vector<MultiDimRot::Math::VecN>& JoinedPolytope::getNormals() const {
	return retN;
}

int JoinedPolytope::getDimensions() const{
	return a->getDimensions();
}
bool JoinedPolytope::update() {

}
}
}
