package tabber;

import java.awt.Component;
import java.awt.Dimension;
import java.io.PrintWriter;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import service.Common;
import service.Dropper;

import filelist.ListItem;
import folders.Catalog;

public class Tab extends JScrollPane {
	static final long serialVersionUID = 1L;
	
	public Tabber tabber;
	public TabHead tabhead;
	public TabOptions options;
	public Catalog catalog;
	
	public JPanel pane = new JPanel();
	Dropper dropper;
	
	public Tab(Tabber parent, String title, TabOptions opts) {
		catalog = new Catalog(this);
		tabber = parent;
		options = opts;
	
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.setBackground(Common.color_background);
		
        dropper = new Dropper(pane, new Dropper.Listener() {
        	public void filesDropped(Dropper.Event ev) {
        		if (!((boolean)ev.getSource()))
        			Common._drop_initer.ProceedDrop(Tab.this, ev.getFiles());
        	}
        });
			
		setViewportView(pane);
	    parent.addTab(null, this);
	    tabhead = new TabHead(this, title);
	    tabber.currTab(this);
	    tabber.setBackgroundAt(tabber.getTabCount() - 1, Common.color_background);
	    tabber.setForegroundAt(tabber.getTabCount() - 1, Common.color_foreground);
	    
	    getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
		getVerticalScrollBar().setUnitIncrement(20);
	    getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12));		
	}
	
	public void addFileList(JPanel list) 		{
		pane.add(list);
		list.setAlignmentX(Component.LEFT_ALIGNMENT);
	}
	public void removeFileList(JPanel list) 	{ pane.remove(list); }
	
	public ListItem currItem() { return catalog.activeItem; }
	
	public void setPlayed(ListItem item) { catalog.setPlayed(item); }
	public void delCurrAndExecNext() { catalog.delCurrAndExecNext(); }
	public void execNext(boolean next) { catalog.execNext(next); }
	public void execCurrOrFirst() { catalog.execCurrOrFirst(); }	
	
//	public Catalog getCatalog() { return catalog; }
	
	public void Shuffle() {
//		Collections.shuffle((List<?>)Files.model);
		
//		Random rgen = new Random();
//		for (int i = 0; i < FilesCount(); i++) {
//			int randomPosition = rgen.nextInt(FilesCount());
//			ListItem temp = File(i);
//			files.model.set(i, File(randomPosition));
//			files.model.set(randomPosition, temp);
//		}
	}

	public int filesCount() 			{ return catalog.itemsCount(); }
	
	public String getTitle() 			{ return tabhead.getTitle(); }
	public void setTitle(String title) 	{ tabhead.setTitle(title); }
	public void updateCounter()			{ tabhead.setCount(filesCount()); }
	public void setStatus(String status){ tabhead.setStatus(status); }
	
	public void save(PrintWriter pw) {
    	setPlayed(null);
    	
    	pw.println('*' + options.Serialize() + getTitle());
    	catalog.save(pw);
    	pw.flush();
	}
}