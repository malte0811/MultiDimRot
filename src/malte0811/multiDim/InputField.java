package malte0811.multiDim;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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
			String lastComp = "\r";
			int lastInd = 0;
			List<String> lastPossible = null;

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					ctrlDown = true;
				}

				if (active) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						DimRegistry.getCalcThread().getCommandListener().textOut
								.println(">" + getText());
						DimRegistry.getCalcThread().addCommand(getText());
						comms.addFirst(getText());
						setText("");
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
						String out = "";
						if (lastSpace == 0) {
							Set<String> keys = Command.commands.keySet();
							if (!lastComp.equals(toComplete)) {
								lastPossible = new ArrayList<>();
								ArrayList<String> a = getFiles(
										new File(DimRegistry.getUserDir()
												+ DimRegistry
														.getFileSeperator()
												+ "scripts"), toComplete);
								for (String s : a) {
									lastPossible.add(s);
									if (out.equals("")) {
										out += s;
									} else {
										out += ", " + s;
									}
								}
							}
							for (String c : keys) {
								if (c.length() >= toComplete.length()
										&& c.substring(0, toComplete.length())
												.equalsIgnoreCase(toComplete)) {
									if (!lastComp.equals(toComplete)) {
										lastPossible.add(c);
									}
									if (out.equals("")) {
										out += c;
									} else {
										out += ", " + c;
									}
								}
							}
						} else {
							int wordI = -1;
							char[] txt = getText().toCharArray();
							char lastI = 'x';
							for (char i : txt) {
								if (i == ' ' && lastI != ' ') {
									wordI++;
								}
								lastI = i;
							}
							StringTokenizer st = new StringTokenizer(getText());
							String cmd = st.nextToken();
							if (Command.commands.containsKey(cmd.toUpperCase())) {
								if (!lastComp.equalsIgnoreCase(toComplete)) {
									lastPossible = Command.commands.get(
											cmd.toUpperCase()).getCompletion(
											wordI, toComplete);
									lastInd = 0;
								}
								lastComp = toComplete;

							}
						}
						if (!lastPossible.isEmpty()) {
							String sub1 = currText.substring(0,
									lastSpace == 0 ? 0 : (lastSpace + 1));
							String sub2 = currText.substring(pos);
							int index = toComplete.equals(lastComp) ? (lastInd + 1)
									% lastPossible.size()
									: 0;

							if (!lastComp.equals(toComplete)) {
								System.out.println(out);
							}
							lastInd = index;
							setText(sub1 + lastPossible.get(index) + sub2);
							lastComp = lastPossible.get(index);
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

	protected ArrayList<String> getFiles(File base, String startsWith) {
		HashSet<String> files = listFilesForFolder(base);
		ArrayList<String> possible = new ArrayList<>();
		int startLength = startsWith.length();
		for (String s : files) {
			if (startLength <= s.length()
					&& s.substring(0, startLength).equalsIgnoreCase(startsWith)) {
				possible.add(s);
			}
		}
		return possible;
	}

	private HashSet<String> listFilesForFolder(File folder) {
		HashSet<String> ret = new HashSet<>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				ret.add(fileEntry.getName());
			}
		}
		return ret;
	}
}
