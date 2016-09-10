#ifndef OBJPOLYTOPE_H_
#define OBJPOLYTOPE_H_

#include "Polytope.h"


class ObjPolytope: public Polytope {
public:
	ObjPolytope(std::istream* in);
	virtual ~ObjPolytope();
	virtual std::vector<VecN>& getVertices();
	virtual std::vector<Edge>& getEdges();
	virtual std::vector<Triangle>& getFaces();
	virtual int getDimensions();
	virtual void update();
private:
	unsigned int dims;
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
	std::vector<Triangle> faces;
};

#endif /* OBJPOLYTOPE_H_ */
