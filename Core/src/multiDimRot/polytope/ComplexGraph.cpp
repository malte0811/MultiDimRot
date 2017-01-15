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

#include <ComplexGraph.h>
#include <Util.h>


namespace MultiDimRot {
namespace Polytope {

ComplexGraph::ComplexGraph(std::function<Math::ComplexDouble(const Math::ComplexDouble)> f,
		double rMin, double rStep, int rCount,
		double iMin, double iStep, int iCount) {
	vertices = std::vector<Math::VecN>(rCount*iCount, Math::VecN(4));
	faces = std::vector<Triangle>(4*(rCount-1)*(iCount-1));
	edges = std::vector<Edge>(rCount*(iCount-1)+(rCount-1)*iCount);
	normals = std::vector<Math::VecN>(2*rCount*iCount, Math::VecN(4));
	dimensions = 4;
	Math::ComplexDouble** values = new Math::ComplexDouble*[rCount];
	for (int i = 0;i<rCount;i++) {
		values[i] = new Math::ComplexDouble[iCount];
	}
	//init vertices
	int vPos = 0;
	for (int rPos = 0;rPos<rCount;rPos++) {
		for (int iPos = 0;iPos<iCount;iPos++) {
			double r = rMin+rPos*rStep;
			double i = iMin+iPos*iStep;
			vertices[vPos][0] = r;
			vertices[vPos][1] = i;
			values[rPos][iPos] = f(Math::ComplexDouble(r, i));
			vertices[vPos][2] = values[rPos][iPos].getReal();
			vertices[vPos][3] = values[rPos][iPos].getImaginary();
			vPos++;
		}
	}
	//init edges to form a 2D-mesh
	int eId = 0;
	for (int r = 0;r<rCount;r++) {
		for (int i = 0;i<iCount;i++) {
			int pos = r*iCount+i;
			if (r<rCount-1) {
				edges[eId].start = pos;
				edges[eId].end = pos+iCount;
				eId++;
			}
			if (i<iCount-1) {
				edges[eId].start = pos;
				edges[eId].end = pos+1;
				eId++;
			}
		}
	}
	//init faces and normals to form a bent plane
	//faces
	int fId = 0;
	for (int r = 0;r<rCount-1;r++) {
		for (int i = 0;i<iCount-1;i++) {
			int pos = r*iCount+i;
			Triangle* curr = &faces[fId];
			curr->vertices[0] = pos;
			curr->vertices[1] = pos+1;
			curr->vertices[2] = pos+iCount;
			curr->normals = curr->vertices;
			faces[fId+1] = faces[fId];
			for (int k = 0;k<3;k++) {
				faces[fId+1].normals[k] += vPos;
			}
			fId+=2;
			curr = &faces[fId];
			curr->vertices[0] = pos+1;
			curr->vertices[1] = pos+iCount;
			curr->vertices[2] = pos+iCount+1;
			curr->normals = curr->vertices;
			faces[fId+1] = faces[fId];
			for (int k = 0;k<3;k++) {
				faces[fId+1].normals[k] += vPos;
			}
			fId+=2;
		}
	}
	//normals
	for (int r = 0;r<rCount;r++) {
		for (int i = 0;i<iCount;i++) {
			int pos = r*iCount+i;
			double dR = 0;
			int rSamples = 0;
			double dI = 0;
			int iSamples = 0;
			Math::ComplexDouble val = values[r][i];
			if (r<rCount-1) {
				dR = values[r+1][i].getReal()-val.getReal();
				rSamples++;
			}
			if (r>0) {
				dR += val.getReal()-values[r-1][i].getReal();
				rSamples++;
			}
			dR /= rStep*rSamples;
			if (i<iCount-1) {
				dI = values[r][i+1].getImaginary()-val.getImaginary();
				iSamples++;
			}
			if (i>0) {
				dI += val.getImaginary()-values[r][i-1].getImaginary();
				iSamples++;
			}
			dI /= iStep*iSamples;

			//TODO handle dR==0||dI==0
			normals[pos][0] = 1;
			normals[pos][1] = 1;
			normals[pos][2] = Util::isNearZero(dI)?-100000:-1/dR;
			normals[pos][3] = Util::isNearZero(dI)?-100000:-1/dI;
			normals[pos+vPos] = -normals[pos];
		}
	}
	Util::del2DimArray(values, rCount);
}

ComplexGraph::~ComplexGraph() {}

} /* namespace Polytope */
} /* namespace MultiDimRot */
