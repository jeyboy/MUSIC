package drop_panel;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import service.Errorist;
import service.IOOperations;

public class DropPanel extends JScrollPane {
	private static final long serialVersionUID = -9051505855046492589L;
	private int width = 30;
	public JPanel content_pane = new JPanel();
	DropPanelMenus panel_menus;
	boolean vertical = false;
	
	private void commonInit(int axis_orient) {
		panel_menus = new DropPanelMenus(new DropPanelDialogs(this));
		
		setViewportView(content_pane);
		content_pane.setBackground(Color.black);
		BoxLayout box = new BoxLayout(content_pane, axis_orient);
		content_pane.setLayout(box);
		panel_menus.SetContainerMenu(this);
	}
	
	private void initYGUI() {
		vertical = true;
		commonInit(BoxLayout.Y_AXIS);
		setPreferredSize(new Dimension(width, getPreferredSize().height));
	}
	
	private void initXGUI() {
		commonInit(BoxLayout.X_AXIS);
		setPreferredSize(new Dimension(getPreferredSize().width, width));
	}
	
    public DropPanel(boolean vertical) { if (vertical) initYGUI(); else initXGUI(); }
	
	public void AddItem(String text, String path) {
		//if (text.length() == 0 || path.length() == 0) return;
		DropPanelItem b = new DropPanelItem(text, path);
		content_pane.add(b);
		panel_menus.SetItemMenu(b);
		if (vertical)
			b.setUI(new VerticalButtonUI(270));
		revalidate();
	}
	
	public void DropItem(DropPanelItem b) {
		content_pane.remove(b);
		revalidate();
	}
	
	public void Load(String path) {
		BufferedReader reader = null;
		try {
			reader = IOOperations.GetReader(path);
	  		String strLine;
	  			  		
	  		while ((strLine = reader.readLine()) != null) {
	  			if (strLine.length() == 0) continue;
	  			AddItem(strLine, reader.readLine());
	  		}
	  		reader.close();			
		} 
		catch (Exception e) { Errorist.printLog(e); }
		//finally { if (reader != null) reader.close(); }
	}
	
	public void Save(String path) {
		PrintWriter pw;
		try {
			DropPanelItem item;
			pw = IOOperations.GetWriter(path, true);
			for(int loop1 = 0; loop1 < content_pane.getComponentCount(); loop1++)
			{
				item = (DropPanelItem)content_pane.getComponent(loop1);
				pw.println(item.getText());
				pw.println(item.folder.getPath());
			}
		}
		catch (Exception e) { Errorist.printLog(e); }
	} 	
}