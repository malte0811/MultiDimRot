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
#include <cmath>

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
int VecN::getDimensions() const {
	return dimensions;
}

void VecN::scaleToLength(float length, bool ignoreLast) {
	float factor = length/getLength();
	int max = ignoreLast?dimensions-1:dimensions;
	for (int i = 0;i<max;i++) {
		elements[i]*=factor;
	}
}

float VecN::getLength() const {
	float sum = 0;
	for (int i = 0;i<dimensions;i++) {
		sum+=elements[i]*elements[i];
	}
	return std::sqrt(sum);
}

VecN VecN::operator+(const VecN &other) const {
	int dims = std::max(getDimensions(), other.getDimensions());
	VecN ret(dims);
	for (int i = 0;i<dims;i++) {
		ret.elements[i] =  getElement(i, 0)+other.getElement(i, 0);
	}
	return ret;
}

float VecN::operator*(const VecN &other) const {
	//int dims = std::min(getDimensions(), other.getDimensions());
	float ret = 0;
	for (int i = 0;i<dimensions;i++) {
		ret+=elements[i]*other.elements[i];
	}
	return ret;
}

VecN VecN::operator*(float other) const {
	int dims = getDimensions();
	VecN ret(dims);
	for (int i = 0;i<dims;i++) {
		ret.elements[i] = elements[i]*other;
	}
	return ret;
}

float& VecN::operator [](const int &i) {
	return elements[i];
}

const float& VecN::operator [](const int &i) const {
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
		ret << (std::abs(elements[i])<.000001?0:elements[i]);
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
