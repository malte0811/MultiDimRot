#ifndef VECN_H_
#define VECN_H_
#include <string>

class VecN {
public:
	VecN();
	VecN(int dimensions);
	VecN(const VecN &other);
	~VecN();
	float getElement(int i, float def);
	void setElement(int i, float value);
	int getDimensions();
	VecN operator+(VecN &d);
	float operator*(VecN &d);
	VecN operator*(float d);
	void operator=(const VecN &v);
	std::string toString();
	float* elements;
	int dimensions;
private:
};
#endif /* VECN_H_ */
