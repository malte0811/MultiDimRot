package multiDimRot.gui.panels;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import multiDimRot.gui.Main;

import javax.swing.JRadioButton;

public class PolytopePanel extends ParamPanel {
	String polytope;
	private static Map<String, String> polytopes;
	static {
		polytopes = new HashMap<>();
		polytopes.put("n-dimensional Cube", "cube");
		polytopes.put("n-dimensional Cross-polytope", "crosspolytope");
		polytopes.put("n-dimensional Simplex", "simplex");
	}
	@Override
	public void addTo(Group oHor, Group oVert, GroupLayout l, JFrame frame) {
		ParallelGroup hor = l.createParallelGroup();
		SequentialGroup vert = l.createSequentialGroup();

		JRadioButton[] buttons = new JRadioButton[polytopes.size()];
		ButtonGroup bGroup = new ButtonGroup();
		Iterator<String> it = polytopes.keySet().iterator();
		for (int i = 0;i<buttons.length;i++) {
			String curr = it.next();
			buttons[i] = new JRadioButton(curr);
			bGroup.add(buttons[i]);
			buttons[i].addActionListener((a)->{
				polytope = polytopes.get(curr);
			});
			hor.addComponent(buttons[i]);
			vert.addComponent(buttons[i]);
		}
		oHor.addGroup(hor);
		oVert.addGroup(vert);
	}

	@Override
	public String getParam() {
		return "--polytope "+polytope+" "+Main.INSTANCE.dimensions+" ";
	}

	@Override
	public boolean isFinished() {
		return polytope!=null;
	}
	@Override
	public String getTitle() {
		return "Choose Polytope";
	}
}
