package tabber;

import java.awt.Color;
import java.io.PrintWriter;
import javax.swing.JTabbedPane;
import service.Common;
import service.Constants;
import service.Errorist;
import service.IOOperations;
import service_threads.TabberLoader;
import uis.TabberUI;
import filelist.ListItem;

public class Tabber extends JTabbedPane {
	private static final long serialVersionUID = -2714890310462311079L;
	
	public Tabber() { setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);  } //JTabbedPane.SCROLL_TAB_LAYOUT
	
	public Tab addTab(String title, TabOptions opts)					{ return new Tab(this, title, opts); }
	public Tab addTab(String title)										{ return addTab(title, new TabOptions()); }
	
	public Tab getTab(int index)	{ return (Tab) getComponentAt(index);}
	public Tab currTab()			{ return (Tab) getSelectedComponent();}
	public void currTab(int index)	{ setSelectedIndex(index);}
	public void currTab(Tab tab)	{ setSelectedComponent(tab);}
	public ListItem getCurrItem()	{ return currTab().currItem(); }
	
//	public void addFolder(Tab root, String root_path, File ... files) {
//		tabs.get(root).addFolder(root_path, files);
//	}	
	
	static public void load() {
		Common.tabber = new Tabber();
		Common.tabber.setUI(new TabberUI());
		Common.tabber.setBackground(Color.black);
		TabberLoader loader = new TabberLoader();
		
//	    // A property listener used to update the progress bar
//	    PropertyChangeListener listener = 
//	                               new PropertyChangeListener(){
//	      public void propertyChange(PropertyChangeEvent event) {
//	        if ("progress".equals(event.getPropertyName())) {
//	          progressBar.setValue( (Integer)event.getNewValue() );
//	        }
//	      }
//	    };
//	    loader.addPropertyChangeListener(listener);
		
		loader.execute();
	}
	
	public void save() {
		if (!Common.save_flag) return;
		
	    PrintWriter pw = null;

	    try {
	        pw = IOOperations.getWriter(Constants.tabspath, false, false);
	        	        
	        for(int loop = 0; loop < getTabCount(); loop++)
	        	getTab(loop).save(pw);
	    }
	    catch (Exception e) { Errorist.printLog(e); }
	    finally { if (pw != null) pw.close(); }
	}		

	/**
	 * @param style = JTabbedPane.BOTTOM or JTabbedPane.TOP or ...
	 */
	public void tabsPlacement(int style) { super.setTabPlacement(style); }
	
	//Delete selected elem in selected tab and init next
	public void deleteSelectAndInit() {	currTab().delCurrAndExecNext(); }
	
	//Move to the next elem from selected elem in selected tab
	public void moveSelectAndInit(boolean next) { currTab().execNext(next);	}
	
	//Move to the next elem from selected elem in selected tab
	public void playSelectedOrFirst() {	currTab().execCurrOrFirst(); }	
//
//	/**
//	 * @param index
//	 * @param key =  KeyEvent.VK_C or ...
//	 */
//	public void SetHotKey(int index, int key) { super.setMnemonicAt(index, key); }
}
