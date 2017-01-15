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
#ifndef VECN_H_
#define VECN_H_

#include <string>

namespace MultiDimRot {
namespace Math {
class VecN {
public:
	VecN();
	VecN(int dimensions);
	VecN(const VecN &other);
	~VecN();
	float getElement(int i, float def) const;
	int getDimensions() const;
	float getLength() const;
	void scaleToLength(float length, bool ignoreLast);
	VecN operator+(const VecN &d) const;
	float operator*(const VecN &d) const;
	VecN operator*(float d) const;
	void operator=(const VecN &v);
	bool operator==(const VecN& other);
	VecN operator-() const;

	float& operator[](const int &i);
	const float& operator[](const int &i) const;

	std::string toString() const;
private:
	float* elements;
	int dimensions = 0;
};
}
}
#endif /* VECN_H_ */
