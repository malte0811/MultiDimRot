#include <string>
#include <vector>

namespace util {

int toInt(std::string in);
float toFloat(std::string in);
std::vector<std::string> splitAtWords(std::string in);
void initQuad(std::vector<Triangle>& tris, int &startIndex,
		const int &a, const int &b, const int &c, const int &d, const int &normal);
void addFace(std::vector<Triangle> &faces, std::vector<VecN> &normals, const std::vector<VecN> &vertices,
		const int &a, const int &b, const int &c, int &id);

}  // namespace util


