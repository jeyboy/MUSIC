package media;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import controls.Label;
import service.Common;

public class TitlePanel extends JPanel {	
	private static final long serialVersionUID = -4120310985111446039L;
	Label track_name;

	void GUI() {
    	setBackground(Common.color_background);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}
    public TitlePanel() {
		this.add(track_name = new Label("-----------", 1, 1));
		
		GUI();
    }
    public void setTitle(String title) { track_name.setText(title); }
}