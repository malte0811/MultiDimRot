package malte0811.multiDim.commands;

public class LayeredStringTokenizer {
	String s;
	int pos = 0;

	public LayeredStringTokenizer(String c) throws IllegalArgumentException {
		if (!isValid(c)) {
			throw new IllegalArgumentException(
					"Too many opening or closing brackets");
		}
		s = c;
	}

	public boolean isValid(String f) {
		int layer = 0;
		char[] c = f.toCharArray();
		for (char a : c) {
			if (a == '(') {
				layer++;
			}
			if (a == ')') {
				layer--;
				if (layer < 0) {
					return false;
				}
			}

		}
		return layer == 0;

	}

	public String nextToken() {
		String ret = "";
		if (pos == s.length()) {
			return null;
		}
		char c = s.charAt(pos);
		while (c == ' ' || c == '	') {
			pos++;
			c = s.charAt(pos);
		}
		int layer = 0;
		while (((c != ' ' && c != '	') || layer != 0) && pos < s.length() - 1) {
			if (c == '(') {
				layer++;
			} else if (c == ')') {
				layer--;
			}
			ret += c;
			pos++;
			c = s.charAt(pos);
		}
		if (pos == s.length() - 1 && c != ' ' && c != '	') {
			ret += c;
		}
		return ret;
	}

	public boolean hasNext() {
		return pos < s.length() - 1;
	}
}
