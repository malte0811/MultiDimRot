#ifndef OBJPOLYTOPE_H_
#define OBJPOLYTOPE_H_

#include "Polytope.h"


class ObjPolytope: public Polytope {
public:
	ObjPolytope(std::istream* in);
	virtual ~ObjPolytope();
	virtual const std::vector<VecN>& getVertices() const;
	virtual const std::vector<Edge>& getEdges() const;
	virtual const std::vector<Triangle>& getFaces() const;
	virtual const std::vector<VecN>& getNormals() const;
	virtual int getDimensions() const;
	virtual void update();
private:
	unsigned int dims;
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
	std::vector<VecN> normals;
	std::vector<Triangle> faces;
};

#endif /* OBJPOLYTOPE_H_ */
