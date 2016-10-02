
#ifndef MULTIDIMROT_POLYTOPE_JOINEDPOLYTOPE_H_
#define MULTIDIMROT_POLYTOPE_JOINEDPOLYTOPE_H_

#include <Polytope.h>
#include <vector>

class JoinedPolytope:public Polytope {
public:
	JoinedPolytope(Polytope* p0, Polytope* p1);
	virtual ~JoinedPolytope();
	virtual const std::vector<VecN>& getVertices() const;
	virtual const std::vector<Edge>& getEdges() const;
	virtual const std::vector<Triangle>& getFaces() const;
	virtual const std::vector<VecN>& getNormals() const;
	virtual int getDimensions() const;
	virtual void update();
private:
	Polytope* a;
	Polytope* b;
	std::vector<Edge> retE;
	std::vector<VecN> retV;
	std::vector<VecN> retN;
	std::vector<Triangle> retF;
};

#endif /* MULTIDIMROT_POLYTOPE_JOINEDPOLYTOPE_H_ */
