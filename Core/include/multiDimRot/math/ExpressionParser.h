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
#ifndef SRC_MULTIDIMROT_MATH_EXPRESSIONPARSER_H_
#define SRC_MULTIDIMROT_MATH_EXPRESSIONPARSER_H_

#include <multiDimRot/math/ComplexDouble.h>
#include <functional>
#include <list>
#include <map>
#include <string>

namespace MultiDimRot {
namespace Math {
enum EElementType {
	function,
	number,
	parenthesis,
	unknown,
	op,
	x
};
class ExpressionElement {
public:
	ExpressionElement();
	ExpressionElement(std::string in, int& pos);
	ExpressionElement(const ComplexDouble& d);
	ExpressionElement(char c, EElementType type);
	EElementType getType() const;
	const std::function<ComplexDouble(const ComplexDouble&)>& getFunction() const;
	const ComplexDouble& getComplex() const;
	char getChar() const;
	int getOpPrecedence();
private:
	EElementType type;
	std::function<ComplexDouble(const ComplexDouble&)> fValue;
	ComplexDouble cValue;
	char charValue = 0;
};
class ExpressionParser {
public:
	ExpressionParser(std::string exp);
	virtual ~ExpressionParser();
	ComplexDouble evaluate(const ComplexDouble& x) const;
	static std::map<std::string, std::function<ComplexDouble(const ComplexDouble&)>> functions;
	void addFunctions();
	ComplexDouble operator()(const ComplexDouble& x);
	//Type is output of token type.
	static std::string readToken(int& pos, std::string expression, EElementType& type);
	static EElementType getType(char c);
private:
	std::list<ExpressionElement> expressionList;
};

} /* namespace Math */
} /* namespace MultiDimRot */

#endif /* SRC_MULTIDIMROT_MATH_EXPRESSIONPARSER_H_ */
