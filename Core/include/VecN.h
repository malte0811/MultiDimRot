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
	void setElement(int i, float value);
	int getDimensions() const;
	VecN operator+(const VecN &d) const;
	float operator*(const VecN &d) const;
	VecN operator*(float d) const;
	void operator=(const VecN &v);
	float operator[](int i) const;
	std::string toString() const;
	float* elements;
	int dimensions;
private:
};
#endif /* VECN_H_ */
