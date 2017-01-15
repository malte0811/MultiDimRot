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
#include <P24Cell.h>
#include <vector>
#include <iostream>
using namespace MultiDimRot::Polytope;
P24Cell::P24Cell() {
	vertices = std::vector<Math::VecN>(24, Math::VecN(4));
	edges = std::vector<Edge>(96);
	dimensions = 4;
	int edgeId = 0;
	for (int i = 0;i<16;i++) {
		for (int digit = 0;digit<4;digit++) {
			int tmp = i&(1<<digit);
			vertices[i][digit] = tmp>0?.5:-.5;
			//edge
			if (tmp==0) {
				edges[edgeId].start = i;
				edges[edgeId].end = i+(1<<digit);
				edgeId++;
			}
		}
	}

	for (int i = 0;i<4;i++) {
		//vertex
		vertices[16+2*i][i] = 1;
		//edges
		for (int j = 0;j<8;j++) {
			Edge e;
			e.start = 16+2*i;
			e.end = 1<<i;
			for (int k = 0;k<3;k++) {
				if (((j>>k)&1)>0) {
					int digit = (k<i?k:(k+1));
					e.end |= 1<<digit;
				}
			}
			edges[edgeId] = e;
			edgeId++;
		}
		//vertex
		vertices[17+2*i][i] = -1;
		//edges
		for (int j = 0;j<8;j++) {
			Edge e;
			e.start = 17+2*i;
			e.end = 0;
			for (int k = 0;k<3;k++) {
				if (((j>>k)&1)>0) {
					int digit = (k<i?k:(k+1));
					e.end |= 1<<digit;
				}
			}
			edges[edgeId] = e;
			edgeId++;
		}
	}
}

P24Cell::~P24Cell() {}
