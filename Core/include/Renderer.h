/*
 * Renderer.h
 *
 *  Created on: 16.09.2016
 *      Author: malte
 */

#ifndef SRC_MULTIDIMROT_RENDERER_H_
#define SRC_MULTIDIMROT_RENDERER_H_
#include <Polytope.h>
#include <MatrixNxN.h>
#include <vector>
#include <VecN.h>

class Renderer {
public:
	Renderer(Polytope* &polyt, std::vector<MatrixNxN> &startMats, MatrixNxN &powerMat, std::vector<MatrixNxN> &endMats);
	virtual ~Renderer();
	void render();
private:
	Polytope* polyt;
	std::vector<MatrixNxN> &startMats;
	MatrixNxN &powerMat;
	std::vector<MatrixNxN> &endMats;
};

#endif /* SRC_MULTIDIMROT_RENDERER_H_ */
