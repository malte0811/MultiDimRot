#include <iostream>
#include <fstream>
#include <MatrixNxN.h>
#include <ObjPolytope.h>
#include <Polytope.h>
#include <Util.h>
#include <VecN.h>
#include <vector>
#include <string>
#include <iterator>
#include <sstream>

ObjPolytope::ObjPolytope(std::istream* in) {
	char lineTmp[256];
	dims = 3;//default for reading normal OBJ files
	std::string line;
	while (!(*in).eof()) {
		(*in).getline(lineTmp, 256);
		line = std::string(lineTmp);
		if (line.find("v ")==0) {
			VecN next(dims);
			std::vector<std::string> words = util::splitAtWords(line.substr(2));
			if (words.size()!=dims) {
				throw "Invalid input in OBJ: element count of \"v\" differs from dimension";
			}
			for (unsigned int i = 0;i<words.size();i++) {
				next.setElement(i, util::toFloat(words[i]));
			}
			vertices.push_back(next);
		} else if (line.find("dims ")==0) {
			dims = util::toInt(line.substr(5));
			if (dims<=0) {
				throw "Dimension count can not be less than 1 (OBJ)";
			}
		} else if (line.find("f ")==0) {
			int first = -1;
			int last = -1;
			std::vector<std::string> words = util::splitAtWords(line.substr(2).c_str());
			for (unsigned int i = 0;i<words.size();i++) {
				int slash = words[i].find("/");
				int next = util::toInt(slash>0?words[i].substr(0, slash):words[i])-1;
				if (last>=0) {
					Edge tmp;
					tmp.end = next;
					tmp.start = last;
					edges.push_back(tmp);
				}
				last = next;
				if (first<0) {
					first = next;
				}
			}
			Edge tmp;
			tmp.end = first;
			tmp.start = last;
			edges.push_back(tmp);
		} else if (line.find("e ")==0) {
			std::vector<std::string> words = util::splitAtWords(line.substr(2));
			if (words.size()!=2) {
				throw "Invalid edge entry for OBJ";
			}
			Edge e;
			e.start = util::toInt(words[0])-1;
			e.end = util::toInt(words[1])-1;
			edges.push_back(e);
		}
	}
}

ObjPolytope::~ObjPolytope() {}

std::vector<VecN>& ObjPolytope::getVertices() {
	return vertices;
}

std::vector<Edge>& ObjPolytope::getEdges() {
	return edges;
}

int ObjPolytope::getDimensions() {
	return dims;
}

void ObjPolytope::update() {}
