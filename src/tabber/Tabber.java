package tabber;

import java.io.PrintWriter;
import javax.swing.JTabbedPane;
import service.Common;
import service.Errorist;
import service.IOOperations;
import service_threads.TabberLoader;
import filelist.ListItem;

public class Tabber extends JTabbedPane {
	private static final long serialVersionUID = -2714890310462311079L;
	
	public Tabber() { setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);  } //JTabbedPane.SCROLL_TAB_LAYOUT
	
	Tab InitTab(String title, TabOptions opts) 			{ return new Tab(this, title, opts); }
	public Tab AddTab(String title, TabOptions opts)	{ return InitTab(title, opts); }
	public Tab AddTab(String title)						{ return InitTab(title, new TabOptions()); }
	
	public Tab GetTab(int index)			{ return (Tab) getComponentAt(index);}
	public Tab GetCurrentTab()				{ return (Tab) getSelectedComponent();}
	public void SetCurrentTab(int index)	{ setSelectedIndex(index);}
	public void SetCurrentTab(Tab tab)		{ setSelectedComponent(tab);}
	public ListItem GetCurrentItem()		{ return GetCurrentTab().GetSelected(); }
	
	static public void Load() {
		Common.tabber = new Tabber();
		new TabberLoader();
	}
	
	public void Save() {
//		try { Common._trash.join(0); }
//		catch (InterruptedException e1) { Errorist.printLog(e1); }
		Common._trash.save();
		if (!Common.save_flag) return;
		
	    PrintWriter pw = null;

	    try {
	        pw = IOOperations.GetWriter(service.Settings.tabspath, false);
	        	        
	        for(int loop = 0; loop < getTabCount(); loop++)
	        	GetTab(loop).Save(pw);
	    }
	    catch (Exception e) { Errorist.printLog(e); }
	    if (pw != null) pw.close();
	}		
// Customizzation
//	Property String	Object Type
//	TabbedPane.actionMap	ActionMap
//	TabbedPane.ancestorInputMap	InputMap
//	TabbedPane.background	Color
//	TabbedPane.borderHightlightColor	Color
//	TabbedPane.contentAreaColor	Color
//	TabbedPane.contentBorderInsets	Insets
//	TabbedPane.contentOpaque	Boolean
//	TabbedPane.darkShadow	Color
//	TabbedPane.focus	Color
//	TabbedPane.focusInputMap	InputMap
//	TabbedPane.font	Font
//	TabbedPane.foreground	Color
//	TabbedPane.highlight	Color
//	TabbedPane.light	Color
//	TabbedPane.opaque	Boolean
//	TabbedPane.selected	Color
//	TabbedPane.selectedForeground	Color
//	TabbedPane.selectedTabPadInsets	Insets
//	TabbedPane.selectHighlight	Color
//	TabbedPane.selectionFollowsFocus	Boolean
//	TabbedPane.shadow	Color
//	TabbedPane.tabAreaBackground	Color
//	TabbedPane.tabAreaInsets	Insets
//	TabbedPane.tabInsets	Insets
//	TabbedPane.tabRunOverlay	Integer
//	TabbedPane.tabsOpaque	Boolean
//	TabbedPane.tabsOverlapBorder	Boolean
//	TabbedPane.textIconGap	Integer
//	TabbedPane.unselectedBackground	Color
//	TabbedPane.unselectedTabBackground	Color
//	TabbedPane.unselectedTabForeground	Color
//	TabbedPane.unselectedTabHighlight	Color
//	TabbedPane.unselectedTabShadow	Color
//	TabbedPaneUI	String	
	
	
	
	
	
	
	
	
	

	//    public void setIconAt(int index, Icon icon)
//    public void setMnemonicAt(int index, int mnemonic)
//    public void setDisplayedMnemonicIndexAt(int index, int mnemonicIndex)
//    public void setToolTipTextAt(int index, String text)
//    public void setComponentAt(int index, Component component)	
//    public void setBackgroundAt(int index, Color background)
//    public void setForegroundAt(int index, Color foreground)
//    public void setEnabledAt(int index, boolean enabled)
//    public void setDisabledIconAt(int index, Icon disabledIcon)
//	removeTabAt(int index)
//	remove(int index)
//	remove(Component component)	
//	removeAll()	
	/**
	 * @param style = JTabbedPane.BOTTOM or JTabbedPane.TOP or ...
	 */
	public void TabsPlacement(int style) { super.setTabPlacement(style); }
	
	//Delete selected elem in selected tab and init next
	public void DeleteSelectAndInit() {
		GetTab(getSelectedIndex()).DeleteSelectAndInit();
	}
	
	//Move to the next elem from selected elem in selected tab
	public void MoveSelectAndInit(boolean next) {
		GetTab(getSelectedIndex()).MoveSelectAndInit(next);
	}	

	/**
	 * @param index
	 * @param key =  KeyEvent.VK_C or ...
	 */
	public void SetHotKey(int index, int key) { super.setMnemonicAt(index, key); }

//	   ChangeListener changeListener = new ChangeListener() {
//		      public void stateChanged(ChangeEvent changeEvent) {
//		        JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
//		        int index = sourceTabbedPane.getSelectedIndex();
//		        System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
//		      }
//		    };
//      tabbedPane.addChangeListener(changeListener);
	
	
	
//    JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftComponent, rightComponent);
//    int size = pane.getDividerSize();
//    size = 1;
//    pane.setDividerSize(size);
	
	//get index by child component
//	int index = pane.indexOfComponent(component);
	
	
	
	//move tab in container
//	  public static void main(String[] argv) throws Exception {
//		    JTabbedPane pane = new JTabbedPane();
//		    int src = pane.getTabCount() - 1;
//		    int dst = 0;
//
//		    Component comp = pane.getComponentAt(src);
//		    String label = pane.getTitleAt(src);
//		    Icon icon = pane.getIconAt(src);
//		    Icon iconDis = pane.getDisabledIconAt(src);
//		    String tooltip = pane.getToolTipTextAt(src);
//		    boolean enabled = pane.isEnabledAt(src);
//		    int keycode = pane.getMnemonicAt(src);
//		    int mnemonicLoc = pane.getDisplayedMnemonicIndexAt(src);
//		    Color fg = pane.getForegroundAt(src);
//		    Color bg = pane.getBackgroundAt(src);
//
//		    pane.remove(src);
//
//		    pane.insertTab(label, icon, comp, tooltip, dst);
//
//		    pane.setDisabledIconAt(dst, iconDis);
//		    pane.setEnabledAt(dst, enabled);
//		    pane.setMnemonicAt(dst, keycode);
//		    pane.setDisplayedMnemonicIndexAt(dst, mnemonicLoc);
//		    pane.setForegroundAt(dst, fg);
//		    pane.setBackgroundAt(dst, bg);
//		  }	
}
