#ifndef MATRIXNXN_H_
#define MATRIXNXN_H_
#include <string>
#include <vector>
#include "VecN.h"

class MatrixNxN {
public:
	MatrixNxN(int size);
	MatrixNxN();
	MatrixNxN(const MatrixNxN& other);
	virtual ~MatrixNxN();
	MatrixNxN operator*(const MatrixNxN &other);
	void operator=(const MatrixNxN &other);
	std::string toString();
	void rotate(int a1, int a2, float angleDeg);
	void translate(int axis, float amount);
	void scale(float scale, int axis);
	void scale(float scale);
	void apply(const VecN &in, VecN &out);
	void applyMass(std::vector<VecN> &in, std::vector<VecN> &out);
	int getSize();
	void prepareForRender();
	void project(int dim, float val);
private:
	float** elements;
	int length;
};

#endif /* MATRIXNXN_H_ */
