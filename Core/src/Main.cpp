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

#include <boost/thread.hpp>
#include <boost/thread/detail/thread.hpp>
#include <multiDimRot/math/MatrixNxN.h>
#include <multiDimRot/polytope/MatrixPowerPolytope.h>
#include <multiDimRot/polytope/NDCrossPolytope.h>
#include <multiDimRot/polytope/NDCube.h>
#include <multiDimRot/polytope/NDSimplex.h>
#include <multiDimRot/polytope/NDSphere.h>
#include <multiDimRot/polytope/ObjPolytope.h>
#include <multiDimRot/polytope/P24Cell.h>
#include <multiDimRot/polytope/ComplexGraph.h>
#include <multiDimRot/polytope/JoinedPolytope.h>
#include <multiDimRot/Renderer.h>
#include <multiDimRot/Util.h>
#include <SFML/Graphics.hpp>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <multiDimRot/math/ExpressionParser.h>

using namespace MultiDimRot;

void rotAll(std::vector<Math::MatrixNxN> &in, int dim, int a0, int a1, float deg, bool con) {
	for (unsigned int i = 0;i<in.size();i++) {
		if (in[i].getSize()==0) {
			in[i] = Math::MatrixNxN(dim+1);
		}
		in[i].rotate(a0, a1, (con?i:1)*deg);
	}
}

void scale(std::vector<Math::MatrixNxN> &in, int dims, int dim, float val) {
	for (unsigned int i = 0;i<in.size();i++) {
		if (in[i].getSize()==0) {
			in[i] = Math::MatrixNxN(dims+1);
		}
		in[i].scale(val, dim);
	}
}

void translate(std::vector<Math::MatrixNxN> &in, int dims, int dim, float val) {
	for (unsigned int i = 0;i<in.size();i++) {
		if (in[i].getSize()==0) {
			in[i] = Math::MatrixNxN(dims+1);
		}
		in[i].translate(dim, val);
	}
}

void project(std::vector<Math::MatrixNxN> &in, int dims, int dim, float dist) {
	for (unsigned int i = 0;i<in.size();i++) {
		if (in[i].getSize()==0) {
			in[i] = Math::MatrixNxN(dims+1);
		}
		in[i].project(dim, 1/dist);
	}
}

void checkSpace(int i, int length, int required, std::string name) {
	if (i>length-required-1) {
		std::stringstream s;
		s << name << " requires " << required << " parameters";
		throw s.str();
	}
}
//TODO change Util::to* to std::strto*
void parsePolytopeParam(const char* argv[], int &argc, int &pos, Polytope::Polytope* &polyt) {
	if (polyt!=0) {
		throw "Already set a polytope!";
	}
	std::vector<Polytope::Polytope*> parts;
	while (pos<argc) {
		std::string in(argv[pos]);
		if (in.find("--")==0) {
			break;
		}
		if (in=="cube") {
			checkSpace(pos, argc, 1, in);
			parts.push_back(new Polytope::NDCube(Util::toInt(argv[pos+1])));
			pos++;
		} else if (in=="obj") {
			checkSpace(pos, argc, 1, in);
			std::string file = argv[pos+1];
			std::ifstream instream(file.c_str());
			if (!instream.is_open()) {
				throw "File not found/Couldn't open "+file;
			}
			parts.push_back(new Polytope::ObjPolytope(&instream));
			pos++;
		} else if (in=="crossPolytope") {
			checkSpace(pos, argc, 1, in);
			parts.push_back(new Polytope::NDCrossPolytope(Util::toInt(argv[pos+1])));
			pos++;
		} else if (in=="simplex") {
			checkSpace(pos, argc, 1, in);
			parts.push_back(new Polytope::NDSimplex(Util::toInt(argv[pos+1])));
			pos++;
		} else if (in=="24Cell") {
			if (polyt!=0)
				delete polyt;
			parts.push_back(new Polytope::P24Cell());
		} else if (in=="sphere") {
			checkSpace(pos, argc, 2, in);
			parts.push_back(new Polytope::NDSphere(Util::toInt(argv[pos+1]), Util::toInt(argv[pos+2])));
			pos+=2;
		} else if (in=="matPower") {
			// matrix size/dimension count
			checkSpace(pos, argc, 1, in);
			int size = Util::toInt(argv[pos+1]);
			if (size<=0) {
				throw "Matrix size must be greater than 0";
			}
			pos++;
			// matrix (size^2)+initial vector (size)
			int totalSize = size*size+size;
			checkSpace(pos, argc, totalSize, in);
			const char** matStart = argv+pos+1;
			parts.push_back(new Polytope::MatrixPowerPolytope(matStart, size));
			pos += totalSize;
		} else if (in=="cGraph") {
			checkSpace(pos, argc, 7, in);
			Math::ExpressionParser function(argv[pos+1]);
			pos+=2;
			double rStart = Util::toFloat(argv[pos]);
			double rStep = Util::toFloat(argv[pos+1]);
			int rCount = Util::toFloat(argv[pos+2]);
			pos+=3;
			double iStart = Util::toFloat(argv[pos]);
			double iStep = Util::toFloat(argv[pos+1]);
			int iCount = Util::toFloat(argv[pos+2]);
			pos+=2;
			parts.push_back(new Polytope::ComplexGraph(function, rStart, rStep, rCount, iStart, iStep, iCount));
		} else {
			throw "Unknown parameter: "+in;
		}
		pos++;
	}
	if (parts.size()==0) {
		throw "You have to set a polytope!";
	} else if (parts.size()==1) {
		polyt = parts[0];
	} else {
		polyt = new Polytope::JoinedPolytope(parts);
	}
}

