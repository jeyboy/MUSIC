package components;

import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

public class ArrowButton extends BasicArrowButton {
	private static final long serialVersionUID = 5990513402920013546L;
	public enum Turn { LEFT, RIGHT, TOP, BOTTOM };

	private static int getDirection(Turn turn) {
		switch (turn) {
			case BOTTOM: 	return SwingConstants.SOUTH;
			case TOP: 		return SwingConstants.NORTH;
			case LEFT: 		return SwingConstants.WEST;
			case RIGHT: 	return SwingConstants.EAST;
			default: 		return -1;
		}
	}
	
	public ArrowButton(Turn direction) { this(getDirection(direction)); }	
	public ArrowButton(int direction) { 
		super(direction);
//		Dimension dim = getPreferredSize();
//		if (direction == SwingConstants.SOUTH || direction == SwingConstants.NORTH)
//			setPreferredSize(new Dimension(dim.width, 10));
//		else setPreferredSize(new Dimension(10, dim.height));	
	}

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
