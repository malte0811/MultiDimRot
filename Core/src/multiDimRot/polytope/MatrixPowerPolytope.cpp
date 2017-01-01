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

#include <MatrixPowerPolytope.h>
#include <Util.h>
#include <iostream>
#include <vector>

using namespace MultiDimRot::Polytope;

MatrixPowerPolytope::MatrixPowerPolytope(const char** in, int size) {
	dimensions = size;
	init = Math::VecN(size);
	base = Math::MatrixNxN(size, false);
	curr = Math::MatrixNxN(size, false);
	for (int i = 0;i<size;i++) {
		for (int j = 0;j<size;j++) {
			base.setElement(i, j, Util::toFloat(in[i*size+j]));
		}
	}
	for (int i = 0;i<size;i++) {
		init[i] = Util::toFloat(in[size*size+i]);
	}
}

MatrixPowerPolytope::~MatrixPowerPolytope() {

}

void MatrixPowerPolytope::update() {
	if (generating) {
		Math::VecN next(dimensions);
		curr.apply(init, next);
		if (nextId>0&&vertices[nextId-1]==next) {
			std::cout << "finished generating matrix power polytope\n";
			generating = false;
			return;
		}
		vertices.push_back(next);
		if (nextId>0) {
			Edge e;
			e.start = nextId-1;
			e.end = nextId;
			edges.push_back(e);
		}
		nextId++;
		curr *= base;
	}
}
