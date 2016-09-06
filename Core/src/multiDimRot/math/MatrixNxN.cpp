/*
 * MatrixNxN.cpp
 *
 *  Created on: 25.07.2016
 *      Author: malte
 */

#include <MatrixNxN.h>
#include <string>
#include <sstream>
#include <cmath>
#include "VecN.h"
#include <vector>
#include <iostream>
// elements[zeile][spalte]
MatrixNxN::MatrixNxN(int size) {
	elements = new float*[size];
	length = size;
	for (int i = 0;i<length;i++) {
		elements[i] = new float[size];
		for (int j = 0;j<length;j++) {
			elements[i][j] = (i==j?1:0);
		}
	}
}

MatrixNxN::MatrixNxN() {
	length = 0;
	elements = 0;
}

MatrixNxN::MatrixNxN(const MatrixNxN &other) {
	length = other.length;
	elements = new float*[length];
	for (int i = 0;i<length;i++) {
		elements[i] = new float[length];
		for (int j = 0;j<length;j++) {
			elements[i][j] = other.elements[i][j];
		}
	}
}

MatrixNxN::~MatrixNxN() {
	if (elements!=0) {
		delete[] elements;
	}
}

MatrixNxN MatrixNxN::operator*(const MatrixNxN &other) {
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
	return ret;
}

void MatrixNxN::operator=(const MatrixNxN &other) {
	if (other.elements==elements) {
		return;
	}
	bool keepArray  = length==other.length;
	length = other.length;
	float** old = elements;
	if (!keepArray) {
		elements = new float*[length];
	}
	for (int i = 0;i<length;i++) {
		if (!keepArray) {
			elements[i] = new float[length];
		}
		for (int j = 0;j<length;j++) {
			elements[i][j] = other.elements[i][j];
		}
	}
	if (!keepArray&&old!=0) {
		delete[] old;
	}
}

void MatrixNxN::rotate(int a1, int a2, float deg) {
	if (a1==a2) {
		throw "Can't rotate around one axis";
	}
	if (a1>length-2||a2>length-2) {
		throw "Can't rotate around the last axis";
	}
	float angle = M_PI*deg/180;
	float cos = std::cos(angle);
	float sin = std::sin(angle);
	for (int i = 0;i<length;i++) {
		float d0Tmp = cos*elements[a1][i]-sin*elements[a2][i];
		elements[a2][i] = cos*elements[a2][i]+sin*elements[a1][i];
		elements[a1][i] = d0Tmp;
	}
}

void MatrixNxN::scale(float scale, int axis) {
	for (int i = 0;i<length;i++) {
		elements[axis][i]*=scale;
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
}

void MatrixNxN::apply(VecN &in, VecN &out) {
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
		out.setElement(i, val);
	}
}

int MatrixNxN::getSize() {
	return length;
}

void MatrixNxN::applyMass(std::vector<VecN> &in, std::vector<VecN> &out) {
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

void MatrixNxN::prepareForRender() {
	scale(.5);
	if (length>1) {
		translate(0, .5);
		if (length>2) {
			translate(1, .5);
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
}

std::string MatrixNxN::toString() {
	std::stringstream ret;
	for (int i = 0;i<length;i++) {
		for (int j = 0;j<length;j++) {
			ret << (std::abs(elements[i][j])<.001?0:elements[i][j]) << "\t";
		}
		ret<<"\n";
	}
	return ret.str();
}
