package tabber;

import java.io.PrintWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import service.Common;
import service.Errorist;
import service.IOOperations;
import service_threads.TabberLoader;
import filelist.ListItem;

public class Tabber extends CTabFolder {
	public Tabber(Composite arg0, int arg1) { super(arg0, arg1); }
	
	Tab InitTab(String title, TabOptions opts, ListItem [] items) 		{ return new Tab(this, title, opts, items); }
	public Tab AddTab(String title, TabOptions opts, ListItem [] items)	{ return InitTab(title, opts, items); }
	public Tab AddTab(String title, TabOptions opts)					{ return InitTab(title, opts, null); }
	public Tab AddTab(String title)										{ return InitTab(title, new TabOptions(), null); }
	
	public Tab GetTab(int index)			{ return (Tab) getItem(index);}
	public Tab GetCurrentTab()				{ return (Tab) getSelection();}
	public void SetCurrentTab(int index)	{ setSelection(index);}
	public void SetCurrentTab(Tab tab)		{ setSelection(tab);}
	public ListItem GetCurrentItem()		{
		try { return (ListItem)GetCurrentTab().Files().getSelectedValue(); }
		catch(Exception e) { return null; }
	}
	
	static public void Load(Composite container) {
		Common.tabber = new Tabber(container, SWT.CLOSE);
		Common.tabber.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 0));		
		Common.tabber.setBorderVisible(true);
		
		Common.tabber.setUnselectedCloseVisible(false);
		Common.tabber.setSimple(false);
		Common.tabber.setBackground(new Color(Display.getCurrent(), 0, 0, 0));
		Common.tabber.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
		
		container.redraw();
		new TabberLoader();
	}
	
	public void Save() {
//		try { Common._trash.join(0); }
//		catch (InterruptedException e1) { Errorist.printLog(e1); }
		Common._trash.save();
		if (!Common.save_flag) return;
		
	    PrintWriter pw = null;
	    Tab curr_tab;

	    try {
	        pw = IOOperations.GetWriter(service.Settings.tabspath, false);
	        	        
	        for(int loop = 0; loop < getItemCount(); loop++) {
	        	curr_tab = GetTab(loop);
	        	
	        	pw.println('*' + curr_tab.options.Serialize() + curr_tab.GetTitle());
	        	
	        	for(int loop1 = 0; loop1 < curr_tab.FilesCount(); loop1++) 
	        		pw.println(curr_tab.File(loop1).SaveInfo());
	        	
	        	pw.println(" ");
	        	pw.flush();
	        }
	    }
	    catch (Exception e) { Errorist.printLog(e); }
	    finally { if (pw != null) pw.close(); }
	}		
	
	//Delete selected elem in selected tab and init next
	public void DeleteSelectAndInit() {
//		GetTab(getSelectedIndex()).Files.DeleteSelectAndInit();
	}
	
	//Move to the next elem from selected elem in selected tab
	public void MoveSelectAndInit(Boolean next) {
//		GetTab(getSelectedIndex()).Files.MoveSelectAndInit(next);
	}		
}
