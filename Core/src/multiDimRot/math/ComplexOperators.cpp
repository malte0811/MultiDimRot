#include <multiDimRot/math/ComplexDouble.h>

namespace MultiDimRot {
namespace Math {
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
ComplexDouble operator+(double a, const ComplexDouble& b) {
	return ComplexDouble(a)+b;
}
ComplexDouble operator-(double a, const ComplexDouble& b) {
	return ComplexDouble(a)-b;
}
ComplexDouble operator*(double a, const ComplexDouble& b) {
	return ComplexDouble(a)*b;
}
ComplexDouble operator/(double a, const ComplexDouble& b) {
	return ComplexDouble(a)/b;
}
ComplexDouble ComplexDouble::operator+(double d) const {
	return *this+ComplexDouble(d);
}
ComplexDouble ComplexDouble::operator-(double d) const {
	return *this-ComplexDouble(d);
}
ComplexDouble ComplexDouble::operator*(double d) const {
	return *this*ComplexDouble(d);
}
ComplexDouble ComplexDouble::operator/(double d) const {
	return *this/ComplexDouble(d);
}
bool ComplexDouble::operator==(double d) const {
	return imaginary==0&&real==d;
}
ComplexDouble ComplexDouble::operator-() const {
	return ComplexDouble(-real, -imaginary);
}

std::ostream& operator<<(std::ostream& stream, const ComplexDouble& cd) {
	if (cd.getReal()!=0||cd.getImaginary()==0) {
		stream << cd.getReal();
	}
	if (cd.getReal()!=0&&cd.getImaginary()!=0&&cd.getImaginary()>0) {
		stream << "+";
	}
	if (cd.getImaginary()!=0) {
		stream << cd.getImaginary() << "*I";
	}
	return stream;
}
}
}
