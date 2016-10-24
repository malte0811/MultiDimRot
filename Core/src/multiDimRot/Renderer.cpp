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
#include <Renderer.h>
#include <vector>
#include <Polytope.h>
#include <MatrixNxN.h>
#include <SFML/Graphics.hpp>
#include <fstream>
#include <iostream>
#include <GL/glew.h>
#include <cmath>

Renderer::Renderer(Polytope* &p, std::vector<MatrixNxN> &start, MatrixNxN &power, std::vector<MatrixNxN> &end, const bool rt[]):
startMats(start), powerMat(power), endMats(end), dims(p->getDimensions()) {
	polyt = p;
	renderType = rt;
}

Renderer::~Renderer() {
	//delete polyt;
}
void Renderer::init_resources() {
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
			"uniform vec3 colorMultiplier;"
			"void main(void) {"
			"  gl_FragColor = vec4(color*colorMultiplier[0], color*colorMultiplier[1], color*colorMultiplier[2], 1);"
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

	uniform_baseColor = glGetUniformLocation(program, "colorMultiplier");
	if (uniform_baseColor == -1) {
		throw "could not bind uniform colorMultiplier";
	}
	glEnable(GL_DEPTH_TEST);
	glDepthFunc(GL_LEQUAL);
	glDepthMask(GL_TRUE);
}

inline void Renderer::renderFaces(const std::vector<Triangle> &faces, const VecN &light) {
	int nSize = transformedNormals.size();
	for (int i = 0;i<nSize;i++) {
		transformedNormals[i].scaleToLength(1, true);
		float val = (light*transformedNormals[i]);
		if (i<brightnessSize) {
			brightness[i] = val;
		} else {
			brightness.push_back(val);
			brightnessSize++;
		}
	}
	//generate faceData
	int fSize = faces.size();
	if (fSize*4*3>dataSize) {//TODO replace 4 by 6 and figure out interleaving
		if (data!=0) {
			delete[] data;
		}
		dataSize = 3*4*fSize;
		data = new GLfloat[dataSize];
	}
	int base = 0;
	for (int i = 0;i<fSize;i++) {
		Triangle curr = faces[i];
		bool skip = true;
		for (int j = 0;j<3;j++) {
			if (brightness[curr.normals[j]]>0) {
				skip = false;
				break;
			}
		}
		if (!skip) {
			for (int j = 0;j<3;j++) {
				const VecN& pos = transformedVertices[curr.vertices[j]];
				float b = brightness[curr.normals[j]];
				data[base] = pos.getElement(0, 0);
				data[base+1] = pos.getElement(1, 0);
				data[base+2] = pos.getElement(2, .5);
				data[base+3] = b;
				base+=4;
			}
		}
	}
	//render faceData
	glVertexAttribPointer(attribute_pos, 4, GL_FLOAT, GL_FALSE, 0, data);
	glUniform3f(uniform_baseColor, 1, 0, 0);
	glDrawArrays(GL_TRIANGLES, 0, base/4);
}

inline void Renderer::renderVertices() {
	if (dataSize<vSize*4) {
		delete[] data;
		data = new GLfloat[vSize*4];
		dataSize = vSize*4;
	}
	for (int i = 0;i<vSize;i++) {
		const VecN& curr = transformedVertices[i];
		data[4*i] = curr.getElement(0, 0);
		data[4*i+1] = curr.getElement(1, 0);
		data[4*i+2] = curr.getElement(2, 0);
		data[4*i+3] = 1;
	}
	glVertexAttribPointer(attribute_pos, 4, GL_FLOAT, GL_FALSE, 0, data);
	glUniform3f(uniform_baseColor, 0, 0, 1);
	int size = 2;
	if (renderType[2]||renderType[1]) {
		size = 4;
	}
	glPointSize(size);
	glDrawArrays(GL_POINTS, 0, vSize);
	glPointSize(1);
}

inline void Renderer::renderEdges(const std::vector<Edge> &edges) {
	int eSize = edges.size();
	if (dataSize<2*4*eSize) {
		delete[] data;
		data = new GLfloat[2*4*eSize];
		dataSize = 2*4*eSize;
	}
	for (int i = 0;i<eSize;i++) {
		const Edge &curr = edges[i];
		const VecN &start = transformedVertices[curr.start];
		const VecN &end = transformedVertices[curr.end];
		data[8*i] = start.getElement(0, 0);
		data[8*i+1] = start.getElement(1, 0);
		data[8*i+2] = start.getElement(2, 0);
		data[8*i+3] = 1;
		data[8*i+4] = end.getElement(0, 0);
		data[8*i+5] = end.getElement(1, 0);
		data[8*i+6] = end.getElement(2, 0);
		data[8*i+7] = 1;

	}
	glVertexAttribPointer(attribute_pos, 4, GL_FLOAT, GL_FALSE, 0, data);
	glUniform3f(uniform_baseColor, 0, 1, 0);
	if (renderType[2])
		glLineWidth(2);
	glDrawArrays(GL_LINES, 0, eSize*2);
	glLineWidth(1);
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
	light[dims-1] = -1;
	std::vector<float> brightnesses(0, 0);
	while (window.isOpen()) {
		c.restart();
		polyt->update();
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
		const std::vector<Triangle>& tris = polyt->getFaces();
		const std::vector<Edge> &edges = polyt->getEdges();
		const std::vector<VecN>& verticesOld = polyt->getVertices();
		const std::vector<VecN>& normalsOld = polyt->getNormals();
		curr.applyMass(verticesOld, transformedVertices);
		curr.applyInvTMass(normalsOld, transformedNormals);
		vSize = transformedVertices.size();

		// scale z
		float minZ = 0;
		float maxZ = 0;
		for (int i = 0;i<vSize;i++) {
			VecN& curr = transformedVertices[i];
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
				transformedVertices[i][2] = (transformedVertices[i][2]-minZ)/diff;
			}
		}

		glClearColor(0, 0, 0, 1);
		glClearDepth(1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glUseProgram(program);
		glEnableVertexAttribArray(attribute_pos);

		if (renderType[0]) {
			renderVertices();
		}
		if (renderType[1]) {
			renderEdges(edges);
		}
		if (renderType[2]) {
			renderFaces(tris, light);
		}

		glDisableVertexAttribArray(attribute_pos);

		if (outFile.size()>0) {
			window.pushGLStates();
			window.draw(text);
			window.popGLStates();
		}
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
					polyt->writeObj(&out, id);
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
	std::cout << "Vertex count: " << transformedVertices.size() << ", face count: " << polyt->getFaces().size() << ", normal count: " << transformedNormals.size() << "\n";
	std::cout << "Avg. processing time: " << sum/(float)frameId << " us, Avg. short-circuit FPS: " << (1000000.0*frameId)/sum << "\n";
}
