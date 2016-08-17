/*
 * NDCrossPolytope.h
 *
 *  Created on: 29.07.2016
 *      Author: malte
 */

#ifndef SRC_MULTIDIMROT_SOLID_NDCROSSPOLYTOPE_H_
#define SRC_MULTIDIMROT_SOLID_NDCROSSPOLYTOPE_H_

#include <Solid.h>
#include <vector>

class NDCrossPolytope:public Solid{
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

#endif /* SRC_MULTIDIMROT_SOLID_NDCROSSPOLYTOPE_H_ */
