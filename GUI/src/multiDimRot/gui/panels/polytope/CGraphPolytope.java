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
import javax.swing.GroupLayout.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class CGraphPolytope extends Polytope {
	private final String function;
	private final float rStart;
	private final float iStart;
	private final float rStep;
	private final float iStep;
	private final int rCount;
	private final int iCount;
	public CGraphPolytope(String function, float rStart, float iStart, float rStep, float iStep, int rCount, int iCount) {
		this.function = function;
		this.rStart = rStart;
		this.iStart = iStart;
		this.rStep = rStep;
		this.iStep = iStep;
		this.rCount = rCount;
		this.iCount = iCount;
	}
	@Override
	public String getParam() {
		return "cGraph "+function+" "+rStart+" "+rStep+" "+rCount+" "+iStart+" "+iStep+" "+iCount;
	}

	@Override
	public boolean checkValid() {
		return Main.INSTANCE.dimensions==4;
	}

	@Override
	public void onAdded() {
		if (!checkValid()) {
			Main.INSTANCE.dimensions = 4;
			Main.INSTANCE.frame.repaint();
		}
	}

	@Override
	public String getDisplayString() {
		float rMax = rStart+rStep*rCount;
		float iMax = iStart+iStep*iCount;
		String start = rStart+""+(iStart<0?iStart:"+"+iStart)+"*I";
		String end = rMax+""+(iMax<0?iMax:"+"+iMax)+"*I";
		return "Complex Graph: "+function+" on range ["+start+"; "+end+"]";
	}
	public static class CGraphFactory extends PolytopeFactory {
		JTextField function;
		JTextField rStart;
		JTextField iStart;
		JTextField rStep;
		JTextField iStep;
		JTextField rCount;
		JTextField iCount;
		@Override
		public Polytope create() {
			return new CGraphPolytope(function.getText(), getValue(rStart), getValue(iStart), getValue(rStep), getValue(iStep), (int)getValue(rCount), (int)getValue(iCount));
		}

		@Override
		public JRadioButton add(GroupLayout gl, ParallelGroup hor, SequentialGroup vert) {
			JRadioButton ret = new JRadioButton("Complex Graph");
			ret.addChangeListener(e -> {
				function.setEnabled(ret.isSelected());
				rStart.setEnabled(ret.isSelected());
				iStart.setEnabled(ret.isSelected());
				rStep.setEnabled(ret.isSelected());
				iStep.setEnabled(ret.isSelected());
				rCount.setEnabled(ret.isSelected());
				iCount.setEnabled(ret.isSelected());
			});

			function = new JTextField();
			function.setEnabled(false);
			function.setMinimumSize(new Dimension(0, 20));
			function.setMaximumSize(new Dimension(200, 20));

			SequentialGroup iHor = gl.createSequentialGroup();
			ParallelGroup iVert = gl.createParallelGroup();
			addToGroups(ret, iHor, iVert);
			addToGroups(function, iHor, iVert);
			hor.addGroup(iHor);
			vert.addGroup(iVert);
			addRangeChooser(gl, hor, vert);
			return ret;
		}
		private void addRangeChooser(GroupLayout gl, ParallelGroup horMain, SequentialGroup vert) {
			JLabel start = new JLabel("Start:");
			JLabel step = new JLabel("Step Size: ");
			JLabel count = new JLabel("Vertex Count:");
			JLabel re = new JLabel("Real:");
			JLabel im = new JLabel("Imaginary:");
			rStart = newNumberField(true);
			iStart = newNumberField(true);
			rStep = newNumberField(true);
			iStep = newNumberField(true);
			rCount = newNumberField(false);
			iCount = newNumberField(false);
			Group hor = gl.createSequentialGroup();
			Group col0 = gl.createParallelGroup();
			Group col1 = gl.createParallelGroup();
			Group col2 = gl.createParallelGroup();
			Group row0 = gl.createParallelGroup();
			Group row1 = gl.createParallelGroup();
			Group row2 = gl.createParallelGroup();
			Group row3 = gl.createParallelGroup();

			addToGroups(start, row1, col0);
			addToGroups(step, row2, col0);
			addToGroups(count, row3, col0);
			addToGroups(re, row0, col1);
			addToGroups(im, row0, col2);
			addToGroups(rStart, row1, col1);
			addToGroups(rStep, row2, col1);
			addToGroups(rCount, row3, col1);
			addToGroups(iStart, row1, col2);
			addToGroups(iStep, row2, col2);
			addToGroups(iCount, row3, col2);

			hor.addGap(30);
			hor.addGroup(col0);
			hor.addGroup(col1);
			hor.addGroup(col2);

			vert.addGroup(row0);
			vert.addGroup(row1);
			vert.addGroup(row2);
			vert.addGroup(row3);
			horMain.addGroup(hor);
		}
		private void addToGroups(Component c, Group row, Group col) {
			row.addComponent(c);
			col.addComponent(c);
		}
		private JTextField newNumberField(boolean allowDouble) {
			JTextField ret = new JTextField();
			((PlainDocument)ret.getDocument()).setDocumentFilter(new NumberDocFilter(allowDouble, value -> setValid(isValid())));
			ret.setMinimumSize(new Dimension(0, 20));
			ret.setMaximumSize(new Dimension(100, 20));
			ret.setEnabled(false);
			return ret;
		}

		@Override
		public boolean isValid() {
			if (function.getText().trim().isEmpty()) {
				return false;
			}
			float rSta = getValue(rStart);
			if (isNaN(rSta)) {
				return false;
			}
			float iSta = getValue(iStart);
			if (isNaN(iSta)) {
				return false;
			}
			float rSte = getValue(rStep);
			if (isNaN(rSte)||rSte<=0) {
				return false;
			}
			float iSte = getValue(iStep);
			if (isNaN(iSte)||iSte<=0) {
				return false;
			}
			float rC = getValue(rCount);
			if (isNaN(rC)||rC<=0) {
				return false;
			}
			float iC = getValue(iCount);
			if (isNaN(iC)||iC<=0) {
				return false;
			}

			return true;
		}
		private float getValue(JTextField t) {
			try {
				return Float.parseFloat(t.getText());
			} catch (NumberFormatException x) {
				return Float.NaN;
			}
		}
		private boolean isNaN(float in) {
			return in!=in;
		}
	}
}
