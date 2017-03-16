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
import multiDimRot.gui.gui.NumberDocFilter;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.Formatter;
import java.util.Locale;
import java.util.function.IntPredicate;

public class AddIntPolytope extends Polytope {
	private String type;
	private String display;
	private int addInt;
	public AddIntPolytope(String t, String d, int aI) {
		type = t;
		display = d;
		addInt = aI;
	}
	@Override
	public String getParam() {
		return type+" "+ Main.INSTANCE.dimensions+" "+addInt;
	}

	@Override
	public boolean checkValid() {
		return Main.INSTANCE.dimensions>=0;
	}

	@Override
	public void onAdded() {

	}

	@Override
	public String getDisplayString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb, Locale.ENGLISH);
		f.format(display, Main.INSTANCE.dimensions, addInt);
		return sb.toString();
	}
	public static class AddIntFactory extends PolytopeFactory {
		private final String type;
		private final String display;
		private final String displayBase;
		private final IntPredicate isValid;
		private final int initValue;
		private JTextField in;
		public AddIntFactory(String t, String disp, String dispBase, int init, IntPredicate valid) {
			type = t;
			display = disp;
			displayBase = dispBase;
			isValid = valid;
			initValue = init;
		}
		@Override
		public Polytope create() {
			try {
				return new AddIntPolytope(type, display, Integer.parseInt(in.getText()));
			} catch (NumberFormatException x) {
				return new AddIntPolytope(type, display, 0);
			}
		}

		@Override
		public JRadioButton add(GroupLayout gl, GroupLayout.ParallelGroup hor, GroupLayout.SequentialGroup vert) {
			JRadioButton ret = new JRadioButton(displayBase);
			ret.addChangeListener(e -> in.setEnabled(ret.isSelected()));
			in = new JTextField(Integer.toString(initValue));
			in.setMaximumSize(new Dimension(100, 20));
			in.setMinimumSize(new Dimension(50, 20));
			in.setEnabled(false);
			((PlainDocument)in.getDocument()).setDocumentFilter(new NumberDocFilter(false, value -> setValid(isValid())));
			GroupLayout.ParallelGroup iVert = gl.createParallelGroup();
			GroupLayout.SequentialGroup iHor = gl.createSequentialGroup();
			iVert.addComponent(ret);
			iHor.addComponent(ret);
			iVert.addComponent(in);
			iHor.addComponent(in);
			hor.addGroup(iHor);
			vert.addGroup(iVert);
			return ret;
		}

		@Override
		public boolean isValid() {
			String s = in.getText();
			try {
				return isValid.test(Integer.parseInt(s));
			} catch (NumberFormatException x) {
				return isValid.test(0);
			}
		}
	}
}
