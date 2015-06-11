package malte0811.multiDim.commands.programs;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.LayeredStringTokenizer;

public class Programm {
	public Programm innerProgramm = null;
	final String[] file;
	final boolean debug = false;
	static private Programm instance = null;
	private HashMap<String, Double> numbers = new HashMap<>();
	private HashMap<String, String> strings = new HashMap<>();
	private static Set<String> banned = new HashSet<>();

	private int currLine = 0;
	private int[] layers = new int[0];
	public static boolean stop = false;

	public Programm(String[] data) {
		file = data;
	}

	public int step() {
		if (file.length <= currLine && innerProgramm == null) {
			instance = null;
			return 0;
		}
		instance = this;
		if (stop) {
			DimRegistry.getCalcThread().getCommandListener().input.toggle();
			return 0;
		}
		while (currLine < file.length && !stop) {
			if (innerProgramm != null) {
				int s = innerProgramm.step();
				if (s == 0) {
					innerProgramm = null;
				} else {
					return s;
				}
			}
			String cmd = file[currLine];
			currLine++;
			if (cmd == null || cmd.equals("")) {
				DimRegistry.getCalcThread().getCommandListener().input.toggle();
				return 0;
			}
			if (debug) {
				System.out.println(currLine + " " + cmd);
			}
			if (!Command.processCommand(cmd, true)) {
				LayeredStringTokenizer st = new LayeredStringTokenizer(cmd,
						'(', ')', new char[] { ' ', '	' }, true);
				String tmp = st.nextToken();
				if (tmp.toUpperCase().equals("SLEEP")) {
					if (!st.hasMoreTokens()) {
						System.out.println("1 argument is required");
						continue;
					}
					int i = (int) MathHelper.getDoubleValue(st.nextToken(),
							numbers);
					return i;
				} else if (tmp.equalsIgnoreCase("while")
						|| tmp.equalsIgnoreCase("if")) {
					String t = st.nextToken();
					while (st.hasMoreTokens()) {
						t += " " + st.nextToken();
					}
					if (layers.length == 0
							|| layers[layers.length - 1] != currLine) {
						layers = Arrays.copyOf(layers, layers.length + 1);
						layers[layers.length - 1] = currLine;
					}
					if (!MathHelper.isTrue(t, this, numbers)) {
						int oldI = currLine;
						int intE = 1;
						for (int i = currLine; i < file.length; i++) {
							StringTokenizer temp = new StringTokenizer(
									(String) file[i]);
							if (!temp.hasMoreTokens()) {
								continue;
							}
							String fT = temp.nextToken();
							if (fT.equalsIgnoreCase("end")
									|| (tmp.equalsIgnoreCase("if") && fT
											.equalsIgnoreCase("else"))) {
								intE--;
								if (intE == 0) {
									currLine = i + 1;
									if (currLine >= file.length - 1) {
										DimRegistry.getCalcThread()
												.getCommandListener().input
												.toggle();
										return 0;
									}
									if (tmp.equalsIgnoreCase("while")) {
										layers = Arrays.copyOf(layers,
												layers.length - 1);
									}

									break;
								}
							} else if (fT.equalsIgnoreCase("while")
									|| fT.equalsIgnoreCase("if")) {
								intE++;
							}
						}
						if (currLine == oldI) {
							System.exit(13);
						}
					}
				} else if (tmp.equalsIgnoreCase("end")) {
					if (layers.length > 0) {
						int lastLayer = layers[layers.length - 1];
						if (new StringTokenizer(file[lastLayer - 1])
								.nextToken().equalsIgnoreCase("while")) {
							currLine = layers[layers.length - 1] - 1;
						}
					} else {
						System.out.println("Line " + currLine
								+ ": end whithout while/if");
						return 0;
					}
				} else if (tmp.equalsIgnoreCase("else")) {
					int intE = 0;
					for (int i = currLine; i < file.length; i++) {
						StringTokenizer temp = new StringTokenizer(
								(String) file[i]);
						if (!temp.hasMoreTokens()) {
							continue;
						}
						String fT = temp.nextToken();
						if (fT.equalsIgnoreCase("end")) {
							intE--;
							if (intE == 0) {
								currLine = i + 1;
								if (currLine >= file.length - 1) {
									DimRegistry.getCalcThread()
											.getCommandListener().input
											.toggle();
									return 0;
								}
								break;
							}
						} else if (fT.equalsIgnoreCase("while")
								|| fT.equalsIgnoreCase("if")) {
							intE++;
						}
					}
				} else {

					if (st.nextToken().equals("=")) {
						st = new LayeredStringTokenizer(cmd, '\"', '\"',
								new char[] { ' ', '	' }, false);
						st.nextToken();
						st.nextToken();
						String term = st.nextToken();
						while (st.hasMoreTokens()) {
							term += st.nextToken();
						}
						if (term == null) {
							System.err.println("No term");
							System.exit(-10);
						}
						try {
							setDoubleValue(tmp,
									MathHelper.calculate(term, numbers));
						} catch (IllegalArgumentException x) {
							try {
								setStringValue(tmp, getStringValue(term));
							} catch (IllegalArgumentException x2) {
								System.out.println(x.getMessage());
								System.out.println(x2.getMessage());
							}
						}
					} else {
						try {
							innerProgramm = load(cmd);
						} catch (Exception x) {
						}
					}
				}

			}
		}
		if (innerProgramm != null) {
			int s = innerProgramm.step();
			if (s == 0) {
				innerProgramm = null;
			} else {
				return s;
			}
		}
		DimRegistry.getCalcThread().getCommandListener().input.toggle();
		return 0;
	}

