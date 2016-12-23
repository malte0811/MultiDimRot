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
#include <MatrixNxN.h>
#include <string>
#include <sstream>
#include <cmath>
#include "VecN.h"
#include <vector>
#include <iostream>
#include <Util.h>
// elements[row][column]
MatrixNxN::MatrixNxN(int size, bool inverse) {
	elements = new float*[size];
	buffer = new float*[size];
	length = size;
	for (int i = 0;i<length;i++) {
		elements[i] = new float[size];
		buffer[i] = new float[size];
		for (int j = 0;j<length;j++) {
			elements[i][j] = (i==j?1:0);
		}
	}
	if (inverse) {
		initInverse();
	}
}

MatrixNxN::MatrixNxN() {
	length = 0;
	elements = 0;
}

MatrixNxN::MatrixNxN(const MatrixNxN &other) {
	length = other.length;
	elements = new float*[length];
	buffer = new float*[length];
	for (int i = 0;i<length;i++) {
		elements[i] = new float[length];
		buffer[i] = new float[length];
		for (int j = 0;j<length;j++) {
			elements[i][j] = other.elements[i][j];
		}
	}
	if (other.inverse!=0) {
		inverse = new float*[length];
		for (int i = 0;i<length;i++) {
			inverse[i] = new float[length];
			for (int j = 0;j<length;j++) {
				inverse[i][j] = other.inverse[i][j];
			}
		}
	}
}

MatrixNxN::~MatrixNxN() {
	util::del2DimArray(elements, length);
	util::del2DimArray(inverse, length);
	util::del2DimArray(buffer, length);
}

void MatrixNxN::initInverse() {
	if (inverse==0) {
		inverse = new float*[length];
		for (int i = 0;i<length;i++) {
			inverse[i] = new float[length];
			for (int j = 0;j<length;j++) {
				inverse[i][j] = (i==j?1:0);
			}
		}
	}
}

void MatrixNxN::setToIdentity() {
	for (int i = 0;i<length;i++) {
		for (int j = 0;j<length;j++) {
			elements[i][j] = (i==j?1:0);
		}
	}
	if (inverse!=0) {
		for (int i = 0;i<length;i++) {
			for (int j = 0;j<length;j++) {
				inverse[i][j] = (i==j?1:0);
			}
		}
	}
}

MatrixNxN MatrixNxN::operator*(const MatrixNxN &other) const {
	if (length!=other.length) {
		throw "Can't multiply matrices of different sizes!";
	}
	MatrixNxN ret(length);
	for (int i = 0;i<length;i++) {
		for (int j = 0;j<length;j++) {
			float sum = 0;
			for (int k = 0;k<length;k++) {
				sum+=elements[i][k]*other.elements[k][j];
			}
			ret.elements[i][j] = sum;
		}
	}
	if ((inverse==0)!=(other.inverse==0)) {
		throw "Can't multiply a matrix with an inverse with one without one!";
	}
	if (inverse!=0) {
		ret.initInverse();
		for (int i = 0;i<length;i++) {
			for (int j = 0;j<length;j++) {
				float sum = 0;
				for (int k = 0;k<length;k++) {
					sum+=other.inverse[i][k]*inverse[k][j];
				}
				ret.inverse[i][j] = sum;
			}
		}
	}
	return ret;
}

void MatrixNxN::operator=(const MatrixNxN &other) {
	if (other.elements==elements) {
		return;
	}
	bool keepArray = length==other.length;
	length = other.length;
	if (!keepArray) {
		util::del2DimArray(elements, length);
		util::del2DimArray(buffer, length);
		elements = new float*[length];
		buffer = new float*[length];
	}
	for (int i = 0;i<length;i++) {
		if (!keepArray) {
			elements[i] = new float[length];
			buffer[i] = new float[length];
		}
		for (int j = 0;j<length;j++) {
			elements[i][j] = other.elements[i][j];
		}
	}
	if (other.inverse==0) {
		util::del2DimArray(inverse, length);
		inverse = 0;
	} else {
		if (!keepArray) {
			util::del2DimArray(inverse, length);
			inverse = new float*[length];
		}
		for (int i = 0;i<length;i++) {
			if (!keepArray) {
				inverse[i] = new float[length];
			}
			for (int j = 0;j<length;j++) {
				inverse[i][j] = other.inverse[i][j];
			}
		}
	}
}
void MatrixNxN::operator*=(const MatrixNxN &other) {
	//copy matrix to buffer
	for (int i = 0;i<length;i++) {
		for (int j = 0;j<length;j++) {
			buffer[i][j] = elements[i][j];
		}
	}
	//multiply main matrix
	for (int i = 0;i<length;i++) {
		for (int j = 0;j<length;j++) {
			float sum = 0;
			for (int k = 0;k<length;k++) {
				sum+=other.elements[i][k]*buffer[k][j];
			}
			elements[i][j] = sum;
		}
	}
	if (inverse!=0) {
		//copy inverse to buffer
		for (int i = 0;i<length;i++) {
			for (int j = 0;j<length;j++) {
				buffer[i][j] = inverse[i][j];
			}
		}
		//multiply inverse matrix
		for (int i = 0;i<length;i++) {
			for (int j = 0;j<length;j++) {
				float sum = 0;
				for (int k = 0;k<length;k++) {
					sum+=buffer[i][k]*other.inverse[k][j];
				}
				inverse[i][j] = sum;
			}
		}
	}
}

