#ifndef MATRIXNXN_H_
#define MATRIXNXN_H_
#include <string>
#include <vector>
#include <VecN.h>
#include <iostream>

class MatrixNxN {
public:
	MatrixNxN(int size, bool inverse = true);
	MatrixNxN();
	MatrixNxN(const MatrixNxN& other);
	virtual ~MatrixNxN();
	MatrixNxN operator*(const MatrixNxN &other) const;
	void operator=(const MatrixNxN &other);
	/**
	 * multiplies the argument matrix onto the matrix. WARNING: a*=b is equivalent to a = b*a
	 */
	void operator*=(const MatrixNxN &other);
	std::string toString() const;

	void initInverse();
	void setToIdentity();

	void rotate(int a1, int a2, float angleDeg);
	void translate(int axis, float amount);
	void scale(float scale, int axis);
	void scale(float scale);
	void project(int dim, float val);

	void apply(const VecN &in, VecN &out) const;
	void applyMass(const std::vector<VecN> &in, std::vector<VecN> &out) const;
	void applyInvT(const VecN &in, VecN &out) const;
	void applyInvTMass(const std::vector<VecN> &in, std::vector<VecN> &out) const;
	int getSize() const;
	void checkInverse() const;
private:
	float** elements;
	float** buffer = 0;
	float** inverse = 0;
	int length;
	friend std::ostream& operator<<(std::ostream& os, const MatrixNxN& en);
};
#endif /* MATRIXNXN_H_ */
