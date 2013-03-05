package tabber;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.JScrollPane;
import filelist.FileList;
import filelist.ListItem;
import folders.Catalog;

public class Tab extends JScrollPane {
	private static final long serialVersionUID = 1L;
	public Tabber tabber;
	public TabHead tabhead;
	FileList files;
	public TabOptions options;
	Catalog catalog;
	
	public Tab(Tabber parent, String title, TabOptions opts) {
		catalog = new Catalog(this);
		tabber = parent;
		options = opts;
	
		files = new FileList(this);
		setViewportView(files);
	    parent.addTab(null, this);
	    tabhead = new TabHead(this, title);
	    tabber.SetCurrentTab(this);
	    tabber.setBackgroundAt(tabber.getTabCount() - 1, Color.black);
	    tabber.setForegroundAt(tabber.getTabCount() - 1, Color.white);
	    UpdateCounter();
	    getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
	    getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12));		
	}
	
	public Catalog getCatalog() { return catalog; }
	
	public Integer FilesCount() { return files.model.getSize(); }
	public ListItem File(int index)	{	return files.model.getElementAt(index); }
	public void Shuffle() {
//		Collections.shuffle((List<?>)Files.model);
		Random rgen = new Random();
		for (int i = 0; i < FilesCount(); i++) {
			int randomPosition = rgen.nextInt(FilesCount());
			ListItem temp = File(i);
			files.model.set(i, File(randomPosition));
			files.model.set(randomPosition, temp);
		}
	}

//	public FileList Files() 			{	return (FileList) getViewport().getView(); }
	public FileList Files() 			{	return files; }
	
	public String GetTitle() 			{	return tabhead.GetTitle(); }
	public void SetTitle(String title) 	{	tabhead.SetTitle(title); }
	public void UpdateCounter()			{	tabhead.SetCount(FilesCount()); }
	public void SetStatus(String status){   tabhead.SetStatus(status);	}
}