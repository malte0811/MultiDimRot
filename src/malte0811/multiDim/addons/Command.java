package malte0811.multiDim.addons;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import malte0811.multiDim.commands.CommandBackground;
import malte0811.multiDim.commands.CommandChangeRenderAlgo;
import malte0811.multiDim.commands.CommandChangeRenderOption;
import malte0811.multiDim.commands.CommandDebugRender;
import malte0811.multiDim.commands.CommandExit;
import malte0811.multiDim.commands.CommandHelp;
import malte0811.multiDim.commands.CommandRecord;
import malte0811.multiDim.commands.CommandResetRot;
import malte0811.multiDim.commands.CommandResizeSolid;
import malte0811.multiDim.commands.CommandRotCon;
import malte0811.multiDim.commands.CommandRotInst;
import malte0811.multiDim.commands.CommandScreenShot;
import malte0811.multiDim.commands.CommandSetSize;
import malte0811.multiDim.commands.CommandToggleSides;
import malte0811.multiDim.commands.CommandZoom;
import malte0811.multiDim.commands.ser.CommandAdd;
import malte0811.multiDim.commands.ser.CommandDeserialize;
import malte0811.multiDim.commands.ser.CommandSerialize;
import malte0811.multiDim.commands.show.CommandShowCube;
import malte0811.multiDim.commands.show.CommandShowLorenz;
import malte0811.multiDim.commands.show.CommandShowOctaeder;
import malte0811.multiDim.commands.show.CommandShowSphere;
import malte0811.multiDim.commands.show.CommandShowStatic;
import malte0811.multiDim.commands.show.CommandShowTetraeder;
import malte0811.multiDim.commands.show.CommandShowTorus;

public abstract class Command {
	public static HashMap<String, Command> commands = new HashMap<>();

	public abstract String getCommandName();

	public abstract String getCommandUsage();

	public abstract void processCommand(String[] args) throws Exception;

	public ArrayList<String> getCompletion(int i, String toComplete) {
		return new ArrayList<>();
	}

	static {
		register(new CommandChangeRenderAlgo());
		register(new CommandChangeRenderOption());
		register(new CommandExit());
		register(new CommandRotCon());
		register(new CommandRotInst());
		register(new CommandShowCube());
		register(new CommandShowSphere());
		register(new CommandShowStatic());
		register(new CommandResetRot());
		register(new CommandBackground());
		register(new CommandShowOctaeder());
		register(new CommandSerialize());
		register(new CommandDeserialize());
		register(new CommandAdd());
		register(new CommandZoom());
		register(new CommandShowTorus());
		register(new CommandShowTetraeder());
		register(new CommandResizeSolid());
		register(new CommandShowLorenz());
		register(new CommandHelp());
		register(new CommandDebugRender());
		register(new CommandScreenShot());
		register(new CommandRecord());
		register(new CommandSetSize());
		register(new CommandToggleSides());

		try {
			// only load if exists, so it doesnt appear in the master branch
			Class<?> cl = Class
					.forName("malte0811.multiDim.commands.CommandTmp");
			Command cmd = (Command) cl.asSubclass(Command.class)
					.getConstructor().newInstance();
			register(cmd);
		} catch (Exception x) {
		}
	}

	public static boolean processCommand(String command, boolean suppressWarning) {
		if (command == null) {
			return false;
		}
		StringTokenizer st = new StringTokenizer(command);
		String cmd = st.nextToken();
		String[] args = new String[0];
		while (st.hasMoreTokens()) {
			args = Arrays.copyOf(args, args.length + 1);
			args[args.length - 1] = st.nextToken();
		}
		if (!commands.containsKey(cmd.toUpperCase())) {
			if (!suppressWarning) {
				System.out.println("This command does not exist");
			}
			return false;
		}
		try {
			commands.get(cmd.toUpperCase()).processCommand(args);
		} catch (Exception x) {
			System.out.println(command);
			x.printStackTrace();
		}
		return true;
	}

	public static void register(Command c) {
		commands.put(c.getCommandName(), c);
	}

	protected String[] getFiles(File base, String startsWith) {
		HashSet<String> files = listFilesForFolder(base);
		HashSet<String> possible = new HashSet<>();
		int startLength = startsWith.length();
		for (String s : files) {
			if (startLength <= s.length()
					&& s.substring(0, startLength).equalsIgnoreCase(startsWith)) {
				possible.add(s);
			}
		}
		return possible.toArray(new String[0]);
	}

	private HashSet<String> listFilesForFolder(File folder) {
		HashSet<String> ret = new HashSet<>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				ret.add(fileEntry.getName());
			}
		}
		return ret;
	}
}
