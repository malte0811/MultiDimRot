#include <Polytope.h>
#include <ostream>
#include <vector>
#include <cmath>


Polytope::Polytope() {}

Polytope::~Polytope() {}

void Polytope::writeObj(std::ostream* out, MatrixNxN &apply) {
	int dims = getDimensions();
	*out << "dims " << dims << "\n";
	std::vector<VecN> verticesOld = getVertices();
	std::vector<VecN> vertices;
	apply.applyMass(verticesOld, vertices);
	for (unsigned int i = 0;i<vertices.size();i++) {
		*out << "v ";
		VecN curr = vertices[i];
		for (int j = 0;j<dims;j++) {
			*out << curr.getElement(j, 0);
			if (j<dims-1) {
				*out << " ";
			}
		}
		*out << "\n";
	}
	*out << "\n";
	std::vector<Edge> edges = getEdges();
	for (unsigned int i = 0;i<edges.size();i++) {
		*out << "e ";
		Edge e = edges[i];
		*out << e.start << " " << e.end << "\n";
	}
	//TODO add faces+normals once they exist
}

float Polytope::getLength(Edge e) {
	std::vector<VecN>& vertices = getVertices();
	float sum = 0;
	VecN a = vertices[e.start];
	VecN b = vertices[e.end];
	for (int i = 0;i<getDimensions();i++) {
		sum += (a[i]-b[i])*(a[i]-b[i]);
	}
	return std::sqrt(sum);
}
