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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import multiDimRot.gui.Main;
import multiDimRot.gui.panels.polytope.FixedDimPolytope.BasicPolytopeFactory;

public abstract class Polytope {
	private static List<PolytopeFactory> polytopes = new ArrayList<>();
	static {
		polytopes.add(new BasicPolytopeFactory("N-dimensional Cube", ()->new VarDimPolytope("cube", "Cube")));
		polytopes.add(new BasicPolytopeFactory("N-dimensional Simplex", ()->new VarDimPolytope("simplex", "Simplex")));
		polytopes.add(new BasicPolytopeFactory("N-dimensional Cross-Polytope", ()->new VarDimPolytope("crossPolytope", "Cross-Polytope")));
		polytopes.add(new AddIntPolytope.AddIntFactory("sphere", "%d-dimensional Sphere with resolution %d", "N-dimensional Sphere with resolution", 10, value -> value>1));
		polytopes.add(new BasicPolytopeFactory("24-Cell", ()->new FixedDimPolytope("24Cell", "24-Cell", 4)));
		polytopes.add(new ObjPolytope.ObjFactory());
	}
	// mustn't be called multiple times simultaneously!
	public static Polytope showGUI() {
		JDialog jd = new JDialog(Main.INSTANCE.frame, "Choose a polytope", true);
		JPanel jp = new JPanel();
		GroupLayout l = new GroupLayout(jp);
		ParallelGroup hor = l.createParallelGroup();
		SequentialGroup vert = l.createSequentialGroup();
		AtomicBoolean done = new AtomicBoolean(false);
		JButton ok = new JButton("OK");
		ok.setEnabled(false);
		JButton cancel = new JButton("Cancel");
		ok.addActionListener((a)->{
			done.set(true);
			jd.dispose();
		});
		cancel.addActionListener((a)->{
			jd.dispose();
		});
		Map<ButtonModel, PolytopeFactory> buttons = new HashMap<>(polytopes.size());
		ButtonGroup bGroup = new ButtonGroup();
		for (PolytopeFactory pc:polytopes) {
			JRadioButton jb = pc.add(l, hor, vert);
			pc.setOnValidFunction(ok::setEnabled);
			jb.addChangeListener(e -> {
				if (jb.isSelected()) {
					ok.setEnabled(pc.isValid());
				}
			});
			bGroup.add(jb);
			buttons.put(jb.getModel(), pc);
		}
		hor.addComponent(ok);
		vert.addComponent(ok);
		hor.addComponent(cancel);
		vert.addComponent(cancel);
		l.setHorizontalGroup(hor);
		l.setVerticalGroup(vert);
		jd.setSize(400, 400);
		jp.setLayout(l);
		jd.add(jp);
		jd.setVisible(true);//blocks until the dialog is closed
		return done.get()?buttons.get(bGroup.getSelection()).create():null;
	}
	public abstract String getParam();
	public abstract boolean checkValid();
	public abstract void onAdded();
	public abstract String getDisplayString();
}
