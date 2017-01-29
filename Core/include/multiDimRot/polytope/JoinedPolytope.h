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

#ifndef MULTIDIMROT_POLYTOPE_JOINEDPOLYTOPE_H_
#define MULTIDIMROT_POLYTOPE_JOINEDPOLYTOPE_H_

#include <multiDimRot/math/VecN.h>
#include <multiDimRot/polytope/Polytope.h>
#include <vector>

namespace MultiDimRot {
namespace Polytope {
class JoinedPolytope:public Polytope {
public:
	JoinedPolytope(Polytope* p0, Polytope* p1);
	virtual ~JoinedPolytope();
	virtual const std::vector<Math::VecN>& getVertices() const;
	virtual const std::vector<Edge>& getEdges() const;
	virtual const std::vector<Triangle>& getFaces() const;
	virtual const std::vector<Math::VecN>& getNormals() const;
	virtual int getDimensions() const;
	virtual void update();
private:
	Polytope* a;
	Polytope* b;
	std::vector<Edge> retE;
	std::vector<Math::VecN> retV;
	std::vector<Math::VecN> retN;
	std::vector<Triangle> retF;
};
}
}
#endif /* MULTIDIMROT_POLYTOPE_JOINEDPOLYTOPE_H_ */
