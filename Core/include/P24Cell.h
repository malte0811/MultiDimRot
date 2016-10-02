#ifndef MULTIDIMROT_POLYTOPE_P24CELL_H_
#define MULTIDIMROT_POLYTOPE_P24CELL_H_
#include <vector>
#include <Polytope.h>
class P24Cell:public Polytope {
public:
	P24Cell();
	virtual ~P24Cell();
	virtual const std::vector<VecN>& getVertices() const;
	virtual const std::vector<Edge>& getEdges() const;
	virtual const std::vector<Triangle>& getFaces() const;
	virtual const std::vector<VecN>& getNormals() const;
	virtual int getDimensions() const;
	virtual void update();
private:
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
	std::vector<VecN> normals;
	std::vector<Triangle> faces;
};

#endif /* MULTIDIMROT_POLYTOPE_P24CELL_H_ */
