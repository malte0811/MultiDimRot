package malte0811.multiDim.addons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import malte0811.multiDim.CalcThread;
import malte0811.multiDim.CommandListener;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.render.CentralOne;
import malte0811.multiDim.render.CentralThree;
import malte0811.multiDim.render.CentralTwo;
import malte0811.multiDim.render.ParallelRender;
import malte0811.multiDim.render.RenderAlgo;
import malte0811.multiDim.solids.HyperCube;
import malte0811.multiDim.solids.Solid;
import malte0811.multiDim.solids.TestingSolid;

public class DimRegistry {

	private static HashMap<Integer, Class<? extends RenderAlgo>> renderAlgos = new HashMap<>();
	private static HashMap<String, Class<? extends Solid>> staticSolids = new HashMap<>();
	private static String userDir;
	private static String sep;
	private static TextStream output = null;
	private static LogStream out = null;
	private static PrintStream old = null;

	public static CalcThread instance = null;
	static {
		renderAlgos.put(0, ParallelRender.class);
		renderAlgos.put(1, CentralOne.class);
		renderAlgos.put(2, CentralTwo.class);
		renderAlgos.put(3, CentralThree.class);
		staticSolids.put("HyperCube", HyperCube.class);
		staticSolids.put("TestingSolid", TestingSolid.class);
		Programm.addBannedVariablePrefix("_");
		sep = System.getProperty("file.separator");
		try {
			userDir = Paths.get(ClassLoader.getSystemResource(".").toURI())
					.toFile().getAbsolutePath()
					+ sep + "MultiDimRot";
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			old = System.out;
			out = new LogStream();
			output = new TextStream(out);
			System.setOut(output);
			System.setErr(output);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void addRenderAlgo(Class<? extends RenderAlgo> newAlgo,
			int index) {
		renderAlgos.put(index, newAlgo);
	}

	public static RenderAlgo getAlgoInstance(int i) throws Exception {
		Class<? extends RenderAlgo> c = renderAlgos.get(i);
		Constructor<? extends RenderAlgo> constr = c.getConstructor();
		RenderAlgo instance = constr.newInstance();
		return instance;
	}

	public static void addCommand(Command c, boolean forceOverride) {
		if (!Command.commands.containsKey(c.getCommandName()) || forceOverride) {
			Command.register(c);
		} else {
			System.out.println("This command already exists: "
					+ c.getCommandName());
			System.exit(1);
		}
	}

	public static void addRetCommand(ReturningCommand c, boolean forceOverride) {
		if (!ReturningCommand.retCommands.containsKey(c.getRetCommandName())
				|| forceOverride) {
			ReturningCommand.register(c);
		} else {
			System.out
					.println("This command with return value already exists: "
							+ c.getRetCommandName());
			System.exit(1);
		}
	}

	public static CalcThread getCalcThread() {
		return instance;
	}

	public static void addTickHandler(TickHandler th) {
		getCalcThread().getTickHandlers().add(th);
	}

	public static void addStaticSolid(String name, Class<? extends Solid> c) {
		staticSolids.put(name, c);
	}

	public static HashMap<String, Class<? extends Solid>> getStaticSolids() {
		return staticSolids;
	}

	public static String getUserDir() {
		return userDir;
	}

	public static String getFileSeperator() {
		return sep;
	}

	public static LogStream getLogger() {
		return out;
	}

	// Log streams
	static class TextStream extends PrintStream {

		public TextStream(String fileName) throws FileNotFoundException {
			super(fileName);
		}

		public TextStream(LogStream os) {
			super(os);
		}

		public TextStream() throws IOException {
			super(new LogStream());
		}
	}

	public static class LogStream extends OutputStream {
		File outFile;
		FileOutputStream fos;
		String line = "";

		public LogStream() throws IOException {
			String s = DimRegistry.getFileSeperator();
			String base = DimRegistry.getUserDir() + s + "logs" + s;
			Path logLate = Paths.get(base + "latest.log");
			Path log1 = Paths.get(base + "log1.log");
			Path log2 = Paths.get(base + "log2.log");
			Path log3 = Paths.get(base + "log3.log");
			Files.deleteIfExists(log3);
			if (Files.exists(log2)) {
				Files.move(log2, log3);
			}
			if (Files.exists(log1)) {
				Files.move(log1, log2);
			}
			if (Files.exists(logLate)) {
				Files.move(logLate, log1);
			}
			outFile = new File(DimRegistry.getUserDir() + s + "logs" + s
					+ "latest.log");
			fos = new FileOutputStream(outFile);
		}

		@Override
		public void write(int b) throws IOException {
			line += (char) b;
			if (b == '\n') {
				CommandListener.output.append(line);
				old.print(line);
				fos.write(line.getBytes());
				fos.flush();
				line = "";
			}
		}

		public void logFile(String s) {
			try {
				old.print(s + "\r\n");
				fos.write((s + "\r\n").getBytes());
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void logException(Throwable x) {
			String out = x.toString() + "\r\n";
			StackTraceElement[] trace = x.getStackTrace();
			for (StackTraceElement t : trace) {
				out += "\tat " + t.toString() + "\r\n";
			}
			logFile(out);
			Throwable[] suppressed = x.getSuppressed();
			for (Throwable t : suppressed) {
				System.out.println("Suppressed exception:");
				logException(t);
			}
		}
	}

}
