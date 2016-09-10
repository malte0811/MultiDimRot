
#ifndef MULTIDIMROT_POLYTOPE_JOINEDPOLYTOPE_H_
#define MULTIDIMROT_POLYTOPE_JOINEDPOLYTOPE_H_

#include <Polytope.h>
#include <vector>

class JoinedPolytope:public Polytope {
public:
	JoinedPolytope(Polytope* p0, Polytope* p1);
	virtual ~JoinedPolytope();
	virtual std::vector<VecN>& getVertices();
	virtual std::vector<Edge>& getEdges();
	virtual std::vector<Triangle>& getFaces();
	virtual int getDimensions();
	virtual void update();
private:
	Polytope* a;
	Polytope* b;
	std::vector<Edge> retE;
	std::vector<VecN> retV;
	std::vector<Triangle> retF;
};

#endif /* MULTIDIMROT_POLYTOPE_JOINEDPOLYTOPE_H_ */
