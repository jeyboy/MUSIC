package components;

import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

public class ArrowButton extends BasicArrowButton {
	private static final long serialVersionUID = 5990513402920013546L;
	
	public ArrowButton(int direction) { super(direction); }

	public void Toogle() {
		switch (getDirection()) {
			case SwingConstants.NORTH:
				setDirection(SwingConstants.SOUTH);
				break;
			case SwingConstants.EAST:
				setDirection(SwingConstants.WEST);
				break;
			case SwingConstants.SOUTH:
				setDirection(SwingConstants.NORTH);
				break;
			case SwingConstants.WEST:
				setDirection(SwingConstants.EAST);
				break;
		}
	}
}
