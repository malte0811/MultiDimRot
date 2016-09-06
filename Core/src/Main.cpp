#include <SFML/Graphics.hpp>
#include <VecN.h>
#include <MatrixNxN.h>
#include <NDCube.h>
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <cmath>
#include <Util.h>
#include <NDCrossPolytope.h>
#include <NDSimplex.h>
#include <ObjPolytope.h>
#include <Polytope.h>
#include <JoinedPolytope.h>
#include <P24Cell.h>


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
void parsePolytopeParam(const char* argv[], int &argc, int &pos, Polytope* &polyt) {
	while (pos<argc) {
		std::string in(argv[pos]);
		if (in.find("--")==0) {
			return;
		}
		if (in=="cube") {
			checkSpace(pos, argc, 1, in);
			if (polyt!=0)
				delete polyt;
			polyt = new NDCube(util::toInt(argv[pos+1]));
			pos++;
		} else if (in=="obj") {
			checkSpace(pos, argc, 1, in);
			if (polyt!=0)
				delete polyt;
			std::string file = argv[pos+1];
			std::ifstream instream(file.c_str());
			if (!instream.is_open()) {
				throw "File not found/Couldn't open "+file;
			}
			polyt = new ObjPolytope(&instream);
			pos++;
		} else if (in=="crossPolytope") {
			checkSpace(pos, argc, 1, in);
			if (polyt!=0)
				delete polyt;
			polyt = new NDCrossPolytope(util::toInt(argv[pos+1]));
			pos++;
		} else if (in=="simplex") {
			checkSpace(pos, argc, 1, in);
			if (polyt!=0)
				delete polyt;
			polyt = new NDSimplex(util::toInt(argv[pos+1]));
			pos++;
		} else if (in=="24Cell") {
			if (polyt!=0)
				delete polyt;
			polyt = new P24Cell();
		} else {
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
			mat.scale(util::toFloat(argv[pos+2]), util::toInt(argv[pos+1]));
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

void parseArgs(int argc, const char* argv[], Polytope* &polyt, std::vector<MatrixNxN> &startMats, MatrixNxN &powerMat, std::vector<MatrixNxN> &endMats, int &dims) {
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
		} else {
			throw "Unknown parameter: "+in;
		}

	}
	if (polyt==0) {
		throw "No polytope specified";
	}
	dims = polyt->getDimensions();
}

void initDefault(Polytope* &polyt, std::vector<MatrixNxN> &startMats, MatrixNxN &powerMat, std::vector<MatrixNxN> &endMats, int &dims) {
	polyt = new P24Cell();
	dims = polyt->getDimensions();
	endMats = std::vector<MatrixNxN>(1);
	endMats[0] = MatrixNxN(dims+1);
	endMats[0].scale(.5);
	for (int j = dims;j>=3;j--) {
		endMats[0].project(j, .5);
	}
	endMats[0].prepareForRender();
	powerMat = MatrixNxN(dims+1);
	startMats = std::vector<MatrixNxN>(360);
	for (int j = 0;j<dims-1;j++) {
		for (int i = j+1;i<dims;i++) {
			//powerMat.rotate(j, i, 1);
			rotAll(startMats, dims, j, i, 1, true);
		}
	}
}
void renderCycle(Polytope* &polyt, std::vector<MatrixNxN> &startMats, MatrixNxN &powerMat, std::vector<MatrixNxN> &endMats, int dims) {
	int width = 500;
	int height = 500;
	int frameId = 0;
	sf::Clock c;
	sf::RenderWindow window(sf::VideoMode(width, height), "MultiDimRot");
	long int sum = 0;
	std::vector<VecN> vertices;
	MatrixNxN curr;
	MatrixNxN power(dims+1);
	sf::Font font;
	if (!font.loadFromFile("courier.ttf")) {
		throw "Could not load font!";
	}
	std::string outFile;
	sf::Text text;
	int size = 20;
	text.setCharacterSize(size);
	text.setColor(sf::Color::White);
	text.setFont(font);
	text.setPosition(10, height-size-5);
	text.setString("");
	int startSize = startMats.size();
	int endSize = endMats.size();
	while (window.isOpen()) {
		c.restart();
		(*polyt).update();
		curr = startSize>0?startMats[frameId%startSize]:MatrixNxN(dims+1);
		if (powerMat.getSize()>0) {
			curr = power*curr;
			power = powerMat*power;
		}
		if (endSize>0) {
			curr = endMats[frameId%endSize]*curr;
		}
		window.clear();
		std::vector<Edge>& edges = (*polyt).getEdges();
		std::vector<VecN>& verticesOld = (*polyt).getVertices();
		curr.applyMass(verticesOld, vertices);
		int edgeCount = edges.size();
		sf::VertexArray vb(sf::Lines, 2*edgeCount);
		for (int i = 0;i<edgeCount;i++) {
			Edge currE = edges[i];
			float zStart = (vertices[currE.start]).getElement(dims, 1);
			float zEnd = (vertices[currE.end]).getElement(dims, 1);
			int xStart = (dims>0?(vertices[currE.start])[0]:.5)*width/zStart;
			int yStart = (dims>1?(vertices[currE.start])[1]:.5)*height/zStart;
			int xEnd = (dims>0?(vertices[currE.end])[0]:.5)*width/zEnd;
			int yEnd = (dims>1?(vertices[currE.end])[1]:.5)*height/zEnd;
			vb[2*i].position = sf::Vector2f(xStart, yStart);
			vb[2*i+1].position = sf::Vector2f(xEnd, yEnd);
			vb[2*i].color = sf::Color::Green;
			vb[2*i+1].color = sf::Color::Green;
		}
		window.draw(vb);
		window.draw(text);
		window.display();
		sf::Time t = c.getElapsedTime();

		sum+=t.asMicroseconds();
		sf::sleep(sf::microseconds(25000-t.asMicroseconds()));
		frameId++;


		sf::Event event;
		while (window.pollEvent(event)) {
			if (event.type == sf::Event::Closed) {
				window.close();
			} else if (event.type==sf::Event::KeyPressed) {
				if (event.key.code==sf::Keyboard::Return) {
					std::ofstream out((outFile+".nobj").c_str());
					curr = power*(startSize>0?startMats[frameId%startSize]:MatrixNxN(dims+1));
					polyt->writeObj(&out, curr);
					out.flush();
					outFile = "";
					text.setString("");
				} else if (event.key.code==sf::Keyboard::BackSpace&&outFile.length()>0) {
					outFile = outFile.substr(0, outFile.length()-1);
					text.setString(outFile);
				}
			} else if (event.type==sf::Event::TextEntered) {
				if (event.text.unicode>20) {
					outFile+=event.text.unicode;
					text.setString(outFile);
				}
			}
		}
	}
	std::cout << "Avg. processing time: " << sum/(float)frameId << " us, Avg. short-circuit FPS: " << (1000000.0*frameId)/sum << "\n";
}
int main(int argc, const char* argv[]) {
	Polytope* polyt = 0;
	std::vector<MatrixNxN> startMats;
	MatrixNxN power;
	std::vector<MatrixNxN> endMats;
	int dims;
	try {
		if (argc>1) {
			parseArgs(argc, argv, polyt, startMats, power, endMats, dims);
		} else {
			initDefault(polyt, startMats, power, endMats, dims);
		}
		renderCycle(polyt, startMats, power, endMats, dims);
	} catch (const char* c) {
		std::cerr<<c<<"\n";
	} catch (const std::string &c) {
		std::cerr<<c<<"\n";
	}
	return 0;
}

