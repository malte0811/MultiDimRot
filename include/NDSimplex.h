/*
 * NDSimplex.h
 *
 *  Created on: 29.07.2016
 *      Author: malte
 */

#ifndef SRC_MULTIDIMROT_SOLID_NDSIMPLEX_H_
#define SRC_MULTIDIMROT_SOLID_NDSIMPLEX_H_

#include <Solid.h>

class NDSimplex: public Solid {
public:
	NDSimplex(int i);
	virtual ~NDSimplex();
	virtual std::vector<VecN>& getVertices();
	virtual std::vector<Edge>& getEdges();
	virtual int getDimensions();
	virtual void update();
private:
	int dimensions;
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
};

#endif /* SRC_MULTIDIMROT_SOLID_NDSIMPLEX_H_ */
