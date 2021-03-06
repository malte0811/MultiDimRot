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
#ifndef SRC_MULTIDIMROT_POLYTOPE_NDCUBE_H_
#define SRC_MULTIDIMROT_POLYTOPE_NDCUBE_H_

#include <multiDimRot/polytope/Polytope.h>

namespace MultiDimRot {
namespace Polytope {
class NDCube: public Polytope {
public:
	NDCube(int dims);
	virtual ~NDCube();
};
}
}
#endif /* SRC_MULTIDIMROT_POLYTOPE_NDCUBE_H_ */
