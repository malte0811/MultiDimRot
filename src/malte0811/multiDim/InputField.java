package malte0811.multiDim;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
			boolean ctrlDown = false;
			String lastComp = "";
			int lastInd = 0;

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					ctrlDown = true;
				}

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
					if (e.getKeyCode() == KeyEvent.VK_SPACE && ctrlDown) {
						String toComplete = "";
						String currText = getText();
						int pos = getCaretPosition();
						int lastSpace = pos - 1;
						for (; lastSpace >= 0
								&& currText.charAt(lastSpace) != ' '; lastSpace--)
							;
						if (lastSpace < 0) {
							lastSpace = 0;
						}
						for (int i = lastSpace; i < pos; i++) {
							if (currText.charAt(i) == ' ') {
								i++;
								if (i >= pos) {
									break;
								}
							}
							toComplete += currText.charAt(i);
						}
						Set<String> keys = Command.commands.keySet();
						List<String> possible = new ArrayList<>();
						String out = "";
						for (String c : keys) {
							if (c.length() >= toComplete.length()
									&& c.substring(0, toComplete.length())
											.equalsIgnoreCase(toComplete)) {
								possible.add(c);
								if (out.equals("")) {
									out += c;
								} else {
									out += ", " + c;
								}
							}
						}
						if (!possible.isEmpty()) {
							// TODO use last completion to determine next
							String sub1 = currText.substring(0, lastSpace);
							String sub2 = currText.substring(pos);
							int index = toComplete.equals(lastComp) ? (lastInd + 1)
									% possible.size()
									: 0;
							setText(sub1 + possible.get(0) + sub2);
							lastComp = possible.get(0);
							System.out.println(out);
						}
					}
				} else {
					if (e.getKeyCode() == KeyEvent.VK_X) {
						Programm.stop = true;
					}
					setText("");
				}
				super.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					ctrlDown = false;
				}
				super.keyReleased(e);
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
