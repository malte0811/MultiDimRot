#ifndef SRC_MULTIDIMROT_POLYTOPE_NDCROSSPOLYTOPE_H_
#define SRC_MULTIDIMROT_POLYTOPE_NDCROSSPOLYTOPE_H_

#include <vector>
#include "Polytope.h"

class NDCrossPolytope:public Polytope{
public:
	NDCrossPolytope(int i);
	virtual ~NDCrossPolytope();
	virtual std::vector<VecN>& getVertices();
	virtual std::vector<Edge>& getEdges();
	virtual int getDimensions();
	virtual void update();
private:
	int dimensions;
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
};

#endif /* SRC_MULTIDIMROT_POLYTOPE_NDCROSSPOLYTOPE_H_ */
