package malte0811.multiDim;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import malte0811.multiDim.addons.DimRegistry;

public class CommandListener extends JFrame {
	public InputField input = new InputField();
	public JTextArea output = new JTextArea(0, 30);
	public TextStream out;
	public PrintStream old;
	boolean program = false;

	public CommandListener() {
		DefaultCaret caret = (DefaultCaret) output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JPanel pane = new JPanel();
		this.setLayout(new BorderLayout());
		this.add(pane, BorderLayout.CENTER);
		JScrollPane jsp = new JScrollPane(output,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GroupLayout gl = new GroupLayout(pane);
		GroupLayout.ParallelGroup hor = gl.createParallelGroup();
		hor.addComponent(jsp);
		hor.addComponent(input);
		GroupLayout.SequentialGroup ver = gl.createSequentialGroup();
		ver.addComponent(jsp);
		ver.addComponent(input, GroupLayout.PREFERRED_SIZE,
				GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		gl.setVerticalGroup(ver);
		gl.setHorizontalGroup(hor);
		pane.setLayout(gl);

		output.setEditable(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		old = System.out;
		try {
			out = new TextStream();
			System.setOut(out);
			System.setErr(out);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.setTitle("MultiDimRot console");
		this.setSize(400, 400);
		this.setVisible(true);
	}

	class TextStream extends PrintStream {

		public TextStream(String fileName) throws FileNotFoundException {
			super(fileName);
		}

		public TextStream() throws IOException {
			super(new OutStream());
		}
	}

	// TODO \r\n after n characters
	class OutStream extends OutputStream {
		File outFile;
		FileOutputStream fos;
		String line = "";

		public OutStream() throws IOException {
			String s = DimRegistry.getFileSeperator();
			String base = DimRegistry.getUserDir() + s + "logs" + s;
			Path logLate = Paths.get(base + "latest.log");
			Path log1 = Paths.get(base + "log1.log");
			Path log2 = Paths.get(base + "log2.log");
			Path log3 = Paths.get(base + "log3.log");
			Files.deleteIfExists(log3);
			if (Files.exists(log2)) {
				Files.move(log2, log3);
			}
			if (Files.exists(log1)) {
				Files.move(log1, log2);
			}
			if (Files.exists(logLate)) {
				Files.move(logLate, log1);
			}
			outFile = new File(DimRegistry.getUserDir() + s + "logs" + s
					+ "latest.log");
			fos = new FileOutputStream(outFile);
		}

		@Override
		public void write(int b) throws IOException {
			line += (char) b;
			if (b == '\n') {
				if (line.getBytes()[0] != '>') {
					output.append(line);
					// output.setCaretPosition(output.getText().length() - 1);
					old.write(line.getBytes());
				}
				fos.write(line.getBytes());
				fos.flush();
				line = "";
			}
		}

		public void logFile(String s) throws IOException {
			fos.write(s.getBytes());
			fos.flush();
		}
	}

}
