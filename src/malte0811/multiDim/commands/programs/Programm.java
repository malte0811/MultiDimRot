package malte0811.multiDim.commands.programs;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
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
				System.out.println(cmd);
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
					int i = (int) Programm.getDoubleValue(st.nextToken());
					return i;
				} else if (tmp.equalsIgnoreCase("while")) {
					String t = st.nextToken();
					while (st.hasMoreTokens()) {
						t += " " + st.nextToken();
					}
					if (layers.length == 0
							|| layers[layers.length - 1] != currLine) {
						layers = Arrays.copyOf(layers, layers.length + 1);
						layers[layers.length - 1] = currLine;
					}
					if (!MathHelper.isTrue(t, this)) {
						int oldI = currLine;
						int intE = 1;
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
									layers = Arrays.copyOf(layers,
											layers.length - 1);
									break;
								}
							} else if (fT.equalsIgnoreCase("while")) {
								intE++;
							}
						}
						if (currLine == oldI) {
							System.exit(13);
						}
					}
				} else if (tmp.equalsIgnoreCase("end")) {
					currLine = layers[layers.length - 1] - 1;
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
							setDoubleValue(tmp, MathHelper.calculate(term));
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
		Path p = Paths
				.get(DimRegistry.getUserDir() + sep + "scripts" + sep + s);
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
		return progr;
	}

	public static double getDoubleValue(String name)
			throws NumberFormatException, IllegalArgumentException {
		if (name.contains("\"")) {
			throw new IllegalArgumentException(
					"This is not a number, maybe a string?");
		}
		if (name.contains("(") || name.contains("+") || name.contains("-")
				|| name.contains("*") || name.contains("/")) {
			try {
				return MathHelper.calculate(name);
			} catch (IllegalArgumentException x) {
				System.out.println(x.getMessage());
			}
		}
		if (getCurrentProgramm() == null) {
			return Double.parseDouble(name);
		}

		if (!getCurrentProgramm().numbers.containsKey(name)) {
			return Double.parseDouble(name);
		}
		return getCurrentProgramm().numbers.get(name);
	}

	public void setDoubleValue(String name, double value)
			throws IllegalArgumentException {
		if (!strings.containsKey(name)) {
			numbers.put(name, value);
		} else {
			throw new IllegalArgumentException(name + "is a string variable");
		}
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

	public static String getStringValue(String name)
			throws IllegalArgumentException {
		if (!name.contains("+")) {
			if (!name.contains("\"") && getCurrentProgramm() != null) {
				HashMap<String, String> var = getCurrentProgramm().strings;
				if (var.containsKey(name)) {
					return var.get(name);
				} else {
					throw new IllegalArgumentException("The string variable "
							+ name + "does not exist");
				}
			} else {
				return StringHelper.replace(name);
			}
		}
		return StringHelper.parse(name);
	}

	public static Programm getCurrentProgramm() {
		return instance;
	}

	public static void terminate() {
		DimRegistry.getCalcThread().setCurrentProgram(null);
	}
}
