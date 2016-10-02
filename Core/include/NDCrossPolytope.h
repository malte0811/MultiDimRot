#ifndef SRC_MULTIDIMROT_POLYTOPE_NDCROSSPOLYTOPE_H_
#define SRC_MULTIDIMROT_POLYTOPE_NDCROSSPOLYTOPE_H_

#include <vector>
#include "Polytope.h"

class NDCrossPolytope:public Polytope {
public:
	NDCrossPolytope(int i);
	virtual ~NDCrossPolytope();
	virtual const std::vector<VecN>& getVertices() const;
	virtual const std::vector<Edge>& getEdges() const;
	virtual const std::vector<Triangle>& getFaces() const;
	virtual const std::vector<VecN>& getNormals() const;
	virtual int getDimensions() const;
	virtual void update();
private:
	int dimensions;
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
	std::vector<VecN> normals;
	std::vector<Triangle> faces;
};

#endif /* SRC_MULTIDIMROT_POLYTOPE_NDCROSSPOLYTOPE_H_ */
