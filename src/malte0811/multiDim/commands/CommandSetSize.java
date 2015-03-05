package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.commands.programs.Programm;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class CommandSetSize extends Command {

	@Override
	public String getCommandName() {
		return "SETSIZE";
	}

	@Override
	public String getCommandUsage() {
		return "\"setsize x y\" sets the size of the OpenGL window to x*y";
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("This command requires 2 arguments");
			return;
		}
		int x = (int) Programm.getValue(args[0]);
		int y = (int) Programm.getValue(args[1]);
		Display.setDisplayMode(new DisplayMode(x, y));
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

}
