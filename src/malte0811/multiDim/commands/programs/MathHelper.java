package malte0811.multiDim.commands.programs;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.StringTokenizer;

import malte0811.multiDim.addons.ReturningCommand;

public class MathHelper {
	public static double calculate(String term,
			HashMap<String, Double> variables) throws IllegalArgumentException {
		Deque<String> s = parse(term);
		return getValue(s, variables);
	}

	public static double getValue(Deque<String> d,
			HashMap<String, Double> variables) {
		if (d.size() == 1) {
			String next = d.peekLast();
			if (next.contains("(")) {
				return ReturningCommand.processCommand(next);
			}
		}
		double ret = 0;
		String next = d.pollLast();
		switch (next) {
		case "*":
			double b = getValue(d, variables);
			double a = getValue(d, variables);
			ret = a * b;
			break;
		case "/":
			b = getValue(d, variables);
			a = getValue(d, variables);
			ret = a / b;
			break;
		case "-":
			b = getValue(d, variables);
			a = getValue(d, variables);
			ret = a - b;
			break;
		case "+":
			b = getValue(d, variables);
			a = getValue(d, variables);
			ret = a + b;
			break;
		default:
			ret = getDoubleValue(next, variables);
		}
		return ret;
	}

	public static Deque<String> parse(String term) {
		int ebene = 0;
		Deque<String> s = new ArrayDeque<>();
		if (term.charAt(0) == '(' && term.charAt(term.length() - 1) == ')') {
			term = term.substring(1, term.length() - 1);
		}
		// 2:+-
		// 1:*/
		// 0:return

		for (int p = 2; p >= 0; p--) {
			for (int i = 0; i < term.length(); i++) {
				switch (p) {
				case 2:
					if ((term.charAt(i) == '+' || term.charAt(i) == '-')
							&& ebene == 0) {
						Deque<String> l = parse(term.substring(0, i));
						Deque<String> r = parse(term.substring(i + 1,
								term.length()));
						while (!l.isEmpty()) {
							s.addLast(l.pollFirst());
						}
						while (!r.isEmpty()) {
							s.addLast(r.pollFirst());
						}
						s.addLast(Character.toString(term.charAt(i)));
						return s;
					}
					break;
				case 1:
					if ((term.charAt(i) == '*' || term.charAt(i) == '/')
							&& ebene == 0) {
						Deque<String> l = parse(term.substring(0, i));
						Deque<String> r = parse(term.substring(i + 1,
								term.length()));
						while (!l.isEmpty()) {
							s.addLast(l.pollFirst());
						}
						while (!r.isEmpty()) {
							s.addLast(r.pollFirst());
						}
						s.addLast(Character.toString(term.charAt(i)));
						return s;
					}
					break;
				case 0:
					s.addLast(term);
					return s;
				}
				if (term.charAt(i) == '(') {
					ebene++;
				}
				if (term.charAt(i) == ')') {
					ebene--;
					if (ebene < 0) {
						throw new IllegalArgumentException(
								"To many closing brackets");
					}
				}
			}
		}
		if (ebene != 0) {
			throw new IllegalArgumentException("Not enough closing brackets");
		}
		return s;
	}

	public static boolean isTrue(String s, Programm p,
			HashMap<String, Double> variables) {
		boolean ret = false;
		StringTokenizer st = new StringTokenizer(s);
		String[] vergleiche = { "==", "<", ">", ">=", "<=", "!=" };
		Arrays.sort(vergleiche);
		String first = st.nextToken();
		double firstValue = 0;
		int vergleich = -1;
		while (st.hasMoreTokens()) {
			String tmp = st.nextToken();
			if (Arrays.binarySearch(vergleiche, tmp) >= 0) {
				firstValue = calculate(first, variables);
				vergleich = Arrays.binarySearch(vergleiche, tmp);
				break;
			} else {
				first += tmp;
			}
		}
		String second = st.nextToken();
		double secondValue = 0;
		while (st.hasMoreTokens()) {
			first += st.nextToken();
		}
		secondValue = calculate(second, variables);
		switch (vergleiche[vergleich]) {
		case "==":
			ret = firstValue == secondValue;
			break;
		case "<":
			ret = firstValue < secondValue;
			break;
		case ">":
			ret = firstValue > secondValue;
			break;
		case ">=":
			ret = firstValue >= secondValue;
			break;
		case "<=":
			ret = firstValue <= secondValue;
			break;
		case "!=":
			ret = firstValue != secondValue;
		}
		return ret;
	}

	public static double getDoubleValue(String name,
			HashMap<String, Double> variables) throws NumberFormatException,
			IllegalArgumentException {
		if (name.contains("(") || name.contains("+")
				|| name.substring(1).contains("-") || name.contains("*")
				|| name.contains("/")) {
			return MathHelper.calculate(name, variables);
		}
		if (variables == null) {
			return Double.parseDouble(name);
		}

		if (!variables.containsKey(name)) {
			return Double.parseDouble(name);
		}
		return variables.get(name);
	}
}
