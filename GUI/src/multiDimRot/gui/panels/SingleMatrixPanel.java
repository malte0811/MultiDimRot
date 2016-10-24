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

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import multiDimRot.gui.DialogHelper;
import multiDimRot.gui.Main;
import multiDimRot.gui.list.ColoredCellRenderer;
import multiDimRot.gui.transformations.Projection;
import multiDimRot.gui.transformations.Rotation;
import multiDimRot.gui.transformations.Scaling;
import multiDimRot.gui.transformations.Transformation;
import multiDimRot.gui.transformations.Translation;

public class SingleMatrixPanel extends ParamPanel {
	DefaultListModel<Transformation> model;
	String paramPrefix;
	String title;
	public SingleMatrixPanel(String pp, String t) {
		paramPrefix = pp;
		title = t;
	}
	@Override
	public void addTo(Group oHor, Group oVert, GroupLayout l, JFrame frame) {
		ParallelGroup vert = l.createParallelGroup();
		SequentialGroup hor = l.createSequentialGroup();

		model = new DefaultListModel<>();

		JList<Transformation> transformations = new JList<>(model);
		transformations.setCellRenderer(new ColoredCellRenderer(true));
		transformations.setBorder(BorderFactory.createEtchedBorder());
		transformations.setMaximumSize(new Dimension(200, 200));
		transformations.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(transformations);

		ParallelGroup iHor = l.createParallelGroup();
		SequentialGroup iVert = l.createSequentialGroup();

		JButton delete = new JButton("Delete");
		delete.addActionListener((e)->{
			int min = transformations.getSelectionModel().getMinSelectionIndex();
			int max = transformations.getSelectionModel().getMaxSelectionIndex();
			if (min==-1) {
				min = 0;
				max = 0;
			}
			model.removeRange(min, max);
		});

		JButton rotate = new JButton("Rotate");
		rotate.addActionListener((c)->{
			Rotation t = DialogHelper.showRotationDialog(frame, Main.INSTANCE.dimensions, false);
			if (t!=null) {
				model.addElement(t);
				transformations.repaint();
			}
		});
		JButton translate = new JButton("Translate");
		translate.addActionListener((c)->{
			Translation t = DialogHelper.showTranslationDialog(frame, Main.INSTANCE.dimensions);
			if (t!=null) {
				model.addElement(t);
				transformations.repaint();
			}
		});
		JButton scale = new JButton("Scale");
		scale.addActionListener((c)->{
			Scaling t = DialogHelper.showScalingDialog(frame, Main.INSTANCE.dimensions);
			if (t!=null) {
				model.addElement(t);
				transformations.repaint();
			}
		});

		JButton project = new JButton("Project");
		project.addActionListener((c)->{
			Projection t = DialogHelper.showProjectionDialog(frame, Main.INSTANCE.dimensions);
			if (t!=null) {
				model.addElement(t);
				transformations.repaint();
			}
		});

		vert.addComponent(scroll);
		hor.addComponent(scroll);
		iHor.addComponent(delete);
		iVert.addComponent(delete);
		iVert.addGap(10);
		iHor.addComponent(rotate);
		iVert.addComponent(rotate);
		iHor.addComponent(scale);
		iVert.addComponent(scale);
		iHor.addComponent(translate);
		iVert.addComponent(translate);
		iHor.addComponent(project);
		iVert.addComponent(project);
		vert.addGroup(iVert);
		hor.addGroup(iHor);
		oHor.addGroup(hor);
		oVert.addGroup(vert);
	}

	@Override
	public String getParam() {
		String ret = paramPrefix+" ";
		for (int i = 0;i<model.size();i++) {
			ret+=model.getElementAt(i).getParameterString(false)+" ";
		}
		return ret;
	}

	@Override
	public boolean isFinished() {
		int dims = Main.INSTANCE.dimensions;
		for (int i = 0;i<model.size();i++) {
			if (!model.getElementAt(i).isValid(dims)) {
				return false;
			}
		}
		return true;
	}
	@Override
	public String getTitle() {
		return title;
	}
}
