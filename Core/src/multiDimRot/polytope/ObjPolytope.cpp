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
			std::vector<std::string> words = util::splitAtWords(line.substr(2), ' ');
			if (words.size()!=dims) {
				throw "Invalid input in OBJ: vertex element count of \"v\" differs from dimension";
			}
			for (unsigned int i = 0;i<words.size();i++) {
				next[i] = util::toFloat(words[i]);
			}
			vertices.push_back(next);
		} else if (line.find("dims ")==0) {
			dims = util::toInt(line.substr(5));
			if (dims<=0) {
				throw "Dimension count can not be less than 1 (OBJ)";
			}
		} else if (line.find("e ")==0) {
			std::vector<std::string> words = util::splitAtWords(line.substr(2), ' ');
			if (words.size()!=2) {
				throw "Invalid edge entry for OBJ";
			}
			Edge e;
			e.start = util::toInt(words[0])-1;
			e.end = util::toInt(words[1])-1;
			edges.push_back(e);
		} else if (line.find("vn ")==0) {
			VecN next(dims);
			std::vector<std::string> words = util::splitAtWords(line.substr(3), ' ');
			if (words.size()!=dims) {
				throw "Invalid input in OBJ: normal element count of \"v\" differs from dimension";
			}
			for (unsigned int i = 0;i<words.size();i++) {
				next[i] = util::toFloat(words[i]);
			}
			normals.push_back(next);
		} else if (line.find("f ")==0) {
			std::vector<std::string> words = util::splitAtWords(line.substr(2), ' ');
			std::vector<int> vert(words.size());
			std::vector<int> norm(words.size());
			for (int i = 0;i<words.size();i++) {
				std::vector<std::string> innerWords = util::splitAtWords(words[i], '/');
				if (innerWords.size()==2) {
					vert[i] = util::toFloat(innerWords[0]);
					norm[i] = util::toFloat(innerWords[1]);
				} else if (innerWords.size()==3) {
					vert[i] = util::toFloat(innerWords[0]);
					norm[i] = util::toFloat(innerWords[2]);
				} else {
					throw "Invalid input in OBJ: face data must contain 1 or 2 slashes!";
				}
			}
			for (int i = 2;i<vert.size();i++) {
				//0, i, i-1
				faces.push_back(Triangle());
				int id = faces.size()-1;
				faces[id].normals[0] = norm[0];
				faces[id].normals[1] = norm[i-1];
				faces[id].normals[2] = norm[i];
				faces[id].vertices[0] = vert[0];
				faces[id].vertices[1] = vert[i-1];
				faces[id].vertices[2] = vert[i];
			}
		}
	}
}

ObjPolytope::~ObjPolytope() {}

const std::vector<VecN>& ObjPolytope::getVertices() const {
	return vertices;
}

const std::vector<Edge>& ObjPolytope::getEdges() const {
	return edges;
}

int ObjPolytope::getDimensions() const {
	return dims;
}

const std::vector<Triangle>& ObjPolytope::getFaces() const {
	return faces;
}
const std::vector<VecN>& ObjPolytope::getNormals() const {
	return normals;
}

void ObjPolytope::update() {}

void Polytope::writeObj(std::ostream* out, const MatrixNxN &apply) const {
	int dims = getDimensions();
	*out << "dims " << dims << "\n";
	const std::vector<VecN> &verticesOld = getVertices();
	std::vector<VecN> vertices;
	apply.applyMass(verticesOld, vertices);
	for (unsigned int i = 0;i<vertices.size();i++) {
		*out << "v ";
		VecN &curr = vertices[i];
		for (int j = 0;j<dims;j++) {
			*out << curr.getElement(j, 0);
			if (j<dims-1) {
				*out << " ";
			}
		}
		*out << "\n";
	}
	*out << "\n";
	const std::vector<Edge> &edges = getEdges();
	for (unsigned int i = 0;i<edges.size();i++) {
		*out << "e ";
		Edge e = edges[i];
		*out << e.start << " " << e.end << "\n";
	}
	*out << "\n";
	const std::vector<VecN> &normalsOld = getNormals();
	std::vector<VecN> normals;
	apply.applyInvTMass(normalsOld, normals);
	for (unsigned int i = 0;i<normals.size();i++) {
		*out << "vn ";
		VecN &curr = normals[i];
		for (int j = 0;j<dims;j++) {
			*out << curr.getElement(j, 0);
			if (j<dims-1) {
				*out << " ";
			}
		}
		*out << "\n";
	}
	*out << "\n";
	const std::vector<Triangle> &faces = getFaces();
	for (unsigned int i = 0;i<faces.size();i++) {
		const Triangle &curr = faces[i];
		*out << "f ";
		for (int j = 0;j<3;j++) {
			*out << curr.vertices[j] << "//" << curr.normals[j];
			if (j<2) {
				*out << " ";
			}
		}
		*out << "\n";
	}
}
