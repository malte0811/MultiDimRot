#ifndef SRC_MULTIDIMROT_POLYTOPE_NDSPHERE_H_
#define SRC_MULTIDIMROT_POLYTOPE_NDSPHERE_H_

#include <Polytope.h>
class NDSphere: public Polytope {
public:
	NDSphere(int dimensions, int verticesPerHalf = 18);
	virtual ~NDSphere();
	virtual const std::vector<VecN>& getVertices() const;
	virtual const std::vector<Edge>& getEdges() const;
	virtual const std::vector<Triangle>& getFaces() const;
	virtual const std::vector<VecN>& getNormals() const;
	virtual int getDimensions() const;
	virtual void update();
private:
	int dimensions;
	std::vector<Edge> edges;
	std::vector<VecN> vertices;
	std::vector<VecN> normals;
	std::vector<Triangle> faces;
};

#endif
