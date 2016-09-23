#include <NDCube.h>
#include <Polytope.h>
#include <vector>
#include <iostream>
void initQuad(std::vector<Triangle>& tris, int startIndex, int a, int b, int c, int d, int normal) {
	tris[startIndex].normals[0] = normal;
	tris[startIndex].normals[1] = normal;
	tris[startIndex].normals[2] = normal;
	tris[startIndex].vertices[0] = a;
	tris[startIndex].vertices[1] = b;
	tris[startIndex].vertices[2] = c;
	tris[startIndex+1].normals[0] = normal;
	tris[startIndex+1].normals[1] = normal;
	tris[startIndex+1].normals[2] = normal;
	tris[startIndex+1].vertices[0] = c;
	tris[startIndex+1].vertices[1] = d;
	tris[startIndex+1].vertices[2] = b;
}
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
			vertices[i].setElement(digit, is0?-.5:.5);
			//edge
			if (!is0) {
				edges[edgeId].start = i;
				edges[edgeId].end = i+(1<<digit);
				edgeId++;
				for (int digit2 = digit+1;digit2<dims;digit2++) {
					if (!getBit(i, digit2)) {
						initQuad(faces, 2*faceId, i, i+(1<<digit), i+(1<<digit2), i+(1<<digit)+(1<<digit2), faceId);
						for (int digit3 = 0;digit3<dims;digit3++) {
							if (digit3!=digit&&digit3!=digit2) {
								if (getBit(i, digit3)) {
									normals[faceId].setElement(digit3, 1);
								} else {
									normals[faceId].setElement(digit3, -1);
								}
							}
						}
						normals[faceId].scaleToLength(1, false);
						faceId++;
					}
				}
			}
		}
	}

}

NDCube::~NDCube() {}

int NDCube::getDimensions() {
	return dimensions;
}

std::vector<Edge>& NDCube::getEdges() {
	return edges;
}

std::vector<VecN>& NDCube::getVertices() {
	return vertices;
}
std::vector<Triangle>& NDCube::getFaces() {
	return faces;
}
std::vector<VecN>& NDCube::getNormals() {
	return normals;
}

void NDCube::update() {}
