/*******************************************************************************
 * This file is part of MultiDimRot2.0.
 * Copyright (C) 2016, 2017 malte0811
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

#include <multiDimRot/Util.h>
#include <cmath>
#include <sstream>
#include <string>
#include <vector>
namespace MultiDimRot {
namespace Util {
int toInt(std::string in) {
	std::istringstream tmp(in);
	if (tmp.bad()) {
		throw "Invalid input!";
	}
	int i;
	tmp >> i;
	return i;
}

float toFloat(std::string in) {
	std::istringstream tmp(in);
	if (tmp.bad()) {
		throw "Invalid input!";
	}
	float i = 0;
	tmp >> i;
	return i;
}

std::vector<std::string> splitAtWords(std::string inStr, char split) {
	std::vector<std::string> ret;
	std::stringstream s;
	const char* in = inStr.c_str();
	for (unsigned int i = 0;i<inStr.length()+1;i++) {
		if (i==inStr.length()||in[i]==split) {
			if (s.str().length()>0) {
				ret.push_back(s.str());
				s.str("");
			}
		} else {
			s << in[i];
		}
	}
	return ret;
}

void addFace(std::vector<Polytope::Triangle> &faces, std::vector<Math::VecN> &normals, const std::vector<Math::VecN> &vertices,
		const int &a, const int &b, const int &c, int &id) {
	for (int i = 0;i<3;i++) {
		faces[id].normals[i] = id;
	}
	faces[id].vertices[0] = a;
	faces[id].vertices[1] = b;
	faces[id].vertices[2] = c;
	normals[id] = vertices[a]+vertices[b]+vertices[c];
	normals[id].scaleToLength(1, false);
	id++;
}

void initQuad(std::vector<Polytope::Triangle>& tris, int &startIndex,
		const int &a, const int &b, const int &c, const int &d, const int &normal) {
	tris[startIndex].normals[0] = normal;
	tris[startIndex].normals[1] = normal;
	tris[startIndex].normals[2] = normal;
	tris[startIndex].vertices[0] = a;
	tris[startIndex].vertices[1] = b;
	tris[startIndex].vertices[2] = c;
	tris[startIndex+1].normals[0] = normal;
	tris[startIndex+1].normals[1] = normal;
	tris[startIndex+1].normals[2] = normal;
	tris[startIndex+1].vertices[0] = c;
	tris[startIndex+1].vertices[1] = d;
	tris[startIndex+1].vertices[2] = b;
	startIndex+=2;
}

bool isNearZero(const double d) {
	return std::abs(d)<.00000001;
}

Math::ComplexDouble parseComplex(const std::string& in) {
	char last = in[in.size()-1];
	bool imaginary = last=='I'||last=='i';
	double value = std::strtod(in.c_str(), NULL);
	if (imaginary) {
		return Math::ComplexDouble(0, value);
	} else {
		return Math::ComplexDouble(value);
	}
}
Polytope::Triangle offset(Polytope::Triangle orig, int vOff, int nOff) {
	Polytope::Triangle ret;
	for (int i = 0;i<3;i++) {
		ret.normals[i] = orig.normals[i]+nOff;
		ret.vertices[i] = orig.vertices[i]+vOff;
	}
	return ret;
}
}
}
