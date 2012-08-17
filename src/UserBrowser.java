import java.applet.Applet;
import java.awt.Graphics;

public class UserBrowser extends Applet {
	private static final long serialVersionUID = 3247658310641804528L;	
	//for repainting of applet call repaint()
	
	@Override
	//called one time at start of applet
	public void init() {
		components.MainWnd.init(this);
	}
	
	@Override
	//called after initialization and after situation when user come back at page with applet
	public void start() {
		
	}
	
	@Override
	public void paint(Graphics g) {
		//more some actions
		super.paint(g);
		//more some actions
	}
	
	@Override
	//called when user go to another page. But applet not destroyed and applet can still work
	public void stop() {
		
	}
	
	@Override
	//called when browser start deleting applet
	public void destroy() {
		components.MainWnd.destroy();
	}	
}
