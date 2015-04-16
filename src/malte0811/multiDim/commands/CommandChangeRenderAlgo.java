package malte0811.multiDim.commands;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;
import malte0811.multiDim.render.RenderAlgo;

public class CommandChangeRenderAlgo extends Command {

	@Override
	public String getCommandName() {
		return "CHANGERENDERALGO";
	}

	@Override
	public void processCommand(String[] args) {
		int r = (int) Programm.getValue(args[0]);
		RenderAlgo algo = null;
		try {
			algo = DimRegistry.getAlgoInstance(r);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		DimRegistry.getCalcThread().setRenderAlgo(algo);
		DimRegistry.getCalcThread().setRenderOptions(algo.getInitialParams());
	}

	@Override
	public String getCommandUsage() {
		return "\"changerenderalgo <newAlgo>\" changes the rendering technique. 0: Orthogonal projection, 1-3: perspective rendering, increasing quality";
	}

	@Override
	public int getMinParameterCount() {
		return 1;
	}

}
