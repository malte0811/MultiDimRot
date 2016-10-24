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
#include <JoinedPolytope.h>
#include <vector>

JoinedPolytope::JoinedPolytope(Polytope* p0, Polytope* p1) {
	if (p0->getDimensions()!=p1->getDimensions()) {
		throw "Can't join polytopes with different dimension count!";
	}
	a = p0;
	b = p1;
}

JoinedPolytope::~JoinedPolytope() {
	if (a!=0) {
		delete a;
	}
	if (b!=0) {
		delete b;
	}
}

const std::vector<VecN>& JoinedPolytope::getVertices() const {
	return retV;
}

const std::vector<Triangle>& JoinedPolytope::getFaces() const {
	return retF;
}

const std::vector<Edge>& JoinedPolytope::getEdges() const {
	return retE;
}

const std::vector<VecN>& JoinedPolytope::getNormals() const {
	return retN;
}

int JoinedPolytope::getDimensions() const{
	return a->getDimensions();
}
void JoinedPolytope::update() {
	a->update();
	b->update();
	std::vector<VecN> vA = a->getVertices();
	std::vector<VecN> vB = b->getVertices();
	int vASize = vA.size();
	int vBSize = vB.size();
	retV = std::vector<VecN>(vASize+vBSize);
	int i = 0;
	for (;i<vASize;i++) {
		retV[i] = vA[i];
	}
	for (;i<vASize+vBSize;i++) {
		retV[i] = vB[i-vASize];
	}
	std::vector<Edge> eA = a->getEdges();
	std::vector<Edge> eB = b->getEdges();
	retE = std::vector<Edge>(eA.size()+eB.size());
	i = 0;
	int eASize = eA.size();
	int eBSize = eB.size();
	for (;i<eASize;i++) {
		retE[i] = eA[i];
	}
	for (;i<eASize+eBSize;i++) {
		Edge tmp = eB[i-eASize];
		tmp.start+=vASize;
		tmp.end+=vASize;
		retE[i] = tmp;
	}
	std::vector<VecN> nA = a->getNormals();
	std::vector<VecN> nB = b->getNormals();
	int nASize = nA.size();
	int nBSize = nB.size();
	retN = std::vector<VecN>(nASize+nB.size());
	i = 0;
	for (;i<nASize;i++) {
		retN[i] = nA[i];
	}
	for (;i<nBSize;i++) {
		retN[i+nASize] = nB[i];
	}


	std::vector<Triangle> fA = a->getFaces();
	std::vector<Triangle> fB = b->getFaces();
	retF = std::vector<Triangle>(fA.size()+fB.size());
	i = 0;
	int fASize = fA.size();
	int fBSize = fB.size();
	for (;i<eASize;i++) {
		retF[i] = fA[i];
	}
	for (;i<fASize+fBSize;i++) {
		Triangle tmp = fB[i-fASize];
		tmp.vertices[0]+=vASize;
		tmp.vertices[1]+=vASize;
		tmp.vertices[2]+=vASize;
		tmp.normals[0]+=nASize;
		tmp.normals[1]+=nASize;
		tmp.normals[2]+=nASize;
		retF[i] = tmp;
	}
}
