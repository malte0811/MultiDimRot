#ifndef VECN_H_
#define VECN_H_
#include <string>

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

	float& operator[](const int &i);
	const float& operator[](const int &i) const;

	std::string toString() const;
private:
	float* elements;
	int dimensions = 0;
};
#endif /* VECN_H_ */
