/*
 * Util.h
 *
 *  Created on: 28.07.2016
 *      Author: malte
 */

#include <string>
#include <vector>

namespace util {

int toInt(std::string in);
float toFloat(std::string in);
std::vector<std::string> splitAtWords(std::string in);

}  // namespace util


