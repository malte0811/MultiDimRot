package malte0811.multiDim.commands.show;

import java.util.Arrays;
import java.util.HashMap;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.MathHelper;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.solids.TMPSolid;

public class CommandShowRecursiveFunction extends Command {

	@Override
	public String getCommandName() {
		return "SHOWRECURSIVEFUNCTION";
	}

	@Override
	public String getCommandUsage() {
		// TODO dims, [start], iterations, keep, dims*{term}
		// variables: _[ticks before]_[dimension]
		return null;
	}

	@Override
	public int getMinParameterCount() {
		return 5;
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		int dim = (int) Programm.getDoubleValue(args[0]);
		int iter = (int) Programm.getDoubleValue(args[1 + dim]);
		int keep = (int) Programm.getDoubleValue(args[2 + dim]);
		double[] start = new double[dim];
		for (int i = 0; i < dim; i++) {
			start[i] = Programm.getDoubleValue(args[1 + i]);
		}
		double[][] vertices = new double[iter][dim];
		vertices[0] = Arrays.copyOf(start, dim);
		int[][] edges = new int[iter - 1][2];
		for (int i = 0; i < edges.length; i++) {
			edges[i][0] = i;
			edges[i][1] = i + 1;
		}
		double[][] last = new double[keep][dim];
		Programm c = DimRegistry.getCalcThread().getCurrentProgram();
		HashMap<String, Double> vars = (c == null) ? new HashMap<String, Double>()
				: c.getDoubleVariables();
		for (int i = 0; i < iter - 1; i++) {
			for (int i2 = 0; i2 < keep - 1; i2++) {
				last[i2] = last[i2 + 1];
			}
			last[keep - 1] = Arrays.copyOf(vertices[i], dim);
			for (int i2 = 0; i2 < keep; i2++) {
				for (int i3 = 0; i3 < dim; i3++) {
					vars.put("_" + i2 + "_" + i3, last[i2][i3]);
				}
			}
			for (int i2 = 0; i2 < dim; i2++) {
				vertices[i + 1][i2] = MathHelper.calculate(args[3 + dim + i2],
						vars);
			}
		}
		if (c != null) {
			for (int i = 0; i < keep; i++) {
				for (int i2 = 0; i2 < dim; i2++) {
					if (vars.containsKey("_" + i + "_" + i2)) {
						vars.remove("_" + i + "_" + i2);
					}
				}
			}
		}
		DimRegistry.getCalcThread().setSolid(
				new TMPSolid(edges, vertices, null));
	}

}
