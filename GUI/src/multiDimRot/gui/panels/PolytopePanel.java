/*******************************************************************************
 * This file is part of MultiDimRot2.0.
 * Copyright (C) 2016 malte0811
 *
 * MultiDimRot2.0 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MultiDimRot2.0 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MultiDimRot2.0.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package multiDimRot.gui.panels;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import multiDimRot.gui.Main;

import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PolytopePanel extends ParamPanel {
	String polytope;
	String path = "";
	Map<String, JTextField> extraData = new HashMap<>();
	private static Map<String, String> polytopes;
	static {
		polytopes = new HashMap<>();
		polytopes.put("n-dimensional Cube", "cube -d");
		polytopes.put("n-dimensional Cross-polytope", "crosspolytope -d");
		polytopes.put("n-dimensional Simplex", "simplex -d");
		polytopes.put("n-dimensional Sphere", "sphere -e");
		polytopes.put("24-Cell", "24Cell -4");
		polytopes.put("(N)Obj file", "obj -o");
	}
	@Override
	public void addTo(Group oHor, Group oVert, GroupLayout l, JFrame frame) {
		ParallelGroup hor = l.createParallelGroup();
		SequentialGroup vert = l.createSequentialGroup();

		JRadioButton[] buttons = new JRadioButton[polytopes.size()];
		ButtonGroup bGroup = new ButtonGroup();
		Iterator<String> it = polytopes.keySet().iterator();
		for (int i = 0;i<buttons.length;i++) {
			String curr = it.next();
			buttons[i] = new JRadioButton(curr);
			bGroup.add(buttons[i]);
			int iCopy = i;
			buttons[i].addActionListener((a)->{
				polytope = polytopes.get(curr);
				if (polytope.endsWith("o")) {
					JFileChooser c = new JFileChooser(System.getProperty("user.dir"));
					c.showOpenDialog(frame);
					File f = c.getSelectedFile();
					System.out.println(f);
					if (f==null) {
						polytope = null;
						buttons[iCopy].setSelected(false);
					} else {
						try {
							FileInputStream fis = new FileInputStream(f);
							int dimensions = 3;
							while (fis.available()>0) {
								String line = "";
								int next;
								while ((next = fis.read())!=-1&&next!='\n'&&next!='\r') {
									line += (char) next;
								}
								if (line.startsWith("dims ")) {
									dimensions = Integer.parseInt(line.substring(5));
									break;
								}
							}
							if (dimensions>=0) {
								path = f.getAbsolutePath();
								Main.INSTANCE.dimensions = dimensions;
								Main.INSTANCE.frame.repaint();
							}
							fis.close();
						} catch (Exception x) {
							x.printStackTrace();
							JOptionPane.showMessageDialog(frame, "An error occured: "+x.getClass(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				} else if (Character.isDigit(polytope.charAt(polytope.length()-1))) {
					Main.INSTANCE.dimensions = Integer.parseInt(polytope.substring(polytope.length()-1, polytope.length()));
					Main.INSTANCE.frame.repaint();
				}
				for (String s:extraData.keySet()) {
					extraData.get(s).setEnabled(s.equals(polytope));
				}
			});
			String p = polytopes.get(curr);
			if (p.endsWith("-e")) {
				GroupLayout.ParallelGroup iVert = l.createParallelGroup();
				GroupLayout.SequentialGroup iHor = l.createSequentialGroup();
				JTextField input = new JTextField();
				input.setMaximumSize(new Dimension(200, buttons[i].getHeight()));
				input.setEnabled(false);
				iVert.addComponent(buttons[i]);
				iHor.addComponent(buttons[i]);
				iVert.addComponent(input);
				iHor.addComponent(input);
				hor.addGroup(iHor);
				vert.addGroup(iVert);
				extraData.put(p, input);
			} else {
				hor.addComponent(buttons[i]);
				vert.addComponent(buttons[i]);
			}
		}
		oHor.addGroup(hor);
		oVert.addGroup(vert);
	}

	@Override
	public String getParam() {
		String end = "";
		if (polytope.endsWith("o")) {
			end = path;
		} else if (polytope.endsWith("d")) {
			end = Integer.toString(Main.INSTANCE.dimensions);
		} else if (polytope.endsWith("e")) {
			end = Integer.toString(Main.INSTANCE.dimensions)+" "+extraData.get(polytope).getText();
		}
		return "--polytope "+polytope.substring(0, polytope.length()-2)+end+" ";
	}

	@Override
	public boolean isFinished() {
		return polytope!=null;
	}
	@Override
	public String getTitle() {
		return "Choose Polytope";
	}
}
