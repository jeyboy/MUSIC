import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import service.Errorist;

public class UserLocal {
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		components.MainWnd.init(shell);
		 
		shell.pack ();
		shell.open ();
		while (!shell.isDisposed()) {
		 if (!display.readAndDispatch()) 
		  display.sleep();
		}
		display.dispose();		
//		
//		try { components.Tray.Add(); }
//		catch (Exception e) { Errorist.printLog(e); }
	} 	
}