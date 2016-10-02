#include <Polytope.h>
#include <ostream>
#include <vector>
#include <cmath>


Polytope::Polytope() {}

Polytope::~Polytope() {}

float Polytope::getLength(Edge e) {
	const std::vector<VecN>& vertices = getVertices();
	float sum = 0;
	VecN a = vertices[e.start];
	VecN b = vertices[e.end];
	for (int i = 0;i<getDimensions();i++) {
		sum += (a[i]-b[i])*(a[i]-b[i]);
	}
	return std::sqrt(sum);
}
