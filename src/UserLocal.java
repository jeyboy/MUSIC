import java.util.HashMap;

import javax.swing.JFrame;

import Media.Media;

import service.Errorist;

public class UserLocal {
	public static void main(String[] args) {
		JFrame frame = new JFrame("(O_o)");
		frame.addWindowListener(new service.CloseDialog(frame));
		components.MainWnd.init(frame);
		
		try { components.Tray.Add(); }
		catch (Exception e) { Errorist.printLog(e); }	
		
		Media m;
		try { 
			m = new Media("C:/01 - My Last Breath.flac");
			HashMap<String, Object> ty = m.getProperties();
			System.out.print(ty + "");
		}
		catch(Exception w) {w.printStackTrace();}
	} 	
}