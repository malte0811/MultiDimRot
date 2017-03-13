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
package multiDimRot.gui.panels;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MiscPanel extends ParamPanel {
	JCheckBox[] renderType = new JCheckBox[3];
	JTextField threadCount = new JTextField(Integer.toString(Runtime.getRuntime().availableProcessors()));

	@Override
	public void addTo(Group hor, Group vert, GroupLayout l, JFrame main) {
		renderType[0] = new JCheckBox("Render vertices", false);
		renderType[1] = new JCheckBox("Render edges", true);
		renderType[2] = new JCheckBox("Render faces", false);
		for (int i = 0;i<3;i++) {
			hor.addComponent(renderType[i]);
			vert.addComponent(renderType[i]);
		}
		SequentialGroup iHor = l.createSequentialGroup();
		ParallelGroup iVert = l.createParallelGroup();
		JLabel tCLabel = new JLabel("Thread count (set to 1 to disable multithreading):");
		iHor.addComponent(tCLabel);
		iVert.addComponent(tCLabel);
		threadCount.setMaximumSize(new Dimension(200, threadCount.getHeight()));
		iHor.addComponent(threadCount);
		iVert.addComponent(threadCount);
		hor.addGroup(iHor);
		vert.addGroup(iVert);
	}

	@Override
	public String getParam() {
		String ret = "--misc renderType ";
		for (int i = 0;i<3;i++) {
			ret += renderType[i].isSelected()?'1':'0';
		}
		ret += " ";
		if (!threadCount.getText().isEmpty()) {
			int tCount = Integer.parseInt(threadCount.getText());
			if (tCount<1) {
				tCount = 1;
			}
			ret += "threadCount "+tCount+" ";
		}
		return ret;
	}

	@Override
	public boolean isFinished() {
		return threadCount.getText().isEmpty()||threadCount.getText().matches("^-?\\d+$");
	}

	@Override
	public String getTitle() {
		//TODO how do you spell the full word?
		return "Misc";
	}

}
