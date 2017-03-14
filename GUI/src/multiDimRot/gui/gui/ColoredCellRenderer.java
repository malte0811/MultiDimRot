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
package multiDimRot.gui.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import multiDimRot.gui.Main;
import multiDimRot.gui.transformations.Transformation;

public class ColoredCellRenderer extends JLabel implements ListCellRenderer<Transformation> {
	private static final long serialVersionUID = 1L;
	private final static Color BACK_DEFAULT = UIManager.getColor ( "Panel.background" );
	private boolean multible;
	public ColoredCellRenderer(boolean m) {
		multible = m;
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends Transformation> list, Transformation value,
			int index, boolean isSelected, boolean cellHasFocus) {
		setText(value.getHumanString(multible));
		setOpaque(true);
		setMinimumSize(new Dimension(list.getWidth(), 0));
		Color c = value.isValid(Main.INSTANCE.dimensions)?Color.BLACK:Color.RED;
		if (isSelected) {
			setBackground(Color.BLACK);
			setForeground(invert(c));
		} else {
			setBackground(BACK_DEFAULT);
			setForeground(c);
		}
		return this;
	}
	private Color invert(Color c) {
		return new Color(255-c.getRed(), 255-c.getGreen(), 255-c.getBlue());
	}
}
