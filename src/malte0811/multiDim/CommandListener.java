package malte0811.multiDim;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class CommandListener extends JFrame {
	public InputField input = new InputField();
	public static JTextArea output = new JTextArea(0, 30);
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
		output.setLineWrap(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		old = System.out;

		this.setTitle("MultiDimRot console");
		this.setSize(400, 400);
		this.setVisible(true);
	}

}
