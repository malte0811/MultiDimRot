/*
 * ObjSolid.h
 *
 *  Created on: 28.07.2016
 *      Author: malte
 */

#ifndef OBJSOLID_H_
#define OBJSOLID_H_

#include <Solid.h>


class ObjSolid: public Solid {
public:
	ObjSolid(std::istream* in);
	virtual ~ObjSolid();
	virtual std::vector<VecN>& getVertices();
	virtual std::vector<Edge>& getEdges();
	virtual int getDimensions();
	virtual void update();
private:
	unsigned int dims;
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
};

#endif /* OBJSOLID_H_ */
