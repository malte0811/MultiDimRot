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
	virtual const std::vector<VecN>& getVertices() const = 0;
	virtual const std::vector<VecN>& getNormals() const = 0;
	virtual const std::vector<Edge>& getEdges() const = 0;
	virtual const std::vector<Triangle>& getFaces() const = 0;
	/**
	 * The amount of dimensions of this polytope. MUST NOT CHANGE!
	 */
	virtual int getDimensions() const = 0;
	virtual void update() = 0;
	void writeObj(std::ostream* o, const MatrixNxN &apply) const;
	float getLength(Edge e);
};

#endif /* SRC_MULTIDIMROT_POLYTOPE_H_ */
