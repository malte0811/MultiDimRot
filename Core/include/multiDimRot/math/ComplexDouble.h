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
#ifndef MULTIDIMROT_MATH_COMPLEXDOUBLE_H_
#define MULTIDIMROT_MATH_COMPLEXDOUBLE_H_

#include <iostream>

namespace MultiDimRot {
namespace Math {
class ComplexDouble {
public:
	ComplexDouble(double r = 0, double i = 0);
	virtual ~ComplexDouble();
	ComplexDouble operator+(const ComplexDouble& b) const;
	ComplexDouble operator-(const ComplexDouble& b) const;
	ComplexDouble operator*(const ComplexDouble& b) const;
	ComplexDouble operator/(const ComplexDouble& b) const;
	ComplexDouble operator+(double d) const;
	ComplexDouble operator-(double d) const;
	ComplexDouble operator*(double d) const;
	ComplexDouble operator/(double d) const;
	void operator+=(const ComplexDouble& b);
	void operator-=(const ComplexDouble& b);
	void operator*=(const ComplexDouble& b);
	void operator/=(const ComplexDouble& b);
	ComplexDouble operator-() const;
	bool operator==(double d) const;
	double getReal() const;
	double getImaginary() const;
	double abs() const;
	static ComplexDouble pow(const ComplexDouble& a, const ComplexDouble& b);
	static ComplexDouble exp(const ComplexDouble& a);
	static ComplexDouble ln(const ComplexDouble& a);
	static ComplexDouble sin(const ComplexDouble& a);
	static ComplexDouble cos(const ComplexDouble& a);
	static ComplexDouble tan(const ComplexDouble& a);
private:
	double real;
	double imaginary;
};
const static ComplexDouble I(0, 1);
ComplexDouble operator+(double a, const ComplexDouble& b);
ComplexDouble operator-(double a, const ComplexDouble& b);
ComplexDouble operator*(double a, const ComplexDouble& b);
ComplexDouble operator/(double a, const ComplexDouble& b);
}
}
std::ostream& operator<<(std::ostream& os, const MultiDimRot::Math::ComplexDouble& cd);
#endif /* MULTIDIMROT_MATH_COMPLEXDOUBLE_H_ */
