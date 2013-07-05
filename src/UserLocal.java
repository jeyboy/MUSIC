import javax.swing.JFrame;

import service.Errorist;
import uis.ToolTipUI;

public class UserLocal {
	public static void main(String[] args) {
		new ToolTipUI();
		JFrame frame = new JFrame("(O_o)");
		frame.addWindowListener(new service.CloseDialog(frame));
		components.MainWnd.init(frame);
		
		try { components.Tray.Add(); }
		catch (Exception e) { Errorist.printLog(e); }
	} 	
}