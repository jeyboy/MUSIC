package uis;

import java.awt.Color;
import javax.swing.UIManager;

public class ToolTipUI {
	public ToolTipUI() {			
		UIManager.put("ToolTip.background", Color.darkGray);
		UIManager.put("ToolTip.foreground", Color.white);
	}
}
