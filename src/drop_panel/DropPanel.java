package drop_panel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import service.Errorist;
import service.IOOperations;

public class DropPanel extends ScrolledComposite {
	DropPanelMenus panel_menus;
	Composite container;
	
	private void commonInit() {
	    setBackground(new Color(Display.getCurrent(), 0,0,0));
	    setExpandHorizontal(true);
	    setExpandVertical(true);
	    
	    container = new Composite(this, SWT.NONE);
	    setContent(container);
	    container.setBackground(new Color(Display.getCurrent(), 0, 0, 0));
	    container.setLayout(new RowLayout());
	    
	    
		panel_menus = new DropPanelMenus(new DropPanelDialogs(this));
		panel_menus.SetContainerMenu(this);
	}
	
    public DropPanel(Shell wnd) {
    	super(wnd, SWT.H_SCROLL | SWT.BORDER);
    	commonInit();
    }
	
	public void AddItem(String text, String path) {
		if (text.length() == 0 || path.length() == 0) return;
		DropPanelItem b = new DropPanelItem(container, text, path);
		panel_menus.SetItemMenu(b);
		setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        container.layout();
	}
	
	public void DropItem(DropPanelItem b) {
		b.button.dispose();
		setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        container.layout(true);		
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
		if (reader != null) {
			try { reader.close();}
			catch (IOException e) { Errorist.printLog(e); }
		}
	}
	
	public void Save(String path) {
		PrintWriter pw;
		try {
			DropPanelItem item;
			pw = IOOperations.GetWriter(path, true);
			Object [] objs = getChildren();
			for(int loop1 = 0; loop1 < objs.length; loop1++)
			{
				item = (DropPanelItem)objs[loop1];
				pw.println(item.button.getText());
				pw.println(item.folder.getPath());
			}
		}
		catch (Exception e) { Errorist.printLog(e); }
	} 	
}