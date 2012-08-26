import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class UserLocal {
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		components.MainWnd.init(shell);
		shell.setBackground(new Color(Display.getCurrent(), 0,0,0));
		 
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