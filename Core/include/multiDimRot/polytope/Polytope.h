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
#ifndef SRC_MULTIDIMROT_POLYTOPE_H_
#define SRC_MULTIDIMROT_POLYTOPE_H_

#include <multiDimRot/math/MatrixNxN.h>
#include <multiDimRot/math/VecN.h>
#include <array>
#include <iostream>
#include <vector>

namespace MultiDimRot {
namespace Polytope {
struct Edge {
	int start;
	int end;
};
struct Triangle {
	std::array<int, 3> vertices;
	std::array<int, 3> normals;
};

typedef struct Edge Edge;
class Polytope {
public:
	Polytope();
	virtual ~Polytope() = 0;
	virtual const std::vector<Math::VecN>& getVertices() const;
	virtual const std::vector<Math::VecN>& getNormals() const;
	virtual const std::vector<Edge>& getEdges() const;
	virtual const std::vector<Triangle>& getFaces() const;
	/**
	 * The amount of dimensions of this polytope. MUST NOT CHANGE!
	 */
	virtual int getDimensions() const;
	/**
	 * returns whether changes were made to the polytope
	 */
	virtual bool update();
	void writeObj(std::ostream* o, const Math::MatrixNxN &apply) const;
	float getLength(Edge e);
protected:
	std::vector<Math::VecN> vertices;
	std::vector<Math::VecN> normals;
	std::vector<Edge> edges;
	std::vector<Triangle> faces;
	int dimensions = -1;
};
}
}
#endif /* SRC_MULTIDIMROT_POLYTOPE_H_ */
