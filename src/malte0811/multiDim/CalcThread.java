package malte0811.multiDim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

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

public class CalcThread implements Runnable {
	private Solid solid = null;
	private int[][] rotations = new int[0][3];
	private double[] renderOptions = { 5, 3 };
	private Programm currentProgram = null;
	private double zoomMax = 5;
	private double zoomStep = 0;
	private RenderAlgo renderAlgo = null;
	private ArrayList<TickHandler> handlers = new ArrayList<>();
	private CommandListener c;
	private ArrayDeque<String> commands = new ArrayDeque<>();
	private boolean showSides = true;

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
			renderAlgo = DimRegistry.getAlgoInstance(3);
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
				"scripts", "solids" };
		for (String s : dirs) {
			Path p = Paths.get(DimRegistry.getUserDir()
					+ DimRegistry.getFileSeperator() + s);
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
				if (th.isActive()) {
					th.handleTick(solid, renderOptions);
				}
			}
			processCommands();
			time = System.currentTimeMillis();
			for (int[] i : rotations) {
				solid.rotate(i[0], i[1], i[2]);
			}
			if (timer > 0) {
				timer--;
			}
			if (timer == 0 && currentProgram != null) {
				timer = currentProgram.step();
				if (Programm.stop) {
					timer = 0;
					currentProgram = null;
					Programm.stop = false;
					c.input.active = true;
				}
				if (timer == 0) {
					currentProgram = null;
					c.input.active = true;
				}
			}
			if (!(renderAlgo instanceof ParallelRender)
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
		if (renderAlgo == null) {
			return;
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		// GL11.glClearColor(1, 1, 1, 1);
		int dims = 0;
		if (renderAlgo instanceof ParallelRender) {
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
		renderAlgo.render(vertices, edges, renderOptions, solid.getColors(),
				showSides ? solid.getSides() : null);
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
			if (!Command.processCommand(cmd, true)) {
				try {
					DimRegistry.getCalcThread().currentProgram = Programm
							.load(cmd);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (DimRegistry.getCalcThread().currentProgram != null) {
					c.input.toggle();
					break;
				}
			}
		}
	}

	public Solid getSolid() {
		return solid;
	}

	public void setSolid(Solid solid) {
		this.solid = solid;
	}

	public int[][] getRotations() {
		return rotations;
	}

	public void setRotations(int[][] rotations) {
		this.rotations = rotations;
	}

	public double[] getRenderOptions() {
		return renderOptions;
	}

	public void setRenderOptions(double[] renderOptions) {
		this.renderOptions = renderOptions;
	}

	public Programm getCurrentProgram() {
		return currentProgram;
	}

	public void setCurrentProgram(Programm currentProgram) {
		this.currentProgram = currentProgram;
	}

	public double getZoomMax() {
		return zoomMax;
	}

	public void setZoomMax(double zoomMax) {
		this.zoomMax = zoomMax;
	}

	public double getZoomStep() {
		return zoomStep;
	}

	public void setZoomStep(double zoomStep) {
		this.zoomStep = zoomStep;
	}

	public RenderAlgo getRenderAlgo() {
		return renderAlgo;
	}

	public void setRenderAlgo(RenderAlgo renderAlgo) {
		this.renderAlgo = renderAlgo;
	}

	public ArrayList<TickHandler> getTickHandlers() {
		return handlers;
	}

	public void setTickHandlers(ArrayList<TickHandler> handlers) {
		this.handlers = handlers;
	}

	public CommandListener getCommandListener() {
		return c;
	}

	public void setCommandListener(CommandListener c) {
		this.c = c;
	}

	public void setSidesActive(boolean active) {
		showSides = active;
	}

	public boolean areSidesActive() {
		return showSides;
	}

	public void toggleTickHandler(int id) {
		handlers.get(id).setActive(!handlers.get(id).isActive());
	}
}
