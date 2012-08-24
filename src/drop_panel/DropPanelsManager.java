package drop_panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class DropPanelsManager {
	public Shell wnd;
	
	public DropPanel drop_top = new DropPanel(false);
	public DropPanel drop_bottom = new DropPanel(false);
	
	public Button arrow_top;
	public Button arrow_bottom;	
	
	public DropPanelsManager(Shell parent) {
		wnd = parent;
		initializeArrowButtons();
		LoadDropPanels();
	}
	
	public void LoadDropPanels() {
		drop_top.Load(service.Settings.drop_top_path);
		drop_bottom.Load(service.Settings.drop_bottom_path);
	}
	
	public void saveDropPanels() {
		drop_top.Save(service.Settings.drop_top_path);
		drop_bottom.Save(service.Settings.drop_bottom_path);	
	}

	public void ToogleTopDrop() {
		drop_top.setVisible(!drop_top.isVisible());
//		wnd.redraw();
	}
	public void ToogleBottomDrop() {
		drop_bottom.setVisible(!drop_bottom.isVisible());
//		wnd.redraw();
	}
	
	public void CloseAll() {
		drop_top.setVisible(false);
		drop_bottom.setVisible(false);
//		wnd.redraw();
	}
	
	void initializeArrowButtons() {
		arrow_top = new Button(wnd, SWT.ARROW);
		
		arrow_top.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event arg0) { ToogleTopDrop(); }
				});
		
		arrow_bottom = new Button(wnd, SWT.ARROW);
		
		arrow_top.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) { ToogleBottomDrop(); }
		});		
	}	
}
