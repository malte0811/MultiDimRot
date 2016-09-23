#ifndef SRC_MULTIDIMROT_POLYTOPE_H_
#define SRC_MULTIDIMROT_POLYTOPE_H_
#include <vector>
#include <array>
#include <VecN.h>
#include <MatrixNxN.h>
struct Edge {
	int start;
	int end;
};
struct Triangle {
	std::array<int, 3> vertices;
	std::array<int, 3> normals;
};

typedef struct Edge Edge;
class Polytope {
public:
	Polytope();
	virtual ~Polytope() = 0;
	virtual std::vector<VecN>& getVertices() = 0;
	virtual std::vector<VecN>& getNormals() = 0;
	virtual std::vector<Edge>& getEdges() = 0;
	virtual std::vector<Triangle>& getFaces() = 0;
	/**
	 * The amount of dimensions of this polytope. MUST NOT CHANGE!
	 */
	virtual int getDimensions() = 0;
	virtual void update() = 0;
	void writeObj(std::ostream* o, MatrixNxN &apply);
	float getLength(Edge e);
};

#endif /* SRC_MULTIDIMROT_POLYTOPE_H_ */
