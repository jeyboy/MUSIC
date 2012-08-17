package tabber;

import javax.swing.JScrollPane;
import filelist.FileList;
import filelist.ListItem;

public class Tab extends JScrollPane {
	private static final long serialVersionUID = 1L;
	public Tabber tabber;
	public TabHead tabhead;
	FileList Files;
	public TabOptions options;
	
	void Init(Tabber parent, String title, TabOptions opts, ListItem [] items) {
		tabber = parent;
		options = opts;
	
		Files = items == null ? new FileList(this) : new FileList(this, items);
		setViewportView(Files);
	    parent.addTab(null, this);
	    tabhead = new TabHead(this, title);
	    tabber.SetCurrentTab(this);
	    UpdateCounter();
	}
	
	public Tab(Tabber parent, String title, TabOptions opts, ListItem [] items) { Init(parent, title, opts, items);	}
	
	public Integer FilesCount() { return Files.model.getSize(); }
	public ListItem File(int index)	{	return Files.model.getElementAt(index); }
//	public void AddFlles() {
//		
//	}
	public FileList Files() 			{	return (FileList) getViewport().getView(); }
	
	public String GetTitle() 			{	return tabhead.GetTitle(); }
	public void SetTitle(String title) 	{	tabhead.SetTitle(title); }
	public void UpdateCounter()			{	tabhead.SetCount(FilesCount()); }
	public void SetStatus(String status){   tabhead.SetStatus(status);	}
}
