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
