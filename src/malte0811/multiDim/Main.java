package malte0811.multiDim;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import malte0811.multiDim.commands.ser.CommandAdd;
import malte0811.multiDim.solids.HyperCube;
import malte0811.multiDim.solids.Solid;

public class Main {
	CalcThread panel;

	public Main(Solid c) {
		try {
			System.setOut(new PrintStream(new FileOutputStream(
					FileDescriptor.out), false, "cp850"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		panel = new CalcThread(c);
		Thread t = new Thread(panel);
		t.start();

	}

	public static void main(String[] args) {
		String[] p = new CommandAdd().getFiles(
				new File(System.getProperty("user.dir") + "/scripts"),
				"psychede");
		for (String s : p) {
			System.out.println(s);
		}
		Main m = new Main(new HyperCube());
	}

}