void parseMatVecParam(const char* argv[], int &argc, int &pos, int dims, std::vector<Math::MatrixNxN> &mats) {
	while (pos<argc) {
		std::string in(argv[pos]);
		if (in.find("--")==0) {
			return;
		}
		if (in=="cycle") {
			checkSpace(pos, argc, 1, in);
			mats = std::vector<Math::MatrixNxN>(Util::toInt(argv[pos+1]));
			pos++;
		} else if (in=="rotInc") {
			checkSpace(pos, argc, 3, in);
			rotAll(mats, dims, Util::toInt(argv[pos+1]), Util::toInt(argv[pos+2]), Util::toFloat(argv[pos+3]), true);
			pos+=3;
		} else if (in=="rotConst") {
			checkSpace(pos, argc, 3, in);
			rotAll(mats, dims, Util::toInt(argv[pos+1]), Util::toInt(argv[pos+2]), Util::toFloat(argv[pos+3]), false);
			pos+=3;
		} else if (in=="scale") {
			checkSpace(pos, argc, 2, in);
			scale(mats, dims, Util::toInt(argv[pos+1]), Util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="scaleAll") {
			checkSpace(pos, argc, 1, in);
			float val = Util::toFloat(argv[pos+1]);
			for (int i2 = 0;i2<dims;i2++) {
				scale(mats, dims, i2, val);
			}
			pos++;
		} else if (in=="translate")  {
			checkSpace(pos, argc, 2, in);
			translate(mats, dims, Util::toInt(argv[pos+1]), Util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="project") {
			checkSpace(pos, argc, 2, in);
			project(mats, dims, Util::toInt(argv[pos+1]), Util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="projectAll") {
			float dist = Util::toFloat(argv[pos+1]);
			for (int d = dims;d>=3;d--) {
				project(mats, dims, d, dist);
			}
			pos++;
		} else {
			throw "Unknown parameter: "+in;
		}
		pos++;
	}
}

void parseMatParam(const char* argv[], int &argc, int &pos, int dims, Math::MatrixNxN &mat) {
	if (mat.getSize()==0) {
		mat = Math::MatrixNxN(dims+1);
	}
	while (pos<argc) {
		std::string in(argv[pos]);
		if (in.find("--")==0) {
			return;
		}
		if (in=="rotate") {
			checkSpace(pos, argc, 3, in);
			mat.rotate(Util::toInt(argv[pos+1]), Util::toInt(argv[pos+2]), Util::toFloat(argv[pos+3]));
			pos+=3;
		} else if (in=="scale") {
			checkSpace(pos, argc, 2, in);
			mat.scale(Util::toFloat(argv[pos+2]), Util::toInt(argv[pos+1]));
			pos+=2;
		} else if (in=="scaleAll") {
			checkSpace(pos, argc, 1, in);
			mat.scale(Util::toFloat(argv[pos+1]));
			pos++;
		} else if (in=="translate")  {
			checkSpace(pos, argc, 2, in);
			mat.translate(Util::toInt(argv[pos+1]), Util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="project") {
			checkSpace(pos, argc, 2, in);
			mat.project(Util::toInt(argv[pos+1]), 1/Util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="projectAll") {
			float dist = Util::toFloat(argv[pos+1]);
			for (int d = dims;d>=3;d--) {
				mat.project(d, 1/dist);
			}
			pos++;
		} else {
			throw "Unknown parameter: "+in;
		}
		pos++;
	}
}

void parseMisc(const char* argv[], int argc, int &pos, bool* renderType, int &threadCount) {
	while (pos<argc) {
		std::string in(argv[pos]);
		if (in.find("--")==0) {
			return;
		}
		if (in=="renderType") {
			checkSpace(pos, argc, 1, in);
			pos++;
			const char* param = argv[pos];
			int length = std::strlen(param);
			if (length!=3) {
				throw "renderType has to be 3 characters long";
			}
			for (int i = 0;i<3;i++) {
				char curr = param[i];
				switch (curr) {
				case '0':
					renderType[i] = false;
					break;
				case '1':
					renderType[i] = true;
					break;
				default:
					throw "renderType characters have to be \'0\' and \'1\'";
				}
			}
		} else if (in=="threadCount") {
			checkSpace(pos, argc, 1, in);
			pos++;
			std::string param(argv[pos]);
			threadCount = Util::toInt(param);
		}
		pos++;
	}
}

void parseArgs(int argc, const char* argv[], Polytope::Polytope* &polyt, std::vector<Math::MatrixNxN> &startMats,
		Math::MatrixNxN &powerMat, std::vector<Math::MatrixNxN> &endMats, int &dims, bool renderType[], int& threadCount) {
	int i = 1;
	while (i<argc) {
		std::string in(argv[i]);
		i++;
		if (in=="--polytope") {
			if (polyt!=0) {
				throw "Can't use \"--polytope\" more than once";
			}
			parsePolytopeParam(argv, argc, i, polyt);
		} else if (in=="--startMats") {
			if (polyt==0) {
				throw "No polytope specified";
			}
			parseMatVecParam(argv, argc, i, polyt->getDimensions(), startMats);
		} else if (in=="--powerMat") {
			if (polyt==0) {
				throw "No polytope specified";
			}
			parseMatParam(argv, argc, i, polyt->getDimensions(), powerMat);
		} else if (in=="--endMats") {
			if (polyt==0) {
				throw "No polytope specified";
			}
			parseMatVecParam(argv, argc, i, polyt->getDimensions(), endMats);
		} else if (in=="--misc") {
			parseMisc(argv, argc, i, renderType, threadCount);
		} else {
			throw "Unknown parameter: "+in;
		}

	}
	if (polyt==0) {
		throw "No polytope specified";
	}
	dims = polyt->getDimensions();
}

void initDefault(Polytope::Polytope* &polyt, std::vector<Math::MatrixNxN> &startMats,
		Math::MatrixNxN &powerMat, std::vector<Math::MatrixNxN> &endMats, int &dims, bool renderType[]) {
//	polyt = new Polytope::ComplexGraph(Math::ExpressionParser("ln(x)"), -10, 1, 21, -10, 1, 21);
	polyt = new Polytope::NDCube(4);
	dims = polyt->getDimensions();
	endMats = std::vector<Math::MatrixNxN>(1, Math::MatrixNxN(dims+1));
	endMats[0].scale(.5);
	for (int j = dims;j>2;j--) {
		endMats[0].project(j, .2);
	}
	powerMat = Math::MatrixNxN(dims+1);
	startMats = std::vector<Math::MatrixNxN>(0, Math::MatrixNxN(dims+1));
	//	for (int j = 0;j<dims-1;j++) {
	//		for (int i = j+1;i<dims;i++) {
	//			powerMat.rotate(j, i, 1);
	//			//rotAll(startMats, dims, j, i, 1, true);
	//		}
	//	}
	powerMat.rotate(0, 2, 1);
	powerMat.rotate(1, 3, 1);
	renderType[0] = true;
	renderType[1] = true;
	renderType[2] = true;
}
int main(int argc, const char* argv[]) {
	Polytope::Polytope* polyt = 0;
	std::vector<Math::MatrixNxN> startMats;
	Math::MatrixNxN power;
	std::vector<Math::MatrixNxN> endMats;
	int dims;
	bool renderType[] = {false, false, false};
	int threadCount = boost::thread::hardware_concurrency();
	try {
		if (argc>1) {
			parseArgs(argc, argv, polyt, startMats, power, endMats, dims, renderType, threadCount);
		} else {
			initDefault(polyt, startMats, power, endMats, dims, renderType);
		}
		Render::Renderer r(polyt, startMats, power, endMats, renderType, threadCount);
		r.render();
	} catch (const char* c) {
		std::cerr<<c<<"\n";
	} catch (const std::string &c) {
		std::cerr<<c<<"\n";
	}
	return 0;
}
