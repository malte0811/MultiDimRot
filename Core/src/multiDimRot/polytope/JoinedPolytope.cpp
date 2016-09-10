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

std::vector<VecN>& JoinedPolytope::getVertices() {
	return retV;
}

std::vector<Triangle>& JoinedPolytope::getFaces() {
	return retF;
}

std::vector<Edge>& JoinedPolytope::getEdges() {
	return retE;
}

int JoinedPolytope::getDimensions() {
	return a->getDimensions();
}
void JoinedPolytope::update() {
	a->update();
	b->update();
	std::vector<VecN> vA = a->getVertices();
	std::vector<VecN> vB = b->getVertices();
	retV = std::vector<VecN>(vA.size()+vB.size());
	int i = 0;
	int vASize = vA.size();
	int vBSize = vB.size();
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
		retF[i] = tmp;
	}
}
