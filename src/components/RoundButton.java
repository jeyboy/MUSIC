package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

public class RoundButton extends JButton {
	private static final long serialVersionUID = -8057459718216730510L;
	Shape shape;
	
	void Common() {
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);
		setBackground(service.Common.color_background);

		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);			
	}
	
	public RoundButton(String label) { super(label); Common(); }
	public RoundButton(Icon icon) { super(icon); Common(); }

	protected void paintComponent(Graphics g) {
		if (getModel().isArmed())
			g.setColor(Color.DARK_GRAY);
		else g.setColor(getBackground());
		g.fillOval(1, 1, getSize().width-2, getSize().height-2);

		super.paintComponent(g);
	}
		  
	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds()))
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		return shape.contains(x, y);
	}
}
