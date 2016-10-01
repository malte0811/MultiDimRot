#ifndef SRC_MULTIDIMROT_RENDERER_H_
#define SRC_MULTIDIMROT_RENDERER_H_
#include <Polytope.h>
#include <MatrixNxN.h>
#include <vector>
#include <VecN.h>
#include <GL/glew.h>

class Renderer {
public:
	Renderer(Polytope* &polyt, std::vector<MatrixNxN> &startMats, MatrixNxN &powerMat, std::vector<MatrixNxN> &endMats, const bool* renderType);
	virtual ~Renderer();
	void render();
private:
	inline void renderFaces(const std::vector<Triangle> &faces, const VecN &light);
	inline void renderEdges(const std::vector<Edge> &edges);
	inline void renderVertices();
	void init_resources();
	Polytope* polyt;
	std::vector<MatrixNxN> &startMats;
	MatrixNxN &powerMat;
	std::vector<MatrixNxN> &endMats;
	const bool* renderType;
	GLuint program = 0;
	GLint uniform_baseColor = 0;
	GLint attribute_pos = 0;
	std::vector<VecN> transformedVertices;
	std::vector<VecN> transformedNormals;
	std::vector<float> brightness;
	GLfloat* data = 0;
	int dataSize = -1;
	int vSize = 0;
	int brightnessSize = 0;
	const int dims;
};

#endif /* SRC_MULTIDIMROT_RENDERER_H_ */
