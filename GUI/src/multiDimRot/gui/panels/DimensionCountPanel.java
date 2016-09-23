package multiDimRot.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import multiDimRot.gui.Main;

public class DimensionCountPanel extends ParamPanel {
	private JLabel result;
	private JTextField dimIn;
	@Override
	public void addTo(Group hor, Group vert, GroupLayout l, JFrame frame) {
		SequentialGroup iHor = l.createSequentialGroup();
		ParallelGroup iVert = l.createBaselineGroup(true, false);
		JLabel tmp = new JLabel("Dimensions: ");
		iHor.addComponent(tmp);
		iVert.addComponent(tmp);
		dimIn = new JTextField() {
			private static final long serialVersionUID = -509853485283732383L;

			@Override
			public void paint(Graphics g) {
				int curr = -1;
				try {
					curr = Integer.parseInt(getText());
				} catch (NumberFormatException x) {}
				if (Main.INSTANCE.dimensions>=0&&curr!=Main.INSTANCE.dimensions) {
					setText(Integer.toString(Main.INSTANCE.dimensions));
				}
				super.paint(g);
			}
		};
		dimIn.setMaximumSize(new Dimension(200, 25));
		result = new JLabel(" ");
		dimIn.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateResult();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateResult();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateResult();
			}

		});
		iHor.addComponent(dimIn);
		iVert.addComponent(dimIn);
		hor.addGroup(iHor);
		vert.addGroup(iVert);
		hor.addComponent(result);
		vert.addComponent(result);
	}
	@Override
	public String getParam() {
		return "";
	}

	@Override
	public boolean isFinished() {
		updateResult();
		return result.getForeground()!=Color.RED;
	}
	private void updateResult() {
		result.setForeground(Color.RED);
		try {
			Main.INSTANCE.dimensions = Integer.parseInt(dimIn.getText());
			if (Main.INSTANCE.dimensions<0) {
				result.setText("Dimension count is invalid");
			} else {
				result.setForeground(Color.BLACK);
				result.setText("Dimension count is valid");
				Main.INSTANCE.frame.repaint();
			}
		} catch (NumberFormatException n) {
			result.setText("Dimension count is not a number");
			Main.INSTANCE.dimensions = -1;
		}
	}
	@Override
	public String getTitle() {
		return "Dimension count";
	}
}
