package tabber;

import java.awt.Color;
import java.io.PrintWriter;
import javax.swing.JTabbedPane;
import service.Common;
import service.Constants;
import service.Errorist;
import service.IOOperations;
import service_threads.TabberLoader;
import filelist.ListItem;

public class Tabber extends JTabbedPane {
	private static final long serialVersionUID = -2714890310462311079L;
	
	public Tabber() { setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);  } //JTabbedPane.SCROLL_TAB_LAYOUT
	
	public Tab AddTab(String title, TabOptions opts, ListItem [] items)	{ return new Tab(this, title, opts, items); }
	public Tab AddTab(String title, TabOptions opts)					{ return AddTab(title, opts, null); }
	public Tab AddTab(String title)										{ return AddTab(title, new TabOptions(), null); }
	
	public Tab GetTab(int index)			{ return (Tab) getComponentAt(index);}
	public Tab GetCurrentTab()				{ return (Tab) getSelectedComponent();}
	public void SetCurrentTab(int index)	{ setSelectedIndex(index);}
	public void SetCurrentTab(Tab tab)		{ setSelectedComponent(tab);}
	public ListItem GetCurrentItem()		{
		try { return (ListItem)GetCurrentTab().Files().getSelectedValue(); }
		catch(Exception e) { return null; }
	}
	
	static public void Load() {
		Common.tabber = new Tabber();
		Common.tabber.setUI(new TabberUI());
		Common.tabber.setBackground(Color.black);
		new TabberLoader();
	}
	
	public void Save() {
		Common._trash.save();
		if (!Common.save_flag) return;
		
	    PrintWriter pw = null;
	    Tab curr_tab;

	    try {
	        pw = IOOperations.GetWriter(Constants.tabspath, false, false);
	        	        
	        for(int loop = 0; loop < getTabCount(); loop++) {
	        	curr_tab = GetTab(loop);
	        	curr_tab.Files.SetPlayed(null);
	        	
	        	pw.println('*' + curr_tab.options.Serialize() + curr_tab.GetTitle());
	        	
	        	for(int loop1 = 0; loop1 < curr_tab.FilesCount(); loop1++) 
	        		pw.println(curr_tab.File(loop1).SaveInfo());
	        	
	        	pw.flush();
	        }
	    }
	    catch (Exception e) { Errorist.printLog(e); }
	    finally { if (pw != null) pw.close(); }
	}		

	/**
	 * @param style = JTabbedPane.BOTTOM or JTabbedPane.TOP or ...
	 */
	public void TabsPlacement(int style) { super.setTabPlacement(style); }
	
	//Delete selected elem in selected tab and init next
	public void DeleteSelectAndInit() {
		GetTab(getSelectedIndex()).Files.DeleteSelectAndInit();
	}
	
	//Move to the next elem from selected elem in selected tab
	public void MoveSelectAndInit(Boolean next) {
		GetTab(getSelectedIndex()).Files.MoveSelectAndInit(next);
	}
	
	//Move to the next elem from selected elem in selected tab
	public void PlaySelectedOrFirst() {
		GetTab(getSelectedIndex()).Files.PlaySelectedOrFirst();
	}	

	/**
	 * @param index
	 * @param key =  KeyEvent.VK_C or ...
	 */
	public void SetHotKey(int index, int key) { super.setMnemonicAt(index, key); }
}
