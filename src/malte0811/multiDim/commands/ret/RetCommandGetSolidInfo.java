package malte0811.multiDim.commands.ret;

import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.addons.ReturningCommand;
import malte0811.multiDim.commands.programs.Programm;

public class RetCommandGetSolidInfo extends ReturningCommand {

	@Override
	public String getRetCommandName() {
		return "GETSOLIDINFO";
	}

	@Override
	public String getRetCommandUsage() {
		return "\"getSolidInfo(\"vertexCount\")\" returns the number of vertices the current solid has\r\n"
				+ "\"getSolidInfo(\"dimensions\")\" returns the dimension of the current solid: 1 for a line, 2 for a square, 3 for a cube, ...\r\n"
				+ "\"getSolidInfo(\"vertexElement\", v, a)\" returns the coordinate of the vertex v of the current solid on the axis a\r\n"
				+ "\"getSolidInfo(\"edgeCount\")\" returns the amount of edges the current solid has\r\n"
				+ "\"getSolidInfo(\"edgeElement\", e, v)\" returns the index of the vertex v of the edge e (if the edge e goes from vertex 2 to vertex 1, v=0 returns 2 and v=1 returns 1)\r\n"
				+ "\"getSolidInfo(\"sideCount\")\" returns the amount of sides the current solid has, or -1 if it has no sides\r\n"
				+ "\"getSolidInfo(\"sideVertex\", s, v)\" returns the index of the vertex v of the side s";
	}

	@Override
	public double processCommand(String[] args) {
		String a1 = Programm.getStringValue(args[0]);
		switch (a1) {
		case "vertexCount":
			return DimRegistry.getCalcThread().getSolid().vertices.length;
		case "dimensions":
			return DimRegistry.getCalcThread().getSolid().vertices[0].length;
		case "vertexElement":
			if (args.length != 3) {
				System.out
						.println("getVertexElement requires 2 extra parameters: vertex and axis");
				return 0;
			}
			int v = (int) Programm.getDoubleValue(args[1]);
			int a = (int) Programm.getDoubleValue(args[2]);
			return DimRegistry.getCalcThread().getSolid().vertices[v][a];
		case "edgeCount":
			return DimRegistry.getCalcThread().getSolid().getEdges().length;
		case "edgeElement":
			if (args.length != 3) {
				System.out
						.println("getVertexElement requires 2 extra parameters: vertex and axis");
				return -1;
			}
			int e = (int) Programm.getDoubleValue(args[1]);
			a = (int) Programm.getDoubleValue(args[2]);
			if (a < 0 || a > 1) {
				System.out.println("Each edge has exactly 2 vertices");
				return -1;
			} else if (e < 0
					|| e > DimRegistry.getCalcThread().getSolid().getEdges().length) {
				System.out
						.println("The current solid does not have a edge with the index "
								+ e);
				return -1;
			}
			return DimRegistry.getCalcThread().getSolid().getEdges()[e][a];
		case "sideCount":
			if (DimRegistry.getCalcThread().getSolid().getSides() != null) {
				return DimRegistry.getCalcThread().getSolid().getSides().length;
			} else {
				System.out.println("This solid does not have sides");
				return -1;
			}
		case "sideVertex":
			if (args.length != 3) {
				System.out
						.println("getVertexElement requires 2 extra parameters: vertex and axis");
				return 0;
			}
			int s = (int) Programm.getDoubleValue(args[1]);
			a = (int) Programm.getDoubleValue(args[2]);
			if (DimRegistry.getCalcThread().getSolid().getSides() == null) {
				System.out.println("This solid does not have sides");
				return -1;
			} else if (a < 0 || a > 2) {
				System.out.println("Each side has exactly 3 vertices");
				return -1;
			} else if (s < 0
					|| s > DimRegistry.getCalcThread().getSolid().getSides().length) {
				System.out
						.println("The current solid does not have a side with the index "
								+ s);
				return -1;
			} else {
				return DimRegistry.getCalcThread().getSolid().getSides()[s][a];

			}
		}
		System.out.println("Invalid argument: " + args[0]);
		return 0;
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
