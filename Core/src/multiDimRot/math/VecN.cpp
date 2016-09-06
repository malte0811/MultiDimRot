/*
 * VecN.cpp
 *
 *  Created on: 24.07.2016
 *      Author: malte
 */
#include "VecN.h"
#include <iostream>
#include <string>
#include <sstream>

VecN::VecN(int dims) {
	dimensions = dims;
	elements = new float[dimensions];
	for (int i = 0;i<dimensions;i++) {
		elements[i] = 0;
	}
}

VecN::VecN() {
	elements = 0;
	dimensions = 0;
}

VecN::VecN(const VecN &other) {
	dimensions = other.dimensions;
	elements = new float[dimensions];
	for (int i = 0;i<dimensions;i++) {
		elements[i] = other.elements[i];
	}
}

float VecN::getElement(int i, float def) const {
	if (i>=dimensions) {
		return def;
	}
	return elements[i];
}

void VecN::setElement(int i, float val) {
	elements[i] = val;
}
int VecN::getDimensions() const {
	return dimensions;
}

VecN VecN::operator+(const VecN &other) const {
	int dims = std::max(getDimensions(), other.getDimensions());
	VecN ret(dims);
	for (int i = 0;i<dims;i++) {
		ret.setElement(i, getElement(i, 0)+other.getElement(i, 0));
	}
	return ret;
}

float VecN::operator*(const VecN &other) const {
	int dims = std::max(getDimensions(), other.getDimensions());
	float ret = 0;
	for (int i = 0;i<dims;i++) {
		ret+=elements[i]*other.elements[i];
	}
	return ret;
}

VecN VecN::operator*(float other) const {
	int dims = getDimensions();
	VecN ret(dims);
	for (int i = 0;i<dims;i++) {
		ret.setElement(i, elements[i]*other);
	}
	return ret;
}

float VecN::operator [](int i) const {
	return elements[i];
}

void VecN::operator=(const VecN &v) {
	dimensions = v.dimensions;
	if (elements!=0) {
		delete elements;
	}
	elements = new float[dimensions];
	for (int i = 0;i<dimensions;i++) {
		elements[i] = v.elements[i];
	}
}

std::string VecN::toString() const {
	std::stringstream ret;
	ret << "[";
	for (int i = 0;i<dimensions;i++) {
		ret << elements[i];
		if (i!=dimensions-1) {
			ret << ", ";
		}
	}
	ret << "]";
	return ret.str();
}

VecN::~VecN() {
	if (elements!=0) {
		delete elements;
	}
}
