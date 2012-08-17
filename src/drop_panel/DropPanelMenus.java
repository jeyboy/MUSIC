package drop_panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class DropPanelMenus {
	DropPanelDialogs dialogs;
	
	public DropPanelMenus(DropPanelDialogs panel_dialogs) {
		dialogs = panel_dialogs;
	}
	
	public void SetContainerMenu(DropPanel panel) {
		JPopupMenu menu = new JPopupMenu();
		menu.add(AddMenuItem("Add item"));
		panel.setComponentPopupMenu(menu);
	}
	
	public void SetItemMenu(DropPanelItem item) {
		JPopupMenu menu = new JPopupMenu();
		menu.add(AddMenuItem("Create new item"));
		menu.add(DeleteMenuItem(item, "Delete current"));
		menu.add(ModifyMenuItem(item, "Change current"));
		item.setComponentPopupMenu(menu);
	}
	
	JMenuItem AddMenuItem(String text) {
	    JMenuItem m = new JMenuItem(text);
	    m.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		dialogs.addDropItemDialog();
	        }
	    });
	    return m;
	}
	
	JMenuItem DeleteMenuItem(final DropPanelItem item, String text) {
	    JMenuItem m = new JMenuItem(text);
	    m.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		dialogs.container.DropItem(item);
	        }
	    });
	    return m;
	}
	
	JMenuItem ModifyMenuItem(final DropPanelItem item, String text) {
	    JMenuItem m = new JMenuItem(text);
	    m.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		dialogs.modDropItemDialog(item);
	        }
	    });
	    return m;
	}	
}
