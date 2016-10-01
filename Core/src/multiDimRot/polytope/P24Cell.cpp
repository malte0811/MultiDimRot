#include <P24Cell.h>
#include <vector>
#include <iostream>
P24Cell::P24Cell() {
	vertices = std::vector<VecN>(24, VecN(4));
	edges = std::vector<Edge>(96);
	int edgeId = 0;
	for (int i = 0;i<16;i++) {
		for (int digit = 0;digit<4;digit++) {
			int tmp = i&(1<<digit);
			vertices[i][digit] = tmp>0?.5:-.5;
			//edge
			if (tmp==0) {
				edges[edgeId].start = i;
				edges[edgeId].end = i+(1<<digit);
				edgeId++;
			}
		}
	}

	for (int i = 0;i<4;i++) {
		//vertex
		vertices[16+2*i][i] = 1;
		//edges
		for (int j = 0;j<8;j++) {
			Edge e;
			e.start = 16+2*i;
			e.end = 1<<i;
			for (int k = 0;k<3;k++) {
				if (((j>>k)&1)>0) {
					int digit = (k<i?k:(k+1));
					e.end |= 1<<digit;
				}
			}
			edges[edgeId] = e;
			edgeId++;
		}
		//vertex
		vertices[17+2*i][i] = -1;
		//edges
		for (int j = 0;j<8;j++) {
			Edge e;
			e.start = 17+2*i;
			e.end = 0;
			for (int k = 0;k<3;k++) {
				if (((j>>k)&1)>0) {
					int digit = (k<i?k:(k+1));
					e.end |= 1<<digit;
				}
			}
			edges[edgeId] = e;
			edgeId++;
		}
	}
}

P24Cell::~P24Cell() {}

int P24Cell::getDimensions() {
	return 4;
}

std::vector<Edge>& P24Cell::getEdges() {
	return edges;
}

std::vector<VecN>& P24Cell::getVertices() {
	return vertices;
}

std::vector<Triangle>& P24Cell::getFaces() {
	return faces;
}

std::vector<VecN>& P24Cell::getNormals() {
	return normals;
}

void P24Cell::update() {}
