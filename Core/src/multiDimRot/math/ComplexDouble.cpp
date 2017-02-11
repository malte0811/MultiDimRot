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

#include <multiDimRot/math/ComplexDouble.h>
#include <cmath>

using namespace MultiDimRot::Math;
ComplexDouble::ComplexDouble(double r, double i) {
	real = r;
	imaginary = i;
}

ComplexDouble::~ComplexDouble() {}

ComplexDouble ComplexDouble::pow(const ComplexDouble& a, const ComplexDouble& b) {
	if (a==0) {
		return ComplexDouble();
	}
	return exp(ln(a)*b);
}
ComplexDouble ComplexDouble::exp(const ComplexDouble& a) {
	double multiplier = std::exp(a.getReal());
	double real = std::cos(a.getImaginary());
	double imaginary = std::sin(a.getImaginary());
	return ComplexDouble(real*multiplier, imaginary*multiplier);
}
ComplexDouble ComplexDouble::ln(const ComplexDouble& a) {
	ComplexDouble dir(0, std::atan2(a.imaginary, a.real));
	return std::log(a.abs())+dir;
}
double ComplexDouble::abs() const {
	return std::sqrt(real*real+imaginary*imaginary);
}
ComplexDouble ComplexDouble::sin(const ComplexDouble& a) {
	return -I*(exp(I*a)-exp(-I*a))/2;
}
ComplexDouble ComplexDouble::cos(const ComplexDouble& a) {
	return (exp(I*a)+exp(-I*a))/2;
}
ComplexDouble ComplexDouble::tan(const ComplexDouble& a) {
	return sin(a)/cos(a);
}
//END OF TODO
double ComplexDouble::getReal() const {
	return real;
}
double ComplexDouble::getImaginary() const {
	return imaginary;
}
