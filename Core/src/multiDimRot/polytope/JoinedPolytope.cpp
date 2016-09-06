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
	// TODO Auto-generated destructor stub
}

std::vector<VecN>& JoinedPolytope::getVertices() {
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
	return retV;
}

std::vector<Edge>& JoinedPolytope::getEdges() {
	std::vector<Edge> eA = a->getEdges();
	std::vector<Edge> eB = b->getEdges();
	retE = std::vector<Edge>(eA.size()+eB.size());
	int i = 0;
	int eASize = eA.size();
	int eBSize = eB.size();
	int vASize = a->getVertices().size();
	for (;i<eASize;i++) {
		retE[i] = eA[i];
	}
	for (;i<eASize+eBSize;i++) {
		Edge tmp = eB[i-eASize];
		tmp.start+=vASize;
		tmp.end+=vASize;
		retE[i] = tmp;
	}
	return retE;
}

int JoinedPolytope::getDimensions() {
	return a->getDimensions();
}
void JoinedPolytope::update() {
	a->update();
	b->update();
}
