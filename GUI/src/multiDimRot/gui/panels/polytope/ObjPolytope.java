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

package multiDimRot.gui.panels.polytope;

import multiDimRot.gui.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ObjPolytope extends Polytope {
	private final String path;
	private int dimCount;
	private ObjPolytope(String path) {
		this.path = path;
		dimCount = getDimCount(path);
	}
	@Override
	public String getParam() {
		return "obj "+path;
	}

	@Override
	public boolean checkValid() {
		dimCount = getDimCount(path);
		return Main.INSTANCE.dimensions==dimCount&&dimCount>0;
	}

	@Override
	public void onAdded() {
		if (!checkValid()&&dimCount>=0) {
			Main.INSTANCE.dimensions = dimCount;
			Main.INSTANCE.frame.repaint();
		}
	}

	@Override
	public String getDisplayString() {
		return dimCount+"-dimensional Obj loaded from "+path;
	}

	private static int getDimCount(String path) {
		Path p = Paths.get(path);
		if (!Files.exists(p)||!Files.isRegularFile(p)) {
			return -1;
		}
		int dimensions = 3;
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			while (fis.available()>0) {
				String line = "";
				int next;
				while ((next = fis.read())!=-1&&next!='\n'&&next!='\r') {
					line += (char) next;
				}
				if (line.startsWith("dims ")) {
					dimensions = Integer.parseInt(line.substring(5).trim());
					break;
				}
			}
		} catch (IOException|NumberFormatException e) {
			e.printStackTrace();
			dimensions = -1;
		}
		return dimensions;
	}
	public static class ObjFactory extends PolytopeFactory {
		JTextField pathOut;
		@Override
		public Polytope create() {
			return new ObjPolytope(pathOut.getText());
		}

		@Override
		public JRadioButton add(GroupLayout gl, GroupLayout.ParallelGroup hor, GroupLayout.SequentialGroup vert) {
			JButton choose = new JButton("Choose file...");
			choose.setEnabled(false);
			choose.addActionListener(e -> {
				JFileChooser c = new JFileChooser(System.getProperty("user.dir"));
				c.setMultiSelectionEnabled(false);
				c.showDialog(Main.INSTANCE.frame, "Select");
				if (c.getSelectedFile()!=null) {
					pathOut.setText(c.getSelectedFile().getAbsolutePath());
					setValid(isValid());
				}
			});
			JRadioButton ret = new JRadioButton("(N)OBJ-File");
			ret.addChangeListener(e -> {
				pathOut.setEnabled(ret.isSelected());
				choose.setEnabled(ret.isSelected());
			});
			pathOut = new JTextField();
			pathOut.setEnabled(false);
			pathOut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
			pathOut.setMaximumSize(new Dimension(200, 20));
			pathOut.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (ret.isSelected()) {
						setValid(isValid());
					}
				}
			});
			GroupLayout.ParallelGroup iVert = gl.createParallelGroup();
			GroupLayout.SequentialGroup iHor = gl.createSequentialGroup();
			iVert.addComponent(ret);
			iHor.addComponent(ret);
			iVert.addComponent(pathOut);
			iHor.addComponent(pathOut);
			iVert.addComponent(choose);
			iHor.addComponent(choose);
			hor.addGroup(iHor);
			vert.addGroup(iVert);
			return ret;
		}

		@Override
		public boolean isValid() {
			return getDimCount(pathOut.getText())>=0;
		}
	}
}
