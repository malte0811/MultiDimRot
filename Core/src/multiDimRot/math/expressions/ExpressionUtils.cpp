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
#include <multiDimRot/math/ExpressionParser.h>
#include <multiDimRot/Util.h>
#include <cctype>
#include <functional>
#include <map>
#include <string>

namespace MultiDimRot {
namespace Math {
std::map<std::string, std::function<ComplexDouble(const ComplexDouble&)>> ExpressionParser::functions;
ExpressionElement::ExpressionElement() {
	type = unknown;
}

ExpressionElement::ExpressionElement(std::string in, int& pos) {
	std::string token = ExpressionParser::readToken(pos, in, type);
	switch (type) {
	case function:
		if (ExpressionParser::functions.count(token)==0) {
			throw "Function "+token+" not found";
		}
		fValue = ExpressionParser::functions[token];
		break;
	case number:
		cValue = Util::parseComplex(token);
		break;
	case parenthesis:
	case op:
	case x:
		charValue = token[0];
		break;
	case unknown:
		throw "Can't create expression element for "+in;
	}
}
EElementType ExpressionElement::getType() const {
	return type;
}
char ExpressionElement::getChar() const {
	return charValue;
}
const ComplexDouble& ExpressionElement::getComplex() const {
	return cValue;
}
const std::function<ComplexDouble(const ComplexDouble&)>& ExpressionElement::getFunction() const {
	return fValue;
}
int ExpressionElement::getOpPrecedence() {
	if (type!=op) {
		throw "Can't get operator precedence for non-operator!";
	}
	if (charValue=='+'||charValue=='-') {
		return 0;
	}
	if (charValue=='*'||charValue=='/') {
		return 1;
	}
	std::cout << "Unknown operator: " << charValue << "\n";
	return -1;
}

EElementType ExpressionParser::getType(char c) {
	if (c=='('||c==')') {
		return parenthesis;
	}
	if (c=='*'||c=='+'||c=='-'||c=='/') {
		return op;
	}
	if (c=='x') {
		return x;
	}
	if (std::isdigit(c)||c=='.') {
		return number;
	}
	if (std::isalpha(c)) {
		return function;
	}
	return unknown;
}
std::string ExpressionParser::readToken(int& pos, std::string expression, EElementType& type) {
	std::string ret = "";
	type = unknown;
	bool done = false;
	while (!done&&pos<expression.size()) {
		const char nextChar = expression[pos];
		const EElementType nextType = getType(nextChar);
		if (type==unknown) {
			type = nextType;
		}
		if ((type==op||type==parenthesis||type==x)&&ret.size()>0) {
			done = true;
		} else if (type==number) {
			if (nextType!=number&&(nextChar!='i'&&nextChar!='I')) {
				done = true;
			}
		} else if (type!=unknown&&type!=nextType&&!(type==function&&nextType==x)) {
			done = true;
		}
		if (type!=unknown&&!done) {
			ret+=nextChar;
		}
		if (!done) {
			pos++;
		}
	}
	return ret;
}
void ExpressionParser::addFunctions() {
	if (functions.size()==0) {
		functions["exp"] = [](const ComplexDouble& c){
			double multiplier = std::exp(c.getReal());
			double real = std::cos(c.getImaginary());
			double imaginary = std::sin(c.getImaginary());
			return ComplexDouble(real*multiplier, imaginary*multiplier);
		};
		functions["im"] = [](const ComplexDouble& c) {
			return ComplexDouble(0, c.getImaginary());
		};
		functions["re"] = [](const ComplexDouble& c) {
			return ComplexDouble(c.getReal());
		};
	}
}
} /* namespace Math */
} /* namespace MultiDimRot */
