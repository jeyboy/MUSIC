package components;

import javax.swing.JFrame;

public class TWindow {
	public static JFrame create(String title) {
		return new JFrame(title);
	}	
	
	public static JFrame create(String title, int width, int height) {
		JFrame new_frame = create(title);
		new_frame.setSize(width, height);
		
//		new_frame.setLocationRelativeTo(null);
//		new_frame.setLocation(x, y);
		
		return new_frame;
	}
}
