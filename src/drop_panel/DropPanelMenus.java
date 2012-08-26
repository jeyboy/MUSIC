package drop_panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class DropPanelMenus {
	DropPanelDialogs dialogs;
	
	public DropPanelMenus(DropPanelDialogs panel_dialogs) { dialogs = panel_dialogs; }
	
	public void SetContainerMenu(DropPanel panel) {
	    final Menu menu = new Menu(panel);
		AddMenuItem(menu, "Add item");
		panel.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event arg0) { menu.setVisible(true); }
		});
	}
	
	public void SetItemMenu(DropPanelItem item) {
		final Menu menu = new Menu(item.button);
		AddMenuItem(menu, "Create new item");
		DeleteMenuItem(menu, item, "Delete current");
		ModifyMenuItem(menu, item, "Change current");
		item.button.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event arg0) { menu.setVisible(true); }
		});		
	}
	
	void AddMenuItem(Menu menu, String text) {
		MenuItem newItem = new MenuItem(menu, SWT.NONE); //SWT.CASCADE
		newItem.setText(text);
	    newItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) { dialogs.addDropItemDialog(); }
		});
	}
	
	void DeleteMenuItem(Menu menu, final DropPanelItem item, String text) {
		MenuItem newItem = new MenuItem(menu, SWT.NONE);
		newItem.setText(text);
	    newItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) { dialogs.container.DropItem(item); }
		});		
	}
	
	void ModifyMenuItem(Menu menu, final DropPanelItem item, String text) {
		MenuItem newItem = new MenuItem(menu, SWT.NONE);
		newItem.setText(text);
	    newItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) { dialogs.modDropItemDialog(item); }
		});				
	}	
}
