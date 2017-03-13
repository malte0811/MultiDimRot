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

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.GroupLayout.Group;
import javax.swing.JFrame;
import javax.swing.JList;

import multiDimRot.gui.panels.ParamPanel;

public class PolytopePanel extends ParamPanel {
	private List<Polytope> parts = new ArrayList<>();
	@Override
	public void addTo(Group oHor, Group oVert, GroupLayout l, JFrame frame) {
		DefaultListModel<Polytope> model = new DefaultListModel<>();
		JList<Polytope> list = new JList<>(model);
		list.setCellRenderer(new PolyRenderer());
		list.setMinimumSize(new Dimension(500, 100));
		list.setBorder(BorderFactory.createEtchedBorder());
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_DELETE) {
					removeSelection(list, model);
				}
			}
		});
		JButton addNew = new JButton("Add new polytope");
		addNew.addActionListener((a)->{
			Polytope p = Polytope.showGUI();
			if (p!=null) {
				model.addElement(p);
				parts.add(p);
				p.onAdded();
			}
		});
		JButton remove = new JButton("Remove selected polytopes");
		remove.addActionListener((a)->{
			removeSelection(list, model);
		});
		oHor.addComponent(list);
		oVert.addComponent(list);
		oHor.addComponent(addNew);
		oVert.addComponent(addNew);
		oHor.addComponent(remove);
		oVert.addComponent(remove);
	}

	private void removeSelection(JList list, DefaultListModel model) {
		int[] selected = list.getSelectedIndices();
		for (int i = selected.length-1;i>=0;i--) {
			parts.remove(selected[i]);
			model.remove(selected[i]);
		}
	}

	@Override
	public String getParam() {
		String ret = "--polytope ";
		for (Polytope pol:parts) {
			ret += pol.getParam()+" ";
		}
		return ret;
	}

	@Override
	public boolean isFinished() {
		if (parts.size()==0) {
			return false;
		}
		boolean ret = true;
		for (Polytope p:parts) {
			ret &= p.checkValid();
		}
		return ret;
	}
	@Override
	public String getTitle() {
		return "Choose Polytope";
	}
}
