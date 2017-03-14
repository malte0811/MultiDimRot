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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.util.function.DoubleConsumer;

public class NumberDocFilter extends DocumentFilter {
	private boolean allowDouble;
	private DoubleConsumer onChanged;
	public NumberDocFilter(boolean allowDouble, DoubleConsumer onChange) {
		this.allowDouble = allowDouble;
		onChanged = onChange;
	}
	@Override
	public void insertString(FilterBypass fb, int offset, String string,
							 AttributeSet attr) throws BadLocationException {
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.insert(offset, string);
		String newText = sb.toString();
		if (test(newText)) {
			super.insertString(fb, offset, string, attr);
			try {
				onChanged.accept(Double.parseDouble(newText));
			} catch (NumberFormatException x) {
				onChanged.accept(0);
			}
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text,
						AttributeSet attrs) throws BadLocationException {
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.replace(offset, offset + length, text);

		String newText = sb.toString();
		if (test(newText)) {
			super.replace(fb, offset, length, text, attrs);
			try {
				onChanged.accept(Double.parseDouble(newText));
			} catch (NumberFormatException x) {
				onChanged.accept(0);
			}
		}
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		super.remove(fb, offset, length);
		Document doc = fb.getDocument();
		String text = doc.getText(0, doc.getLength());
		try {
			onChanged.accept(Double.parseDouble(text));
		} catch (NumberFormatException x) {
			onChanged.accept(0);
		}
	}

	private boolean test(String text) {
		try {
			if (text.equals("-")) {
				return true;
			}
			if (allowDouble) {
				if (text.equals(".")) {
					return true;
				}
				Double.parseDouble(text);
			} else {
				Integer.parseInt(text);
			}
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
