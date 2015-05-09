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
	// sollte string sein
	final Object[] file;
	final boolean debug = false;
	static private Programm instance = null;
	private HashMap<String, Double> numbers = new HashMap<>();
	private int zaehler = 0;
	private int[] ebenen = new int[0];
	public static boolean stop = false;

	public Programm(Object[] data) {
		file = data;
	}

	public int step() {
		if (file.length - 1 == zaehler) {
			instance = null;
			return 0;
		}
		instance = this;
		if (innerProgramm != null) {
			int s = innerProgramm.step();
			if (s == 0) {
				innerProgramm = null;
			} else {
				return s;
			}
		}
		if (stop) {
			DimRegistry.getCalcThread().getCommandListener().input.toggle();
			return 0;
		}
		do {
			String cmd = (String) file[zaehler];
			zaehler++;
			if (cmd == null || cmd.equals("")) {
				DimRegistry.getCalcThread().getCommandListener().input.toggle();
				return 0;
			}
			if (debug) {
				System.out.println(cmd);
			}
			if (!Command.processCommand(cmd, true)) {
				LayeredStringTokenizer st = new LayeredStringTokenizer(cmd);
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
					if (ebenen.length == 0
							|| ebenen[ebenen.length - 1] != zaehler) {
						ebenen = Arrays.copyOf(ebenen, ebenen.length + 1);
						ebenen[ebenen.length - 1] = zaehler;
					}
					if (!MathHelper.isTrue(t, this)) {
						int oldI = zaehler;
						int intE = 1;
						for (int i = zaehler; i < file.length; i++) {
							StringTokenizer temp = new StringTokenizer(
									(String) file[i]);
							if (!temp.hasMoreTokens()) {
								continue;
							}
							String fT = temp.nextToken();
							if (fT.equalsIgnoreCase("end")) {
								intE--;
								if (intE == 0) {
									zaehler = i + 1;
									if (zaehler >= file.length - 1) {
										DimRegistry.getCalcThread()
												.getCommandListener().input
												.toggle();
										return 0;
									}
									ebenen = Arrays.copyOf(ebenen,
											ebenen.length - 1);
									break;
								}
							} else if (fT.equalsIgnoreCase("while")) {
								intE++;
							}
						}
						if (zaehler == oldI) {
							System.exit(13);
						}
					}
				} else if (tmp.equalsIgnoreCase("end")) {
					zaehler = ebenen[ebenen.length - 1] - 1;
				} else if (tmp.equalsIgnoreCase("else")) {

				} else {
					if (st.nextToken().equals("=")) {
						String term = st.nextToken();
						while (st.hasMoreTokens()) {
							term += st.nextToken();
						}
						if (term == null) {
							System.err.println("No term");
							System.exit(-10);
						}
						setDoubleValue(tmp, MathHelper.calculate(term));
					} else {
						try {
							innerProgramm = load(cmd);
						} catch (Exception x) {
						}
					}
				}

			}
		} while (zaehler < file.length && !stop);
		DimRegistry.getCalcThread().getCommandListener().input.toggle();
		return 0;
	}

	public static Programm load(String s) throws Exception {
		String sep = DimRegistry.getFileSeperator();
		Path p = Paths
				.get(DimRegistry.getUserDir() + sep + "scripts" + sep + s);
		Object[] file = new Object[0];
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

	public void setDoubleValue(String name, double value) {
		numbers.put(name, value);
	}

	// TODO not only hardcoded strings, add string variables
	public static String getStringValue(String name)
			throws IllegalArgumentException {
		if (!name.contains("+") && !name.contains("\"")) {
			return StringHelper.replace(name);
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
