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

#ifndef MULTIDIMROT_POLYTOPE_COMPLEXGRAPH_H_
#define MULTIDIMROT_POLYTOPE_COMPLEXGRAPH_H_

#include <Polytope.h>
#include <functional>
#include <ComplexDouble.h>

namespace MultiDimRot {
namespace Polytope {

class ComplexGraph: public Polytope {
public:
	ComplexGraph(std::function<Math::ComplexDouble(const Math::ComplexDouble)> f,
			double rMin, double rStep, int rCount,
			double iMin, double iStep, int iCount);
	virtual ~ComplexGraph();
};

} /* namespace Polytope */
} /* namespace MultiDimRot */

#endif /* MULTIDIMROT_POLYTOPE_COMPLEXGRAPH_H_ */