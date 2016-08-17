/*
 * NBCube.h
 *
 *  Created on: 25.07.2016
 *      Author: malte
 */

#ifndef SRC_MULTIDIMROT_SOLIDS_NDCUBE_H_
#define SRC_MULTIDIMROT_SOLIDS_NDCUBE_H_

#include <Solid.h>
#include <vector>

class NDCube: public Solid {
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

#endif /* SRC_MULTIDIMROT_SOLIDS_NDCUBE_H_ */
