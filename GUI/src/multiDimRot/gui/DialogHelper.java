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

import java.awt.Component;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import multiDimRot.gui.transformations.Projection;
import multiDimRot.gui.transformations.Rotation;
import multiDimRot.gui.transformations.Scaling;
import multiDimRot.gui.transformations.Translation;

public class DialogHelper {
	private DialogHelper() {}
	public static Rotation showRotationDialog(Component root, int dims, boolean allowInc) {
		if (dims<0) {
			JOptionPane.showMessageDialog(root, "Please enter the dimension count first", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		JPanel p = new JPanel();
		GroupLayout l = new GroupLayout(p);
		ParallelGroup vert = l.createParallelGroup();
		SequentialGroup hor = l.createSequentialGroup();
		JTextField tA0 = addTextFieldAndLabel("First axis", hor, vert, l);
		JTextField tA1 = addTextFieldAndLabel("Second axis", hor, vert, l);
		JTextField tAngle = addTextFieldAndLabel("Angle", hor, vert, l);
		JCheckBox inc = new JCheckBox("Continuous");
		if (allowInc) {
			vert.addComponent(inc);
			hor.addComponent(inc);
		}
		l.setHorizontalGroup(hor);
		l.setVerticalGroup(vert);
		p.setLayout(l);
		int result = JOptionPane.showOptionDialog(root, p, "Rotation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, JOptionPane.UNINITIALIZED_VALUE);
		if (result==JOptionPane.OK_OPTION) {
			try {
				int a0 = Integer.parseInt(tA0.getText());
				int a1 = Integer.parseInt(tA1.getText());
				float angle = Float.parseFloat(tAngle.getText());
				if (a0>=dims||a0<0||a1>=dims||a1<0||a0==a1||angle==0) {
					JOptionPane.showMessageDialog(root, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					return new Rotation(a0, a1, angle, inc.isSelected());
				}
			} catch (NumberFormatException x) {
				x.printStackTrace();
				JOptionPane.showMessageDialog(root, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
			return null;
	}
	public static Translation showTranslationDialog(Component root, int dims) {
		if (dims<0) {
			JOptionPane.showMessageDialog(root, "Please enter the dimension count first", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		JPanel p = new JPanel();
		GroupLayout l = new GroupLayout(p);
		ParallelGroup vert = l.createParallelGroup();
		SequentialGroup hor = l.createSequentialGroup();
		JTextField tAxis = addTextFieldAndLabel("Axis", hor, vert, l);
		JTextField tDist = addTextFieldAndLabel("Distance", hor, vert, l);
		l.setHorizontalGroup(hor);
		l.setVerticalGroup(vert);
		p.setLayout(l);
		int result = JOptionPane.showOptionDialog(root, p, "Translation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, JOptionPane.UNINITIALIZED_VALUE);
		if (result==JOptionPane.OK_OPTION) {
			try {
				int axis = Integer.parseInt(tAxis.getText());
				float distance = Float.parseFloat(tDist.getText());
				if (axis>=dims||axis<0||distance==0) {
					JOptionPane.showMessageDialog(root, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					return new Translation(axis, distance);
				}
			} catch (NumberFormatException x) {
				x.printStackTrace();
				JOptionPane.showMessageDialog(root, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return null;
	}
	public static Scaling showScalingDialog(Component root, int dims) {
		if (dims<0) {
			JOptionPane.showMessageDialog(root, "Please enter the dimension count first", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		JPanel p = new JPanel();
		GroupLayout l = new GroupLayout(p);
		ParallelGroup vert = l.createParallelGroup();
		SequentialGroup hor = l.createSequentialGroup();
		JCheckBox all = new JCheckBox("All directions");
		vert.addComponent(all);
		hor.addComponent(all);
		JTextField tAxis = addTextFieldAndLabel("Axis", hor, vert, l);
		JTextField tFact = addTextFieldAndLabel("Factor", hor, vert, l);
		all.addChangeListener((e)->{
			tAxis.setEnabled(!all.isSelected());
		});
		l.setHorizontalGroup(hor);
		l.setVerticalGroup(vert);
		p.setLayout(l);
		int result = JOptionPane.showOptionDialog(root, p, "Scale", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, JOptionPane.UNINITIALIZED_VALUE);
		if (result==JOptionPane.OK_OPTION) {
			try {
				int axis = all.isSelected()?-1:Integer.parseInt(tAxis.getText());
				float factor = Float.parseFloat(tFact.getText());
				if (axis>=dims||(axis<0&&!all.isSelected())||factor==0||factor==1) {
					JOptionPane.showMessageDialog(root, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					return new Scaling(axis, factor);
				}
			} catch (NumberFormatException x) {
				x.printStackTrace();
				JOptionPane.showMessageDialog(root, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return null;
	}
	public static Projection showProjectionDialog(Component root, int dims) {
		if (dims<0) {
			JOptionPane.showMessageDialog(root, "Please enter the dimension count first", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		JPanel p = new JPanel();
		GroupLayout l = new GroupLayout(p);
		ParallelGroup vert = l.createParallelGroup();
		SequentialGroup hor = l.createSequentialGroup();
		JCheckBox all = new JCheckBox("Down to 2 dimensions");
		vert.addComponent(all);
		hor.addComponent(all);
		JTextField tDim = addTextFieldAndLabel("Dimension", hor, vert, l);
		JTextField tDist = addTextFieldAndLabel("Distance", hor, vert, l);
		all.addChangeListener((e)->{
			tDim.setEnabled(!all.isSelected());
		});
		l.setHorizontalGroup(hor);
		l.setVerticalGroup(vert);
		p.setLayout(l);
		int result = JOptionPane.showOptionDialog(root, p, "Projection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, JOptionPane.UNINITIALIZED_VALUE);
		if (result==JOptionPane.OK_OPTION) {
			try {
				int dim = all.isSelected()?-1:(Integer.parseInt(tDim.getText())+1);
				float distance = Float.parseFloat(tDist.getText());
				if (dim>=dims||(dim<0&&!all.isSelected())||distance==0) {
					JOptionPane.showMessageDialog(root, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					return new Projection(dim, distance);
				}
			} catch (NumberFormatException x) {
				x.printStackTrace();
				JOptionPane.showMessageDialog(root, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return null;
	}
	private static JTextField addTextFieldAndLabel(String text, Group hor, Group vert, GroupLayout l) {
		JTextField ret = new JTextField();
		JLabel label = new JLabel(text);
		ParallelGroup iHor = l.createParallelGroup();
		SequentialGroup iVert = l.createSequentialGroup();
		iHor.addComponent(label);
		iVert.addComponent(label);
		iHor.addComponent(ret);
		iVert.addComponent(ret);
		hor.addGroup(iHor);
		vert.addGroup(iVert);
		return ret;
	}

}
