#include <Renderer.h>
#include <vector>
#include <Polytope.h>
#include <MatrixNxN.h>
#include <SFML/Graphics.hpp>
#include <fstream>
#include <iostream>
#include <GL/glew.h>
#include <cmath>

Renderer::Renderer(Polytope* &p, std::vector<MatrixNxN> &start, MatrixNxN &power, std::vector<MatrixNxN> &end):
startMats(start), powerMat(power), endMats(end) {
	polyt = p;
}

Renderer::~Renderer() {
	//delete polyt;
}
GLuint program;
GLint attribute_pos;
void init_resources(void) {
	GLint compile_ok = GL_FALSE, link_ok = GL_FALSE;

	GLuint vs = glCreateShader(GL_VERTEX_SHADER);
	const char *vs_source =
			"#version 330\n"
			"attribute vec4 pos;"
			"varying float color;"
			"void main(void) {"
			"  gl_Position = vec4(pos.x, pos.y, pos.z, 1.0);"
			"  color = pos.w;"
			"}";
	glShaderSource(vs, 1, &vs_source, NULL);
	glCompileShader(vs);
	glGetShaderiv(vs, GL_COMPILE_STATUS, &compile_ok);
	if (!compile_ok) {
		throw "Error in vertex shader";
	}
	GLuint fs = glCreateShader(GL_FRAGMENT_SHADER);
	const char *fs_source =
			"#version 330\n"
			"varying float color;"
			"void main(void) {"
			"  gl_FragColor = vec4(color, 0, 0, 1);"
			"  gl_FragDepth = gl_FragCoord.z;"
			"}";
	glShaderSource(fs, 1, &fs_source, NULL);
	glCompileShader(fs);
	glGetShaderiv(fs, GL_COMPILE_STATUS, &compile_ok);
	if (!compile_ok) {
		throw "Error in fragment shader";
	}
	program = glCreateProgram();
	glAttachShader(program, vs);
	glAttachShader(program, fs);
	glLinkProgram(program);
	glGetProgramiv(program, GL_LINK_STATUS, &link_ok);
	if (!link_ok) {
		throw "Error in glLinkProgram";
	}
	const char* attribute_name = "pos";
	attribute_pos = glGetAttribLocation(program, attribute_name);
	if (attribute_pos == -1) {
		throw "Could not bind attribute pos";
	}
	glEnable(GL_DEPTH_TEST);
	glDepthFunc(GL_LEQUAL);
	glDepthMask(GL_TRUE);
}



void Renderer::render() {
	int dims = polyt->getDimensions();
	int width = 500;
	int height = 500;
	int frameId = 0;
	sf::Clock c;
	sf::RenderWindow window(sf::VideoMode(width, height), "MultiDimRot", sf::Style::Default, sf::ContextSettings(24));
	window.setVerticalSyncEnabled(true);
	GLenum glew_status = glewInit();
	if (glew_status != GLEW_OK) {
		throw glewGetErrorString(glew_status);
	}

	init_resources();
	long int sum = 0;
	std::vector<VecN> vertices;
	std::vector<VecN> normals;
	const MatrixNxN id(dims+1);
	MatrixNxN curr = id;
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
	bool doCycle = true;
	VecN light(dims);
	light.setElement(dims-1, -1);
	GLfloat* faceData = 0;
	int facelength = -1;
	std::vector<float> brightnesses(0, 0);
	int brightnessSize = brightnesses.size();
	while (window.isOpen()) {
		c.restart();
		(*polyt).update();
		if (doCycle) {
			curr = startSize>0?startMats[frameId%startSize]:id;
			if (powerMat.getSize()>0) {
				curr *= power;
				power *= powerMat;
			}
			if (endSize>0) {
				curr *= endMats[frameId%endSize];
			}
			frameId++;
		}
		window.clear();
		std::vector<Triangle>& tris = polyt->getFaces();
		std::vector<VecN>& verticesOld = polyt->getVertices();
		std::vector<VecN>& normalsOld = polyt->getNormals();
		curr.applyMass(verticesOld, vertices);
		curr.applyInvTMass(normalsOld, normals);
		int nSize = normals.size();
		int fSize = tris.size();
		int vSize = vertices.size();
		for (int i = 0;i<nSize;i++) {
			normals[i].scaleToLength(1, true);
			if (i<brightnessSize) {
				brightnesses[i] = light*normals[i];
			} else {
				brightnesses.push_back(light*normals[i]);
				brightnessSize++;
			}
		}
		// scale z
		float minZ = 0;
		float maxZ = 0;
		for (int i = 0;i<vSize;i++) {
			VecN& curr = vertices[i];
			float last = curr[dims];
			if (dims>0) {
				curr[0] /= last;
				if (dims>1) {
					curr[1] /= last;
					if (dims>2) {
						float z = curr[2]/last;
						curr[2] = z;
						if (minZ>z) {
							minZ = z;
						}
						if (maxZ<z) {
							maxZ = z;
						}
					}
				}
			}
		}
		float diff = maxZ-minZ;
		//map depth to 1;2
		if (dims>2) {
			for (int i = 0;i<vSize;i++) {
				vertices[i][2] = (vertices[i][2]-minZ)/diff;
			}
		}
		//generate faceData
		if (fSize*4*3>facelength) {//TODO replace 4 by 6 and figure out interleaving
			delete[] faceData;
			facelength = 3*4*fSize;
			faceData = new GLfloat[facelength];
		}
		int base = 0;
		for (int i = 0;i<fSize;i++) {
			Triangle curr = tris[i];
			for (int j = 0;j<3;j++) {
				VecN& pos = vertices[curr.vertices[j]];
				float brightness = brightnesses[curr.normals[j]];
				if (brightness>0) {
					faceData[base] = pos.getElement(0, 0);
					faceData[base+1] = pos.getElement(1, 0);
					faceData[base+2] = pos.getElement(2, .5);
					faceData[base+3] = brightness;
					base+=4;
				}
			}
		}
		//render faceData
		glClearColor(0, 0, 0, 1);
		glClearDepth(1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glUseProgram(program);
		glEnableVertexAttribArray(attribute_pos);
		glVertexAttribPointer(attribute_pos, 4, GL_FLOAT, GL_FALSE, 0, faceData);
		glDrawArrays(GL_TRIANGLES, 0, base/4);
		glDisableVertexAttribArray(attribute_pos);

		window.draw(text);
		window.display();
		sf::Time t = c.getElapsedTime();

		sum+=t.asMicroseconds();
		sf::sleep(sf::microseconds(25000-t.asMicroseconds()));


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
				} else if (event.key.code==sf::Keyboard::I) {
					doCycle = !doCycle;
				}
			} else if (event.type==sf::Event::TextEntered) {
				if (event.text.unicode>20&&event.text.unicode!='i') {
					outFile+=event.text.unicode;
					text.setString(outFile);
				}
			} else if (event.type==sf::Event::Resized) {
				glViewport(0, 0, event.size.width, event.size.height);
			}
		}
	}
	std::cout << "Avg. processing time: " << sum/(float)frameId << " us, Avg. short-circuit FPS: " << (1000000.0*frameId)/sum << "\n";
}
