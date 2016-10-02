#include <VecN.h>
#include <Polytope.h>
#include <Util.h>
#include <sstream>
#include <iostream>
#include <vector>
#include <string>

int util::toInt(std::string in) {
	std::istringstream tmp(in);
	int i;
	tmp >> i;
	return i;
}

float util::toFloat(std::string in) {
	std::istringstream tmp(in);
	float i = 0;
	tmp >> i;
	return i;
}

std::vector<std::string> util::splitAtWords(std::string inStr, char split) {
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

void util::addFace(std::vector<Triangle> &faces, std::vector<VecN> &normals, const std::vector<VecN> &vertices,
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

void util::initQuad(std::vector<Triangle>& tris, int &startIndex,
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
