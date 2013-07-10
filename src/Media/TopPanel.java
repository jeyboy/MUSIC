package media;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import service.Common;

public class TopPanel extends JPanel {	
	private static final long serialVersionUID = -8494337077932106633L;
	TitlePanel title_panel = new TitlePanel();
	
	void GUI() {
    	setBackground(Common.color_background);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(getSize().width, 20));
		setVisible(false);
	}
    public TopPanel() {
    	this.add(title_panel);
		this.add(new PlayerPanel());
		
		GUI();
    }
    
    public void setTitle(String title) { title_panel.setTitle(title); }
}