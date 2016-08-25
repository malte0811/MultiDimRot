#ifndef SRC_MULTIDIMROT_POLYTOPE_NDCUBE_H_
#define SRC_MULTIDIMROT_POLYTOPE_NDCUBE_H_

#include <vector>
#include "Polytope.h"

class NDCube: public Polytope {
public:
	NDCube(int dims);
	virtual ~NDCube();
	virtual std::vector<VecN>& getVertices();
	virtual std::vector<Edge>& getEdges();
	virtual int getDimensions();
	virtual void update();
private:
	int dimensions;
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
};

#endif /* SRC_MULTIDIMROT_POLYTOPE_NDCUBE_H_ */
