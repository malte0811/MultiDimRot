package multiDimRot.gui.panels;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JFrame;

public abstract class ParamPanel {
	public abstract void addTo(Group hor, Group vert, GroupLayout l, JFrame main);
	public abstract String getParam();
	public abstract boolean isFinished();
	public abstract String getTitle();
}
