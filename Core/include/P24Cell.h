#ifndef MULTIDIMROT_POLYTOPE_P24CELL_H_
#define MULTIDIMROT_POLYTOPE_P24CELL_H_
#include <vector>
#include <Polytope.h>
class P24Cell:public Polytope {
public:
	P24Cell();
	virtual ~P24Cell();
	virtual std::vector<VecN>& getVertices();
	virtual std::vector<Edge>& getEdges();
	virtual std::vector<Triangle>& getFaces();
	virtual std::vector<VecN>& getNormals();
	virtual int getDimensions();
	virtual void update();
private:
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
	std::vector<VecN> normals;
	std::vector<Triangle> faces;
};

#endif /* MULTIDIMROT_POLYTOPE_P24CELL_H_ */
