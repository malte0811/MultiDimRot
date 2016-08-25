package multiDimRot.gui.list;

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
	final static Color BACK_DEFAULT = UIManager.getColor ( "Panel.background" );
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