	public static Programm load(String s) throws Exception {
		String sep = DimRegistry.getFileSeperator();
		LayeredStringTokenizer lst = new LayeredStringTokenizer(s, '(', ')',
				new char[] { ' ', '	' }, true);
		Path p = Paths.get(DimRegistry.getUserDir() + sep + "scripts" + sep
				+ lst.nextToken());
		String[] file = new String[0];
		if (!Files.exists(p) || !Files.isRegularFile(p)) {
			System.out.println("File or command does not exist");
			return null;
		}
		File f = p.toFile();
		FileReader fr = new FileReader(f);
		while (true) {
			String line = "";
			int r = fr.read();
			while (r == '\r' || r == '\n') {
				r = fr.read();
			}
			while (r != -1 && r != '\r' && r != '\n') {
				line += (char) r;
				r = fr.read();
			}

			file = Arrays.copyOf(file, file.length + 1);
			file[file.length - 1] = line;

			if (r == -1) {
				break;
			}
		}
		fr.close();
		Programm progr = new Programm(file);
		int i = 0;
		while (lst.hasMoreTokens()) {
			progr.setStringValue("args" + i, lst.nextToken());
			i++;
		}
		progr.setDoubleValue("argsLength", i);
		return progr;
	}

	public void setDoubleValue(String name, double value)
			throws IllegalArgumentException {
		if (!strings.containsKey(name)) {
			if (!banned.contains(name)) {
				numbers.put(name, value);
			} else {

			}
		} else {
			throw new IllegalArgumentException(name + "is a string variable");
		}
	}

	public static double getDoubleValue(String name) {
		return MathHelper.getDoubleValue(name, instance == null ? null
				: instance.getDoubleVariables());
	}

	public void setStringValue(String name, String value)
			throws IllegalArgumentException {
		if (!numbers.containsKey(name)) {
			strings.put(name, value);
		} else {
			throw new IllegalArgumentException(name
					+ "is a double/number variable");
		}
	}

	public static String getStringValue(String name) {
		return StringHelper.getStringValue(name, instance == null ? null
				: instance.getStringVariables());
	}

	public static Programm getCurrentProgramm() {
		return instance;
	}

	public static void terminate() {
		DimRegistry.getCalcThread().setCurrentProgram(null);
	}

	public HashMap<String, Double> getDoubleVariables() {
		return numbers;
	}

	public HashMap<String, String> getStringVariables() {
		return strings;
	}

	public static Set<String> getBannedVariableNames() {
		return banned;
	}

	public static void addBannedVariableName(String s) {
		banned.add(s);
	}
}
