package malte0811.multiDim;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;

import malte0811.multiDim.addons.AddonLoader;
import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.addons.TickHandler;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.render.ParallelRender;
import malte0811.multiDim.render.RenderAlgo;
import malte0811.multiDim.solids.Solid;
import malte0811.multiDim.tickHandlers.DebugHandler;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * email an niklasnickel@yahoo.de
 * 
 */
public class CalcThread implements Runnable {
	public Solid solid = null;
	public int[][] rotations = new int[0][3];
	public double[] renderOptions = { 5, 3 };
	public Programm programmRunning = null;
	public Color background = Color.GRAY;
	public double zoomMax = 5;
	public double zoomStep = 0;
	public RenderAlgo algo = null;
	public HashSet<TickHandler> handlers = new HashSet<>();
	public CommandListener c;
	ArrayDeque<String> commands = new ArrayDeque<>();

	public CalcThread(Solid s) {
		solid = s;
		DimRegistry.instance = this;
	}

	private void init() throws LWJGLException {
		Display.setVSyncEnabled(true);
		Display.setDisplayMode(new DisplayMode(800, 600));
		Display.create();
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		try {
			algo = DimRegistry.getAlgoInstance(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Display.setTitle("MultiDimRot");
	}

	@Override
	public void run() {
		try {
			init();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		RenderAlgo.init();

		// create directories
		String[] dirs = { "tmp", "addons", "logs", "screenshots", "videos",
				"scripts" };
		for (String s : dirs) {
			Path p = Paths.get(System.getProperty("user.dir") + "\\" + s);
			if (!Files.exists(p)) {
				try {
					Files.createDirectories(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		c = new CommandListener();
		try {
			AddonLoader.load();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		int timer = 0;
		long time = System.currentTimeMillis();
		new CleaningThread().start();
		while (!Display.isCloseRequested()) {
			for (TickHandler th : handlers) {
				th.handleTick(solid, renderOptions);
			}
			processCommands();
			time = System.currentTimeMillis();
			for (int[] i : rotations) {
				solid.rotate(i[0], i[1], i[2]);
			}
			if (timer > 0) {
				timer--;
			}
			if (timer == 0 && programmRunning != null) {
				timer = programmRunning.step();
				if (Programm.stop) {
					timer = 0;
					programmRunning = null;
					Programm.stop = false;
					c.input.active = true;
				}
				if (timer == 0) {
					programmRunning = null;
					c.input.active = true;
				}
			}
			if (!(algo instanceof ParallelRender)
					&& renderOptions[0] != zoomMax && zoomStep != 0) {
				if (renderOptions[0] > zoomMax) {
					renderOptions[0] -= zoomStep;
					if (renderOptions[0] <= zoomMax) {
						zoomStep = 0;
					}
				} else {
					renderOptions[0] += zoomStep;
					if (renderOptions[0] >= zoomMax) {
						zoomStep = 0;
					}
				}
			}
			paint();
			Display.update();
			try {
				long tDiff = System.currentTimeMillis() - time;
				DebugHandler.getInstance().addTime(3, (int) tDiff);
				Thread.sleep(100 - (tDiff > 100 ? 0 : tDiff));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		Display.destroy();
		System.exit(0);
	}

	protected void paint() {
		if (algo == null) {
			return;
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		// GL11.glClearColor(1, 1, 1, 1);
		int dims = 0;
		if (algo instanceof ParallelRender) {
			dims = (int) (renderOptions[0] > renderOptions[1] ? renderOptions[0]
					: renderOptions[1]);
		}
		double[][] vertices = solid.getCopyOfVertices(dims > 2 ? dims : 2);
		int[][] oldEdges = solid.getEdges();
		int[][] edges = new int[oldEdges.length][];
		for (int i = 0; i < edges.length; i++) {
			edges[i] = Arrays.copyOf(oldEdges[i], 2);
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		algo.render(vertices, edges, renderOptions, solid.getColors(),
				solid.getSides());
	}

	public void addRotCon(int[] value) {
		rotations = Arrays.copyOf(rotations, rotations.length + 1);
		rotations[rotations.length - 1] = value;
	}

	public void addCommand(String c) {
		commands.addLast(c);
	}

	private void processCommands() {
		while (!commands.isEmpty()) {
			String cmd = commands.pollFirst();
			if (!Command.processCommand(cmd, false)) {
				try {
					DimRegistry.getCalcThread().programmRunning = Programm
							.load(cmd);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (DimRegistry.getCalcThread().programmRunning != null) {
					c.input.toggle();
					break;
				}
			}
		}
	}
}
