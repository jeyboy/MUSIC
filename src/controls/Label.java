package controls;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import service.Common;

public class Label extends JLabel {
	private static final long serialVersionUID = 1L;

	public Label(String text, int left_pad, int right_pad) {
		super(text);
		setForeground(Common.color_foreground);
		setBorder(BorderFactory.createEmptyBorder(0, left_pad, 0, right_pad));		
	}
}
