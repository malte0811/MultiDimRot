package malte0811.multiDim;

import java.util.Arrays;

import malte0811.multiDim.addons.DimRegistry;

public class CleaningThread extends Thread {
	public void run() {
		while (true) {
			try {
				Thread.sleep(20000);
				int[][] newRots = new int[0][3];
				for (int[] i : DimRegistry.getCalcThread().rotations) {
					boolean found = false;
					for (int[] i2 : newRots) {
						if (i2[0] == i[0] && i2[1] == i[1]) {
							i2[2] += i[2];
							found = true;
							break;
						}
					}
					if (!found) {
						newRots = Arrays.copyOf(newRots, newRots.length + 1);
						int index = newRots.length - 1;
						newRots[index] = i;
					}
				}
				DimRegistry.getCalcThread().rotations = newRots;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
