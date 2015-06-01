package malte0811.multiDim.commands.programs;

import java.util.HashMap;

public class StringHelper {
	public static String parse(String s, HashMap<String, String> variables)
			throws IllegalArgumentException {
		if (!s.contains("+")) {
			if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
				return replace(s.substring(1, s.length() - 1));
			}
			return Programm.getStringValue(s);
		}
		String ret = "";
		char[] ch = s.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == '+') {
				return parse(s.substring(0, i), variables)
						+ parse(s.substring(i + 1), variables);
			}
		}
		return ret;
	}

	public static String replace(String s) throws IllegalArgumentException {
		if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
			s = s.substring(1, s.length() - 1);
		}
		String ret = "";
		char[] ch = s.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == '\\') {
				if (i == s.length() - 1) {
					throw new IllegalArgumentException(
							"The last character of a string must not be a backslash");
				}
				switch (ch[i + 1]) {
				case '\\':
					i++;
					ret += "\\";
					break;
				case 'n':
					i++;
					ret += "\r\n";
					break;
				case '\'':
					i++;
					ret += "\'";
					break;
				case '\"':
					i++;
					ret += "\"";
					break;
				case 't':
					i++;
					ret += "\t";
					break;
				case 'u':
					if (i >= ch.length - 5) {
						throw new IllegalArgumentException(
								"The parameter for a unicode character has to be 4 characters long");
					}
					String uni = "" + ch[i + 2] + ch[i + 3] + ch[i + 4]
							+ ch[i + 5];
					try {
						ret += (char) Integer.parseInt(uni, 16);
					} catch (NumberFormatException x) {
						throw new IllegalArgumentException(
								"The parameter for a unicode character has to be a 4 characters long hexadecimal number");
					}
					i += 5;
					break;
				default:
					throw new IllegalArgumentException(
							"Invalid escape sequence: \\" + ch[i + 1]);
				}
			} else {
				ret += ch[i];
			}
		}
		return ret;
	}

	public static String getStringValue(String name,
			HashMap<String, String> variables) throws IllegalArgumentException {
		if (!name.contains("+")) {
			if (!name.contains("\"")) {
				if (variables != null && variables.containsKey(name)) {
					return variables.get(name);
				} else {
					if (!name.contains(" ")) {
						throw new IllegalArgumentException(
								"The string variable \"" + name
										+ "\" does not exist");
					} else {
						throw new IllegalArgumentException("The string \""
								+ name + "\" is not valid");
					}
				}
			} else {
				return StringHelper.replace(name);
			}
		}
		return StringHelper.parse(name, variables);
	}

}
