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
#include <stack>

namespace MultiDimRot {
namespace Math {
//Shunting yard algorithm
ExpressionParser::ExpressionParser(std::string expression) {
	addFunctions();
	int pos = 0;
	std::stack<ExpressionElement> opStack;
	ExpressionElement last('(', parenthesis);
	while (pos<expression.size()) {
		ExpressionElement ee(expression, pos);
		switch (ee.getType()) {
		case function:
			opStack.push(ee);
			break;
		case number:
			expressionList.push_back(ee);
			break;
		case x:
			expressionList.push_back(ee);
			break;
		case op:
			// handle unary -
			if (last.getChar()=='('&&(ee.getChar()=='+'||ee.getChar()=='-')) {
				expressionList.push_back(ExpressionElement(ComplexDouble()));
			}
			//WARNING: this will break once there are right-associative operators!
			while (!opStack.empty()&&opStack.top().getType()==op&&opStack.top().getOpPrecedence()>=ee.getOpPrecedence()) {
				expressionList.push_back(opStack.top());
				opStack.pop();
			}
			opStack.push(ee);
			break;
		case parenthesis:
			if (ee.getChar()=='(') {
				opStack.push(ee);
			} else {
				while (opStack.top().getType()!=parenthesis) {
					expressionList.push_back(opStack.top());
					opStack.pop();
				}
				opStack.pop();
				if (!opStack.empty()&&opStack.top().getType()==function) {
					expressionList.push_back(opStack.top());
					opStack.pop();
				}
			}
			break;
		case unknown:
			std::cout << "End of expression reached?";
			break;
		}
		last = ee;
	}
	while (!opStack.empty()) {
		if (opStack.top().getType()==parenthesis) {
			throw "Mismatched parenthesis!";
		}
		expressionList.push_back(opStack.top());
		opStack.pop();
	}
}

ExpressionParser::~ExpressionParser() {
}

ComplexDouble ExpressionParser::evaluate(const ComplexDouble& xVal) const {
	std::list<ExpressionElement>::const_iterator it = expressionList.begin();
	std::stack<ComplexDouble> stack;
	while (it!=expressionList.end()) {
		ExpressionElement next = (*it);
		ComplexDouble top = stack.empty()?ComplexDouble():stack.top();
		ComplexDouble second, val;
		switch (next.getType()) {
		case function:
			stack.pop();
			top = next.getFunction()(top);
			stack.push(top);
			break;
		case number:
			stack.push(next.getComplex());
			break;
		case op:
			stack.pop();
			second = stack.top();
			stack.pop();
			switch (next.getChar()) {
			case '+':
				val = top+second;
				break;
			case '*':
				val = top*second;
				break;
			case '-':
				val = second-top;
				break;
			case '/':
				val = second/top;
				break;
			case '^':
				val = ComplexDouble::pow(second, top);
				break;
			}
			stack.push(val);
			break;
			case x:
				stack.push(xVal);
				break;
			default:
				std::cout << next.getType();
				break;
		}
		it++;
	}
	if (stack.size()!=1) {
		throw "Something went wrong reading an expression.";
	}
	return stack.top();
}

ComplexDouble ExpressionParser::operator ()(const ComplexDouble& x) {
	return evaluate(x);
}

} /* namespace Math */
} /* namespace MultiDimRot */
