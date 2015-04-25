package malte0811.multiDim.commands.programs;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.StringTokenizer;

public class MathHelper {
	public static double calculate(Programm p, String term) {
		Deque<String> s = parse(term);
		return getValue(s, p);
	}

	private static double getValue(Deque<String> d, Programm p) {
		double ret = 0;
		String next = d.pollLast();
		switch (next) {
		case "*":
			ret = getValue(d, p) * getValue(d, p);
			break;
		case "/":
			ret = getValue(d, p) / getValue(d, p);
			break;
		case "-":
			ret = getValue(d, p) - getValue(d, p);
			break;
		case "+":
			ret = getValue(d, p) + getValue(d, p);
			break;
		default:
			ret = Programm.getValue(next);
		}

		return ret;
	}

	private static Deque<String> parse(String term) {
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
						System.out.println("Termfehler: zu viele Klammern");
						Programm.terminate();
					}
				}
			}
		}
		if (ebene != 0) {
			System.out.println("Klammerfehler");
			Programm.terminate();
		}
		return s;
	}

	public static boolean isTrue(String s, Programm p) {
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
				firstValue = calculate(p, first);
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
		secondValue = calculate(p, second);
		// System.out.println(firstValue+vergleiche[vergleich]+secondValue);
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
}
