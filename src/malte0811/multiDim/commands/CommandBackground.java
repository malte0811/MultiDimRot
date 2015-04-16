package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.commands.programs.Programm;

import org.lwjgl.opengl.GL11;

public class CommandBackground extends Command {

	@Override
	public String getCommandName() {
		return "SETBACKGROUND";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		float r = (float) Programm.getValue(args[0]);
		float g = (float) Programm.getValue(args[1]);
		float b = (float) Programm.getValue(args[2]);
		GL11.glClearColor(r, g, b, 1);
	}

	@Override
	public String getCommandUsage() {
		return "\"setBackground <r> <g> <b>\" changes the background to the color r g b. r, g and b are greater than 0 and less than 1";
	}

	@Override
	public int getMinParameterCount() {
		return 3;
	}
}
