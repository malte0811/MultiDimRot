package malte0811.multiDim.commands;

public class LayeredStringTokenizer {
	String s;
	int pos = 0;
	char op;
	char cl;
	char[] separators;
	boolean kS;

	public LayeredStringTokenizer(String c, char open, char close, char[] sep,
			boolean keepStrings) throws IllegalArgumentException {
		s = c;
		op = open;
		cl = close;
		separators = sep;
		if (!isValid(c)) {
			throw new IllegalArgumentException(
					"Too many opening or closing brackets");
		}
		kS = keepStrings;
	}

	public boolean isValid(String f) {
		int layer = 0;
		char[] c = f.toCharArray();
		boolean inString = false;
		for (char a : c) {
			if (a == op) {
				layer++;
			}
			if (a == cl) {
				layer--;
				if (layer < 0) {
					return false;
				}
			}
			if (a == '\"') {
				inString = !inString;
			}

		}
		if (kS && inString) {
			return false;
		}
		return layer == 0;

	}

	public String nextToken() {
		String ret = "";
		boolean inString = false;
		char c = s.charAt(pos);
		while (isSeparator(c) && pos < s.length() - 1) {
			pos++;
			c = s.charAt(pos);
		}
		if (pos == s.length()) {
			return null;
		}
		int layer = 0;
		while ((!isSeparator(c) || layer != 0 || !((!inString && kS) || !kS))
				&& pos < s.length() - 1) {
			if (c == op) {
				layer++;
			} else if (c == cl) {
				layer--;
			}
			if (c == '\"') {
				inString = !inString;
			}
			ret += c;
			pos++;
			c = s.charAt(pos);
		}
		if (pos == s.length() - 1 && !isSeparator(c)) {
			ret += c;
		}
		return ret;
	}

	public boolean hasMoreTokens() {
		return pos < s.length() - 1;
	}

	private boolean isSeparator(char c) {
		for (char s : separators) {
			if (c == s) {
				return true;
			}
		}
		return false;
	}
}
