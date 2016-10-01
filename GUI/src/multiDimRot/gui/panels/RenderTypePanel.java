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
		vertices = new JCheckBox("Render vertices");
		edges = new JCheckBox("Render edges");
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
