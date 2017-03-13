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

import java.util.function.Supplier;

import javax.swing.GroupLayout;
import javax.swing.JRadioButton;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

public class PolytopeFactory {
	String title;
	Supplier<Polytope> create;
	public PolytopeFactory() {}
	public PolytopeFactory(String t, Supplier<Polytope> c) {
		title = t;
		create = c;
	}
	public Polytope create() {
		return create.get();
	}
	public JRadioButton add(GroupLayout gl, ParallelGroup hor, SequentialGroup vert) {
		JRadioButton b = new JRadioButton(title);
		hor.addComponent(b);
		vert.addComponent(b);
		return b;
	}
}