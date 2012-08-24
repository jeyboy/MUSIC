package tabber;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;

import filelist.FileList;
import filelist.ListItem;

public class Tab extends CTabItem {
	public Tabber tabber;
	FileList Files;
	public TabOptions options;
	
	void Init(Tabber parent, String title, TabOptions opts, ListItem [] items) {
		tabber = parent;
		options = opts;
	
//		Files = items == null ? new FileList(this) : new FileList(this, items);
//		setControl(Files);
		setText(title);
	    tabber.SetCurrentTab(this);
	    UpdateCounter();
//	    getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
//	    getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12));	    
	}
	
	public Tab(Tabber parent, String title, TabOptions opts, ListItem [] items) {
		super(parent, SWT.NONE);
		Init(parent, title, opts, items);	
	}	
	
	public Integer FilesCount() { return Files.model.getSize(); }
	public ListItem File(int index)	{	return Files.model.getElementAt(index); }
//	public void AddFlles() {
//		
//	}
	public FileList Files() 			{	return Files; }
	
	public String GetTitle() 			{	return getText(); }
	public void SetTitle(String title) 	{	setText(title); }
	public void UpdateCounter()			{	/*tabhead.SetCount(FilesCount());*/ }
}
