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

#include <multiDimRot/math/VecN.h>
#include <multiDimRot/polytope/Polytope.h>
#include <string>
#include <vector>
#include <multiDimRot/math/ComplexDouble.h>
#include <functional>

using namespace MultiDimRot;
namespace MultiDimRot {
namespace Util {

int toInt(std::string in);
float toFloat(std::string in);
std::vector<std::string> splitAtWords(std::string in, char split = ' ');
void initQuad(std::vector<Polytope::Triangle>& tris, int &startIndex,
		const int &a, const int &b, const int &c, const int &d, const int &normal);
void addFace(std::vector<Polytope::Triangle> &faces, std::vector<Math::VecN> &normals, const std::vector<Math::VecN> &vertices,
		const int &a, const int &b, const int &c, int &id);
template<typename T> void del2DimArray(T** array, int length) {
	if (array!=0) {
		for (int i = 0;i<length;i++) {
			delete[] array[i];
		}
		delete[] array;
	}
}
bool isNearZero(const double d);
//parses a real or imaginary number (3, 2.5I), but no general complex numbers like 4+3I
Math::ComplexDouble parseComplex(const std::string& in);
template<typename T>
void joinVectors(int count, std::function<std::vector<T>(int)> getPartVec,
		std::function<const std::vector<T> (const std::vector<T>&, int, int)> process, std::vector<T>& output) {
	output.clear();
	int processedCount = 0;
	for (int i = 0;i<count;i++) {
		const std::vector<T> current = getPartVec(i);
		int end = processedCount+current.size();
		output.reserve(end);
		std::vector<T> processed = process(current, processedCount, i);
		output.insert(output.end(), processed.begin(), processed.end());
		processedCount = end;
	}
}
Polytope::Triangle offset(Polytope::Triangle orig, int vOff, int nOff);
}
}