void MatrixNxN::rotate(int a1, int a2, float deg) {
	if (a1==a2) {
		throw "Can't rotate around one axis";
	}
	float angle = M_PI*deg/180;
	float cos = std::cos(angle);
	float sin = std::sin(angle);
	for (int i = 0;i<length;i++) {
		float d0Tmp = cos*elements[a1][i]-sin*elements[a2][i];
		elements[a2][i] = cos*elements[a2][i]+sin*elements[a1][i];
		elements[a1][i] = d0Tmp;
	}
	if (inverse!=0) {
		for (int i = 0;i<length;i++) {
			float d0Tmp = cos*inverse[i][a1]-sin*inverse[i][a2];
			inverse[i][a2] = cos*inverse[i][a2]+sin*inverse[i][a1];
			inverse[i][a1] = d0Tmp;
		}
	}
}

void MatrixNxN::scale(float scale, int axis) {
	for (int i = 0;i<length;i++) {
		elements[axis][i]*=scale;
	}
	if (inverse!=0) {
		for (int i = 0;i<length;i++) {
			inverse[i][axis]/=scale;
		}
	}
}

void MatrixNxN::scale(float s) {
	for (int i = 0;i<length-1;i++) {
		scale(s, i);
	}
}

void MatrixNxN::translate(int axis, float amount) {
	if (axis>length-2) {
		throw "Can't translate along the last axis";
	}
	for (int i = 0;i<length;i++) {
		elements[axis][i] += elements[length-1][i]*amount;
	}
	if (inverse!=0) {
		for (int i = 0;i<length;i++) {
			inverse[axis][i] -= inverse[length-1][i]*amount;
		}
	}
}

void MatrixNxN::project(int dim, float val) {
	if (dim>=length) {
		throw "Can't project on higher dimension than the matrix";
	}
	for (int i = 0;i<length;i++) {
		float newVal = elements[length-1][i]+val*elements[dim-1][i];
		elements[length-1][i] = newVal;
	}
	if (inverse!=0) {
		for (int i = 0;i<length;i++) {
			float newVal = inverse[i][dim-1]-val*inverse[i][length-1];
			inverse[i][dim-1] = newVal;
		}
	}
}

void MatrixNxN::apply(const VecN &in, VecN &out) const {
	int dims = in.getDimensions();
	if (dims>length) {
		throw "Can't apply to a larger vector!";
	}
	for (int i = 0;i<length;i++) {
		float val = 0;
		int j = 0;
		for (;j<dims;j++) {
			val+=elements[i][j]*in[j];
		}
		for (;j<length;j++) {
			val+=elements[i][j];
		}
		out[i] = val;
	}
}

void MatrixNxN::applyMass(const std::vector<VecN> &in, std::vector<VecN> &out) const {
	int outSize = out.size();
	int inSize = in.size();
	for (int i = 0;i<inSize;i++) {
		if (i<outSize) {
			apply(in[i], out[i]);
		} else {
			VecN tmp(length);
			apply(in[i], tmp);
			out.push_back(tmp);
		}
	}
}

void MatrixNxN::applyInvT(const VecN &in, VecN &out) const {
	int dims = in.getDimensions();
	if (dims>length) {
		throw "Can't apply to a larger vector!";
	}
	for (int i = 0;i<length;i++) {
		float val = 0;
		int j = 0;
		for (;j<dims;j++) {
			val+=inverse[j][i]*in[j];
		}
		for (;j<length;j++) {
			val+=inverse[j][i];
		}
		out[i] = val;
	}
}

void MatrixNxN::applyInvTMass(const std::vector<VecN> &in, std::vector<VecN> &out) const {
	int outSize = out.size();
	int inSize = in.size();
	for (int i = 0;i<inSize;i++) {
		if (i<outSize) {
			applyInvT(in[i], out[i]);
		} else {
			VecN tmp(length);
			applyInvT(in[i], tmp);
			out.push_back(tmp);
		}
	}
}

int MatrixNxN::getSize() const {
	return length;
}

std::string MatrixNxN::toString() const {
	std::stringstream ret;
	for (int i = 0;i<length;i++) {
		for (int j = 0;j<length;j++) {
			ret << (std::abs(elements[i][j])<.001?0:elements[i][j]) << "\t";
		}
		ret<<"\n";
	}
	return ret.str();
}

void MatrixNxN::checkInverse() const {
	MatrixNxN ret(length);
	for (int i = 0;i<length;i++) {
		for (int j = 0;j<length;j++) {
			float sum = 0;
			for (int k = 0;k<length;k++) {
				sum+=elements[i][k]*inverse[k][j];
			}
			ret.elements[i][j] = sum;
		}
	}
	ret.inverse = 0;
	std::cout << ret;
}

void MatrixNxN::setElement(int a, int b, float c) {
	if (inverse!=0) {
		throw "Can't set individual elements in matrices with inverses";
	}
	elements[a][b] = c;
}

std::ostream& operator<<(std::ostream& os, const MatrixNxN& en) {
	os << "Matrix: \n";
	for (int i = 0;i<en.length;i++) {
		for (int j = 0;j<en.length;j++) {
			os << (std::abs(en.elements[i][j])<.001?0:en.elements[i][j]) << "\t";
		}
		os<<"\n";
	}
	if (en.inverse!=0) {
		os << "Inverse: \n";
		for (int i = 0;i<en.length;i++) {
			for (int j = 0;j<en.length;j++) {
				os << (std::abs(en.inverse[i][j])<.001?0:en.inverse[i][j]) << "\t";
			}
			os<<"\n";
		}
	}
	return os;
}
