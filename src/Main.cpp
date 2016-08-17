#include <SFML/Graphics.hpp>
#include <VecN.h>
#include <MatrixNxN.h>
#include <Solid.h>
#include <NDCube.h>
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <cmath>
#include <ObjSolid.h>
#include <Util.h>
#include <NDCrossPolytope.h>
#include <NDSimplex.h>


void rotAll(std::vector<MatrixNxN> &in, int dim, int a0, int a1, float deg, bool con) {
	for (unsigned int i = 0;i<in.size();i++) {
		if (in[i].getSize()==0) {
			in[i] = MatrixNxN(dim+1);
		}
		in[i].rotate(a0, a1, (con?i:1)*deg);
	}
}

void scale(std::vector<MatrixNxN> &in, int dims, int dim, float val) {
	for (unsigned int i = 0;i<in.size();i++) {
		if (in[i].getSize()==0) {
			in[i] = MatrixNxN(dims+1);
		}
		in[i].scale(val, dim);
	}
}

void translate(std::vector<MatrixNxN> &in, int dims, int dim, float val) {
	for (unsigned int i = 0;i<in.size();i++) {
		if (in[i].getSize()==0) {
			in[i] = MatrixNxN(dims+1);
		}
		in[i].translate(dim, val);
	}
}

void project(std::vector<MatrixNxN> &in, int dims, int dim, float dist) {
	for (unsigned int i = 0;i<in.size();i++) {
		if (in[i].getSize()==0) {
			in[i] = MatrixNxN(dims+1);
		}
		in[i].project(dim, 1/dist);
	}
}
void prepForRender(std::vector<MatrixNxN> &in, int dims) {
	for (unsigned int i = 0;i<in.size();i++) {
		if (in[i].getSize()==0) {
			in[i] = MatrixNxN(dims+1);
		}
		in[i].prepareForRender();
	}
}

void checkSpace(int i, int length, int required, std::string name) {
	if (i>length-required-1) {
		std::stringstream s;
		s << name << " requires " << required << " parameters";
		throw s.str();
	}
}
void parseSolidParam(const char* argv[], int &argc, int &pos, Solid* &solid) {
	while (pos<argc) {
		std::string in(argv[pos]);
		if (in.find("--")==0) {
			return;
		}
		if (in=="cube") {
			checkSpace(pos, argc, 1, in);
			if (solid!=0)
				delete solid;
			solid = new NDCube(util::toInt(argv[pos+1]));
			pos++;
		} else if (in=="obj") {
			checkSpace(pos, argc, 1, in);
			if (solid!=0)
				delete solid;
			std::string file = argv[pos+1];
			std::ifstream instream(file.c_str());
			if (!instream.is_open()) {
				throw "File not found/Couldn't open "+file;
			}
			solid = new ObjSolid(&instream);
			pos++;
		} else if (in=="crossPolytope") {
			checkSpace(pos, argc, 1, in);
			if (solid!=0)
				delete solid;
			solid = new NDCrossPolytope(util::toInt(argv[pos+1]));
			pos++;
		} else if (in=="simplex") {
			checkSpace(pos, argc, 1, in);
			if (solid!=0)
				delete solid;
			solid = new NDSimplex(util::toInt(argv[pos+1]));
			pos++;
		}else {
			throw "Unknown parameter: "+in;
		}
		pos++;
	}
}

