package drop_panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class DropPanelsManager {
	public Shell wnd;
	
	public DropPanel drop_top;
	public DropPanel drop_bottom;
	
	public Button arrow_top;
	public Button arrow_bottom;	
	
	public DropPanelsManager(Shell parent) { wnd = parent; }
	
	public void saveDropPanels() {
		drop_top.Save(service.Settings.drop_top_path);
		drop_bottom.Save(service.Settings.drop_bottom_path);	
	}
	
	void HidePanel(DropPanel panel, boolean hide) {
		GridData gridData = (GridData)panel.getLayoutData();
		gridData.exclude = hide;
		panel.setLayoutData(gridData);
		panel.setVisible(!hide);
		wnd.pack();
	}

	public void ToogleTopDrop() 	{ HidePanel(drop_top, drop_top.isVisible()); }
	public void ToogleBottomDrop() 	{ HidePanel(drop_bottom, drop_bottom.isVisible());	}
	
	public void CloseAll() {
		HidePanel(drop_top, true);
		HidePanel(drop_bottom, true);
	}
	
	public void initializeUpArrowButtons() {
		drop_top = new DropPanel(wnd);
		drop_top.Load(service.Settings.drop_top_path);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 0);
		gridData.heightHint = 15;
		drop_top.setLayoutData(gridData);
		
		arrow_top = new Button(wnd, SWT.ARROW | SWT.UP);
		gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 0);
		gridData.heightHint = 10;
		arrow_top.setLayoutData(gridData);
		
		arrow_top.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event arg0) { ToogleTopDrop(); }
				});
	}
	
	public void initializeDownArrowButtons() {
		arrow_bottom = new Button(wnd, SWT.ARROW | SWT.DOWN);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 0);
		gridData.heightHint = 10;		
		arrow_bottom.setLayoutData(gridData);
		
		arrow_bottom.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) { ToogleBottomDrop(); }
		});
		
		drop_bottom = new DropPanel(wnd);
		drop_bottom.Load(service.Settings.drop_bottom_path);
		gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 0);
		gridData.heightHint = 15;
		drop_bottom.setLayoutData(gridData);
	}	
}
