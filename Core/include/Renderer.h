/*******************************************************************************
 * This file is part of MultiDimRot2.0.
 * Copyright (C) 2016 malte0811
 *
 * MultiDimRot2.0 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MultiDimRot2.0 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MultiDimRot2.0.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
#ifndef SRC_MULTIDIMROT_RENDERER_H_
#define SRC_MULTIDIMROT_RENDERER_H_
#include <Polytope.h>
#include <MatrixNxN.h>
#include <vector>
#include <VecN.h>
#include <GL/glew.h>

class Renderer {
public:
	Renderer(Polytope* &polyt, std::vector<MatrixNxN> &startMats, MatrixNxN &powerMat, std::vector<MatrixNxN> &endMats, const bool* renderType, int threadCount);
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
	const int threadCount;
};

#endif /* SRC_MULTIDIMROT_RENDERER_H_ */