void parseMatVecParam(const char* argv[], int &argc, int &pos, int dims, std::vector<MatrixNxN> &mats) {
	while (pos<argc) {
		std::string in(argv[pos]);
		if (in.find("--")==0) {
			return;
		}
		if (in=="cycle") {
			checkSpace(pos, argc, 1, in);
			mats = std::vector<MatrixNxN>(util::toInt(argv[pos+1]));
			pos++;
		} else if (in=="rotInc") {
			checkSpace(pos, argc, 3, in);
			rotAll(mats, dims, util::toInt(argv[pos+1]), util::toInt(argv[pos+2]), util::toFloat(argv[pos+3]), true);
			pos+=3;
		} else if (in=="rotConst") {
			checkSpace(pos, argc, 3, in);
			rotAll(mats, dims, util::toInt(argv[pos+1]), util::toInt(argv[pos+2]), util::toFloat(argv[pos+3]), false);
			pos+=3;
		} else if (in=="scale") {
			checkSpace(pos, argc, 2, in);
			scale(mats, dims, util::toInt(argv[pos+1]), util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="scaleAll") {
			checkSpace(pos, argc, 1, in);
			float val = util::toFloat(argv[pos+1]);
			for (int i2 = 0;i2<dims;i2++) {
				scale(mats, dims, i2, val);
			}
			pos++;
		} else if (in=="translate")  {
			checkSpace(pos, argc, 2, in);
			translate(mats, dims, util::toInt(argv[pos+1]), util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="project") {
			checkSpace(pos, argc, 2, in);
			project(mats, dims, util::toInt(argv[pos+1]), util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="projectAll") {
			float dist = util::toFloat(argv[pos+1]);
			for (int d = dims;d>=3;d--) {
				project(mats, dims, d, dist);
			}
			pos++;
		} else if (in=="prepForRender") {
			prepForRender(mats, dims);
		} else {
			throw "Unknown parameter: "+in;
		}
		pos++;
	}
}

void parseMatParam(const char* argv[], int &argc, int &pos, int dims, MatrixNxN &mat) {
	if (mat.getSize()==0) {
		mat = MatrixNxN(dims+1);
	}
	while (pos<argc) {
		std::string in(argv[pos]);
		if (in.find("--")==0) {
			return;
		}
		if (in=="rotate") {
			checkSpace(pos, argc, 3, in);
			mat.rotate(util::toInt(argv[pos+1]), util::toInt(argv[pos+2]), util::toFloat(argv[pos+3]));
			pos+=3;
		} else if (in=="scale") {
			checkSpace(pos, argc, 2, in);
			mat.scale(util::toInt(argv[pos+1]), util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="scaleAll") {
			checkSpace(pos, argc, 1, in);
			mat.scale(util::toFloat(argv[pos+1]));
			pos++;
		} else if (in=="translate")  {
			checkSpace(pos, argc, 2, in);
			mat.translate(util::toInt(argv[pos+1]), util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="project") {
			checkSpace(pos, argc, 2, in);
			mat.project(util::toInt(argv[pos+1]), util::toFloat(argv[pos+2]));
			pos+=2;
		} else if (in=="projectAll") {
			float dist = util::toFloat(argv[pos+1]);
			for (int d = dims;d>=3;d--) {
				mat.project(d, dist);
			}
			pos++;
		} else {
			throw "Unknown parameter: "+in;
		}
		pos++;
	}
}

void parseArgs(int argc, const char* argv[], Solid* &solid, std::vector<MatrixNxN> &startMats, MatrixNxN &powerMat, std::vector<MatrixNxN> &endMats, int &dims) {
	int i = 1;
	while (i<argc) {
		std::string in(argv[i]);
		i++;
		if (in=="--solid") {
			if (solid!=0) {
				throw "Can't use \"--solid\" more than once";
			}
			parseSolidParam(argv, argc, i, solid);
		} else if (in=="--startMats") {
			if (solid==0) {
				throw "No solid specified";
			}
			parseMatVecParam(argv, argc, i, solid->getDimensions(), startMats);
		} else if (in=="--powerMat") {
			if (solid==0) {
				throw "No solid specified";
			}
			parseMatParam(argv, argc, i, solid->getDimensions(), powerMat);
		} else if (in=="--endMats") {
			if (solid==0) {
				throw "No solid specified";
			}
			parseMatVecParam(argv, argc, i, solid->getDimensions(), endMats);
		} else {
			throw "Unknown parameter: "+in;
		}

	}
	if (solid==0) {
		throw "No solid specified";
	}
	dims = solid->getDimensions();
}

void initDefault(Solid* &solid, std::vector<MatrixNxN> &startMats, MatrixNxN &powerMat, std::vector<MatrixNxN> &endMats, int &dims) {
	dims = 4;
	solid = new NDCube(dims);
	dims = solid->getDimensions();
	endMats = std::vector<MatrixNxN>(1);
	endMats[0] = MatrixNxN(dims+1);
	endMats[0].scale(.5);
	for (int j = dims;j>=3;j--) {
		endMats[0].project(j, .2);
	}
	endMats[0].prepareForRender();
	powerMat = MatrixNxN(dims+1);
	for (int j = 0;j<dims;j++) {
		for (int k = j+1;k<dims;k++) {
			powerMat.rotate(j, k, 1);
		}
	}
}
void renderCycle(Solid* solid, std::vector<MatrixNxN> &startMats, MatrixNxN &powerMat, std::vector<MatrixNxN> &endMats, int dims) {
	int width = 500;
	int height = 500;
	int frameId = 0;
	sf::Clock c;
	sf::RenderWindow window(sf::VideoMode(width, height), "MultiDimRot");
	long int sum = 0;
	std::vector<VecN> vertices;
	MatrixNxN curr;
	MatrixNxN power(dims+1);
	while (window.isOpen()) {
		c.restart();
		(*solid).update();
		curr = startMats.size()>0?startMats[frameId%startMats.size()]:MatrixNxN(dims+1);
		if (powerMat.getSize()>0) {
			curr = power*curr;
			power = powerMat*power;
		}
		if (endMats.size()>0) {
			curr = endMats[frameId%endMats.size()]*curr;
		}
		window.clear();
		std::vector<Edge>& edges = (*solid).getEdges();
		std::vector<VecN>& verticesOld = (*solid).getVertices();
		curr.applyMass(verticesOld, vertices);
		int edgeCount = edges.size();
		sf::VertexArray vb(sf::Lines, 2*edgeCount);
		for (int i = 0;i<edgeCount;i++) {
			Edge currE = edges[i];
			float zStart = (vertices[currE.start]).getElement(dims, 1);
			float zEnd = (vertices[currE.end]).getElement(dims, 1);
			int xStart = (vertices[currE.start]).getElement(0, .5)*width/zStart;
			int yStart = (vertices[currE.start]).getElement(1, .5)*height/zStart;
			int xEnd = (vertices[currE.end]).getElement(0, .5)*width/zEnd;
			int yEnd = (vertices[currE.end]).getElement(1, .5)*height/zEnd;
			vb[2*i].position = sf::Vector2f(xStart, yStart);
			vb[2*i+1].position = sf::Vector2f(xEnd, yEnd);
			vb[2*i].color = sf::Color::Green;
			vb[2*i+1].color = sf::Color::Green;
		}
		window.draw(vb);
		window.display();
		sf::Time t = c.getElapsedTime();

		sum+=t.asMicroseconds();
		sf::sleep(sf::microseconds(25000-t.asMicroseconds()));
		frameId++;


		sf::Event event;
		while (window.pollEvent(event)) {
			if (event.type == sf::Event::Closed) {
				window.close();
			} else if (event.type==sf::Event::KeyPressed && event.key.code==sf::Keyboard::Return) {
				std::cout << "Writing to file\n";
				std::ofstream out("Test.obj");
				solid->writeObj(&out, curr);
				out.flush();
			}
		}
	}
	std::cout << "Avg. processing time: " << sum/(float)frameId << " us, Avg. short-circuit FPS: " << (1000000.0*frameId)/sum << "\n";
}
int main(int argc, const char* argv[]) {
	Solid* solid = 0;
	std::vector<MatrixNxN> startMats;
	MatrixNxN power;
	std::vector<MatrixNxN> endMats;
	int dims;
	try {
		if (argc>1) {
			parseArgs(argc, argv, solid, startMats, power, endMats, dims);
		} else {
			initDefault(solid, startMats, power, endMats, dims);
		}
		renderCycle(solid, startMats, power, endMats, dims);
	} catch (const char* c) {
		std::cerr<<c<<"\n";
	} catch (const std::string &c) {
		std::cerr<<c<<"\n";
	}
	return 0;
}

