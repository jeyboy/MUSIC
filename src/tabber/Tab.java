package tabber;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import service.Errorist;
import service.Settings;

import filelist.FileList;
import filelist.ListItem;

public class Tab extends JScrollPane {
	void proceedTab(final String path, final ArrayList<ListItem> items) throws Exception {
		if (path == null) throw new Exception("Path are empty");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				synchronized public void run(){
			    	Tab.this.AddListItems(path, items);
			    }
			});
		} 
		catch (InvocationTargetException | InterruptedException e) { Errorist.printLog(e); }
    }	
	public synchronized void Load(BufferedReader bin) throws Exception {
		LockPaint(true);
		int limit = 75;
		ArrayList<ListItem> files = new ArrayList<ListItem>(limit);
		
  		String strLine, group = null;
  		
  		while ((strLine = bin.readLine()) != null) {
  			if (strLine.length() == 0) {
				proceedTab(group, files);
				files.clear();	  					
				break;  				
  			}
  			
  			switch(strLine.charAt(0)) {
  				case 'g':
  					group = strLine.substring(1);
  					break;
  				case 'i': 
  					ListItem t_item = ListItem.Load(strLine);
  					if (!options.interactive || (options.interactive && t_item.file.exists())) {
  						files.add(t_item);
  						if (files.size() == limit) {
  							proceedTab(group, files);
  							files.clear();
  						}
  					}
  					break;
  			}
  		}

		LockPaint(false);
	}
	
	public void Save(PrintWriter pw) {
    	pw.println(Settings._tab + options.Serialize() + GetTitle());
    	
		for(Entry<String, DefaultMutableTreeNode> entry : Files.roots.entrySet()) {
			for (Enumeration<?> e = entry.getValue().children(); e.hasMoreElements();) {
				DefaultMutableTreeNode o = (DefaultMutableTreeNode) e.nextElement();
				
				pw.println(((ListItem)o.getUserObject()).SaveInfo());
		    }			
		}
		pw.println(Settings._posttab);
		pw.flush();
	}
	
	private static final long serialVersionUID = 1L;
	public Tabber tabber;
	public TabHead tabhead;
	public TabOptions options;
	FileList Files;
	
	void Init(Tabber parent, String title, TabOptions opts) {
		tabber = parent;
		options = opts;
	
		Files = new FileList(this);
		setViewportView(Files);
	    parent.addTab(null, this);
	    tabhead = new TabHead(this, title);
	    tabber.SetCurrentTab(this);
	    UpdateCounter();
	}
	
	public Tab(Tabber parent, String title, TabOptions opts) { Init(parent, title, opts);	}
	
	public ListItem GetSelected()		{
		try { return (ListItem)Files.getSelectionPath().getLastPathComponent(); }
		catch(Exception e) { return null; }
	}
	public Integer FilesCount() 		{
		int count = 0;
		for(Entry<String, DefaultMutableTreeNode> entry : Files.roots.entrySet())
			count += entry.getValue().getChildCount();
		return count; 
	}
	
	public void AddListItems(String path, Collection<ListItem> items) {
		DefaultMutableTreeNode root = Files.GetRoot(path);
		for(ListItem elem:items) Files.ProceedElem(root, elem);
	}
	
	public void AddFileItems(String path, Collection<File> items) {
		DefaultMutableTreeNode root = Files.GetRoot(path);
		for(File elem:items) Files.ProceedElem(root, new ListItem(elem));
	}	
	
	public String GetTitle() 									{	return tabhead.GetTitle(); }
	public void SetTitle(String title) 							{	tabhead.SetTitle(title); }
	public void UpdateCounter()									{	tabhead.SetCount(FilesCount()); }
	public void SetStatus(String status)						{   tabhead.SetStatus(status);	}
	public void LockPaint(boolean lock)							{	Files.LockPaint(lock); }
	public Set<Entry<String, DefaultMutableTreeNode>> Roots() 	{	return Files.roots.entrySet(); }


	/// Helper methods
	
//	private int CheckRange(int index) {
//		if (index >= model.getSize()) index = 0;
//		return (index < 0) ? (model.getSize() - 1) : index;
//	}
//
//	public int CalcSelect(int curr, boolean next) {
//		int index = CheckRange(curr + (next ? 1 : -1));
//		setSelectedIndex(index);
//		return index;
//	}	
//	
	public int MoveSelect(boolean next) { return 0; /*CalcSelect(getSelectedIndex(), next);*/ }
//	
	public void MoveSelectAndInit(boolean next) { /*model.elementAt(MoveSelect(next)).Exec();*/	}
//	
	public void DeleteSelectAndInit() {
//		int selected = getSelectedIndex();
//		if (selected == -1) {
//			MoveSelectAndInit(true);
//			return;
//		}
//		if (parent.options.delete_files)
//			IOOperations.deleteFile(((ListItem)getSelectedValue()).file);
//		model.remove(selected);
//		if ((selected = CheckRange(selected)) == -1) return;
//		setSelectedIndex(selected);
//		model.elementAt(selected).Exec();
	}
}
