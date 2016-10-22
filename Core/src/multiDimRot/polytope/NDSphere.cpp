#include <NDSphere.h>
#include <cmath>

/*
 * This is not perfect, it generates a lot of duplicate vertices.
 * But it works and the FPS is still pretty high, so I will probably leave it as it is.
 */
NDSphere::NDSphere(int dims, int verticesPerHalf) {
	dimensions = dims;
	const float angle = 180/verticesPerHalf;
	vertices = std::vector<VecN>(dims==1?2:1, VecN(dimensions));
	vertices[0][0] = 1;
	if (dims==1)
		vertices[1][0] = -1;
	MatrixNxN rot(dims, false);
	MatrixNxN rotBase(dims, false);
	for (int i = 1;i<dimensions;i++) {
		const int prevVSize = vertices.size();
		const int prevESize = edges.size();
		rot.setToIdentity();
		rotBase.setToIdentity();
		rotBase.rotate(i-1, i, angle);
		const int max = (i==dimensions-1)?2*verticesPerHalf-1:verticesPerHalf;
		for (int j = 0;j<max;j++) {
			rot *= rotBase;
			for (int k = 0;k<prevVSize;k++) {
				VecN tmp(dimensions);
				rot.apply(vertices[k], tmp);
				vertices.push_back(tmp);
			}
			const int vOffset = prevVSize*(j+1);
			for (int k = 0;k<prevESize;k++) {
				Edge tmp;
				tmp.start = edges[k].start+vOffset;
				tmp.end = edges[k].end+vOffset;
				edges.push_back(tmp);
			}
		}
		const int newVSize = vertices.size();
		const int maxV = (i==dimensions-1)?newVSize:(newVSize-prevVSize);
		for (int j = 0;j<maxV;j++) {
			Edge e;
			e.start = j;
			e.end = (j+prevVSize)%newVSize;
			edges.push_back(e);
		}
	}
	if (dimensions==2)
		return;
	// clean up (most of) the mess of duplicates and zero-length edges
	const std::vector<VecN> oldVertices = vertices;
	const std::vector<Edge> oldEdges = edges;
	//TODO get rid of this mess
	std::vector<int> vertexMap = std::vector<int>(oldVertices.size());
	vertices = std::vector<VecN>(2, VecN(dimensions));
	vertices[0][0] = 1;
	vertices[1][0] = -1;
	edges = std::vector<Edge>();
	int nextId = 2;
	for (int i = 0;i<oldVertices.size();i++) {
		int mod = i%(verticesPerHalf+1);
		int id;
		if (mod==0||mod==verticesPerHalf) {
			id = mod/verticesPerHalf;
		} else {
			id = nextId;
			nextId++;
			vertices.push_back(oldVertices[i]);
		}
		vertexMap[i] = id;
	}
	for (int i = 0;i<oldEdges.size();i++) {
		if ((oldEdges[i].start%(verticesPerHalf+1))%verticesPerHalf!=0||
				(oldEdges[i].end%(verticesPerHalf+1))%verticesPerHalf!=0) {
			Edge tmp;
			tmp.start = vertexMap[oldEdges[i].start];
			tmp.end = vertexMap[oldEdges[i].end];
			edges.push_back(tmp);
		}
	}
}



NDSphere::~NDSphere() {

}

const std::vector<VecN>& NDSphere::getVertices() const {
	return vertices;
}

const std::vector<Edge>& NDSphere::getEdges() const {
	return edges;
}

const std::vector<Triangle>& NDSphere::getFaces() const {
	return faces;
}

const std::vector<VecN>& NDSphere::getNormals() const {
	return normals;
}

int NDSphere::getDimensions() const {
	return dimensions;
}

void NDSphere::update() {

}
