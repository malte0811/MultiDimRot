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

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

public class RenderTypePanel extends ParamPanel {
	JCheckBox vertices;
	JCheckBox edges;
	JCheckBox faces;

	@Override
	public void addTo(Group hor, Group vert, GroupLayout l, JFrame main) {
		vertices = new JCheckBox("Render vertices", false);
		edges = new JCheckBox("Render edges", true);
		faces = new JCheckBox("Render faces");
		hor.addComponent(vertices);
		vert.addComponent(vertices);
		hor.addComponent(edges);
		vert.addComponent(edges);
		hor.addComponent(faces);
		vert.addComponent(faces);
	}

	@Override
	public String getParam() {
		String ret = "--renderType ";
		if (vertices.isSelected()) {
			ret+="vertices ";
		}
		if (edges.isSelected()) {
			ret+="edges ";
		}
		if (faces.isSelected()) {
			ret+="faces ";
		}
		return ret;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public String getTitle() {
		return "Render Type";
	}

}
