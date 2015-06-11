package malte0811.multiDim.commands;

import java.util.Arrays;
import java.util.StringTokenizer;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;

public class CommandRun extends Command {

	@Override
	public String getCommandName() {
		return "RUN";
	}

	@Override
	public String getCommandUsage() {
		return "\"run <p>\" runs the variable p as a program, as if the value of p was stored in a file";
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		String[] c = new String[0];
		StringTokenizer st = new StringTokenizer(
				Programm.getStringValue(args[0]), "\r\n");
		while (st.hasMoreTokens()) {
			c = Arrays.copyOf(c, c.length + 1);
			c[c.length - 1] = st.nextToken();
		}
		Programm p = new Programm(c);
		if (DimRegistry.getCalcThread().getCurrentProgram() == null) {
			DimRegistry.getCalcThread().setCurrentProgram(p);
		} else {
			DimRegistry.getCalcThread().getCurrentProgram().innerProgramm = p;
		}
	}

}
