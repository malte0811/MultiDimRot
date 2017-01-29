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

using namespace MultiDimRot::Math;
ComplexDouble::ComplexDouble(double r, double i) {
	real = r;
	imaginary = i;
}

ComplexDouble::~ComplexDouble() {}

ComplexDouble ComplexDouble::operator +(const ComplexDouble& b) const {
	return ComplexDouble(real+b.real, imaginary+b.imaginary);
}

ComplexDouble ComplexDouble::operator -(const ComplexDouble& b) const {
	return ComplexDouble(real-b.real, imaginary-b.imaginary);
}

ComplexDouble ComplexDouble::operator *(const ComplexDouble& b) const {
	double r = real*b.real-imaginary*b.imaginary;
	double i = imaginary*b.real+b.imaginary*real;
	return ComplexDouble(r, i);
}

ComplexDouble ComplexDouble::operator /(const ComplexDouble& b) const {
	double r = real*b.real+imaginary*b.imaginary;
	double i = imaginary*b.real-b.imaginary*real;
	double div = b.real*b.real-b.imaginary*b.imaginary;
	r /= div;
	i /= div;
	return ComplexDouble(r, i);
}

void ComplexDouble::operator +=(const ComplexDouble& b) {
	real += b.real;
	imaginary += b.imaginary;
}

void ComplexDouble::operator -=(const ComplexDouble& b) {
	real -=b.real;
	imaginary -= b.imaginary;
}

//TODO optimization if these are used in the rendering loop
void ComplexDouble::operator *=(const ComplexDouble& b) {
	ComplexDouble tmp = (*this)*b;
	real = tmp.real;
	imaginary = tmp.imaginary;
}

void ComplexDouble::operator /=(const ComplexDouble& b) {
	ComplexDouble tmp = (*this)/b;
	real = tmp.real;
	imaginary = tmp.imaginary;
}
//END OF TODO
double ComplexDouble::getReal() const {
	return real;
}
double ComplexDouble::getImaginary() const {
	return imaginary;
}

std::ostream& operator<<(std::ostream& stream, const ComplexDouble& cd) {
	stream << cd.getReal();
	if (cd.getImaginary()>=0) {
		stream << "+";
	}
	stream << cd.getImaginary() << "*I";
	return stream;
}
