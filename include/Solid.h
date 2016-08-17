/*
 * Solid.h
 *
 *  Created on: 25.07.2016
 *      Author: malte
 */

#ifndef SRC_MULTIDIMROT_SOLIDS_SOLID_H_
#define SRC_MULTIDIMROT_SOLIDS_SOLID_H_
#include <vector>
#include <VecN.h>
#include <MatrixNxN.h>
struct Edge {
	int start;
	int end;
};
typedef struct Edge Edge;
class Solid {
public:
	Solid();
	virtual ~Solid() = 0;
	virtual std::vector<VecN>& getVertices() = 0;
	virtual std::vector<Edge>& getEdges() = 0;
	/**
	 * The amount of dimensions of this solid. MUST NOT CHANGE!
	 */
	virtual int getDimensions() = 0;
	virtual void update() = 0;
	void writeObj(std::ostream* o, MatrixNxN &apply);
};

#endif /* SRC_MULTIDIMROT_SOLIDS_SOLID_H_ */
