package malte0811.multiDim;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;

import javax.swing.JTextField;

import malte0811.multiDim.addons.Command;
import malte0811.multiDim.addons.DimRegistry;
import malte0811.multiDim.commands.programs.Programm;

public class InputField extends JTextField {
	ArrayDeque<String> comms = new ArrayDeque<>();
	String currCmd = null;
	boolean active = true;

	public InputField() {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (active) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						DimRegistry.getCalcThread().c.out.println(">"
								+ getText());
						try {
							DimRegistry.getCalcThread().addCommand(getText());
							comms.addFirst(getText());
							setText("");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						if (currCmd != null) {
							comms.addLast(new String(currCmd));
							currCmd = null;
						}
						int oldPos = getCaretPosition();
						setText(comms.pollFirst());
						if (oldPos != 0)
							setCaretPosition(Math.min(getText().length(),
									oldPos));
						currCmd = getText();
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if (currCmd != null) {
							comms.addFirst(new String(currCmd));
							currCmd = null;
						}
						int oldPos = getCaretPosition();
						setText(comms.pollLast());
						if (oldPos != 0)
							setCaretPosition(Math.min(getText().length(),
									oldPos));
						currCmd = getText();
					}
				} else {
					System.out.println("locked");
					if (e.getKeyCode() == KeyEvent.VK_X) {
						Programm.stop = true;
					}
					setText("");
				}
				super.keyTyped(e);
			}
		});
		this.setSize(300, 50);
	}

	public void toggle() {
		active = !active;
	}

	public void processCommand() throws Exception {
		String cmd = getText();
		comms.addFirst(cmd);
		setText("");
		if (!Command.processCommand(cmd, false)) {

			DimRegistry.getCalcThread().programmRunning = Programm.load(cmd);
			if (DimRegistry.getCalcThread().programmRunning != null) {
				toggle();
			}
		}
	}
}
