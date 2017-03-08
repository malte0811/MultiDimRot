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
#include <multiDimRot/Util.h>

namespace MultiDimRot {
namespace Polytope {
JoinedPolytope::JoinedPolytope(const std::vector<Polytope*>& parts) : parts(parts) {
	if (parts.size()<1) {
		throw "At least one polytope is required";
	}
	dimensions = parts[0]->getDimensions();
	for (int i = 1;i<parts.size();i++) {
		if (parts[i]->getDimensions()!=dimensions) {
			//TODO maybe make joining different dimensions possible?
			throw "Can't join polytopes of different dimension counts (yet?)";
		}
	}
	refresh();
}

JoinedPolytope::~JoinedPolytope() {
	for (Polytope* pol:parts) {
		if (pol!=0) {
			delete pol;
		}
	}
}

const std::vector<MultiDimRot::Math::VecN>& JoinedPolytope::getVertices() const {
	return joinedVertices;
}

const std::vector<Triangle>& JoinedPolytope::getFaces() const {
	return joinedFaces;
}

const std::vector<Edge>& JoinedPolytope::getEdges() const {
	return joinedEdges;
}

const std::vector<MultiDimRot::Math::VecN>& JoinedPolytope::getNormals() const {
	return joinedNormals;
}

int JoinedPolytope::getDimensions() const{
	return dimensions;
}
bool JoinedPolytope::update() {
	bool update = false;
	for (Polytope* pol:parts) {
		update |= pol->update();
	}
	if (update) {
		refresh();
	}
	return update;
}
typedef std::vector<Math::VecN> VecVec;
typedef std::vector<Edge> EdgeVec;
typedef std::vector<Triangle> FaceVec;
void JoinedPolytope::refresh() {
	std::vector<int> vertexOffsets(parts.size());
	std::vector<int> normalOffsets(parts.size());
	Util::joinVectors<Math::VecN>(parts.size(), [this](int i)->VecVec{return parts[i]->getVertices();},
			[&](const VecVec& v, int offset, int i)->VecVec{
		if (i>0) {
			vertexOffsets[i] = offset;
		}
		return v;
	}, joinedVertices);
	Util::joinVectors<Math::VecN>(parts.size(), [this](int i)->VecVec{return parts[i]->getNormals();},
			[&](const VecVec& v, int offset, int i)->VecVec{
		if (i>0) {
			normalOffsets[i] = offset;
		}
		return v;
	}, joinedNormals);
	Util::joinVectors<Edge>(parts.size(), [this](int i)->EdgeVec{return parts[i]->getEdges();},
			[vertexOffsets](const EdgeVec& v, int offset, int item)->EdgeVec{
		EdgeVec v2(v.size());
		for (int i = 0;i<v.size();i++) {
			v2[i].start = v[i].start+vertexOffsets[item];
			v2[i].end = v[i].end+vertexOffsets[item];
		}
		return v2;
	}, joinedEdges);
	Util::joinVectors<Triangle>(parts.size(), [this](int i)->FaceVec{return parts[i]->getFaces();},
			[vertexOffsets, normalOffsets](const FaceVec& v, int offset, int item)->FaceVec{
		FaceVec v2(v.size());
		for (int i = 0;i<v.size();i++) {
			v2[i] = Util::offset(v[i], vertexOffsets[item], normalOffsets[item]);
		}
		return v2;
	}, joinedFaces);
}
}
}
