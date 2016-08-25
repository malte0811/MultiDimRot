#include <Polytope.h>
#include <ostream>
#include <vector>

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
