/*
 * Util.cpp
 *
 *  Created on: 28.07.2016
 *      Author: malte
 */

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

std::vector<std::string> util::splitAtWords(std::string inStr) {
	std::vector<std::string> ret;
	std::stringstream s;
	const char* in = inStr.c_str();
	for (unsigned int i = 0;i<inStr.length()+1;i++) {
		if (i==inStr.length()||in[i]==' ') {
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
