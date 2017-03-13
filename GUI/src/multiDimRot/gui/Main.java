/*******************************************************************************
 * This file is part of MultiDimRot2.0.
 * Copyright (C) 2016-2017 malte0811
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
 ******************************************************************************/
package multiDimRot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import multiDimRot.gui.panels.DimensionCountPanel;
import multiDimRot.gui.panels.MatrixVectorPanel;
import multiDimRot.gui.panels.ParamPanel;
import multiDimRot.gui.panels.MiscPanel;
import multiDimRot.gui.panels.SingleMatrixPanel;
import multiDimRot.gui.panels.polytope.PolytopePanel;

public class Main  {
	public static Main INSTANCE;
	private List<ParamPanel> panels = new ArrayList<>();
	public JFrame frame;
	public int dimensions = -1;

	public static void main(String[] args) {
		new Main().start();
	}
	private void start() {
		String os = System.getProperty("os.name");
		final String end;
		if (os.startsWith("Windows")) {
			end = ".exe";
		} else if (os.equals("Linux")) {
			end = "";
		} else {
			JOptionPane.showMessageDialog(null, "Your OS \""+os+"\" is not supported (yet)", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// test MultiDimRot executable
		try {
			Runtime.getRuntime().exec(System.getProperty("user.dir")+"/MultiDimRot2.0"+end+" invalidArgument");
		} catch (Exception x) {
			x.printStackTrace();
			JOptionPane.showMessageDialog(null, "The MultiDimRot executable file was not found. This is probably an installation error.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		INSTANCE = this;
		panels.add(new DimensionCountPanel());
		panels.add(new PolytopePanel());
		panels.add(new MiscPanel());
		panels.add(new MatrixVectorPanel("--startMats", "Start matrix", 0));
		panels.add(new SingleMatrixPanel("--powerMat", "Power matrix"));
		panels.add(new MatrixVectorPanel("--endMats", "End matrix", 1));


		frame = new JFrame("MultiDimRot");
		JPanel panel = new JPanel();
		GroupLayout l = new GroupLayout(panel);

		ParallelGroup hor = l.createParallelGroup();
		SequentialGroup vert = l.createSequentialGroup();

		for (int i = 0;i<panels.size();i++) {
			ParamPanel p = panels.get(i);
			JLabel label = new JLabel(p.getTitle());
			vert.addComponent(label);
			vert.addGap(5);
			hor.addComponent(label);
			p.addTo(hor, vert, l, frame);
			if (i<panels.size()-1) {
				JSeparator sep = new JSeparator();
				sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
				vert.addComponent(sep);
				hor.addComponent(sep);
			}
		}
		JButton launch = new JButton("Launch");
		launch.setForeground(Color.GREEN);
		launch.addActionListener((a)->{
			String cmd = System.getProperty("user.dir")+File.separator+"MultiDimRot2.0"+end+" ";
			for (ParamPanel p:panels) {
				if (!p.isFinished()) {
					int res = JOptionPane.showOptionDialog(frame, "\""+p.getTitle()+"\" is not ready for launch", "Error", 0, JOptionPane.ERROR_MESSAGE, null, new String[]{"Ok", "Launch default configuration"}, null);
					if (res==1) {
						try {
							Runtime.getRuntime().exec(cmd);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					return;
				}
				cmd+=p.getParam();
			}
			try {
				Runtime.getRuntime().exec(cmd);
				System.out.println(cmd);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "Could not launch MultiDimRot", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		hor.addComponent(launch);
		vert.addComponent(launch);
		l.setHorizontalGroup(hor);
		l.setVerticalGroup(vert);
		panel.setLayout(l);
		frame.add(new JScrollPane(panel));
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
