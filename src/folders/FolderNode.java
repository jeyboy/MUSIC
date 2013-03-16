package folders;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import filelist.ListItem;
import tabber.Tab;

public class FolderNode {
	FolderNode parent = null;
	ArrayList<FolderNode> folders = new ArrayList<FolderNode>(1) {
		private static final long serialVersionUID = -2235109783338833765L;

		public int indexOf(Object o) {
			if (o instanceof String) {
			    for (int i = 0; i < this.size(); i++)
			        if (o.equals(this.get(i).path))
			            return i;
			} 
			else super.indexOf(o);
			return -1;
		};
	};
	ArrayList<ListItem> items = new ArrayList<ListItem>(10);
	public String path;
	Tab tab;
	
	public FolderNode(FolderNode parentNode, String path) {
		parent = parentNode;
		if (parentNode != null)
			parentNode.folders.add(this);
		tab = parentNode.tab;
		this.path = path;
	}
	public FolderNode(Tab container, String path) {
		tab = container;
		this.path = path; 
	}	

	public void addItem(ListItem listItem) {
		tab.Files().ProceedElem(listItem);
		items.add(listItem);		
	}
	
	public void delete(ListItem listItem) {
		items.remove(listItem);
	}
	
	public void addFiles(File ... files) {
		for(File f : files)
			addItem(new ListItem(this, f));
	}
	
	public void addFolder(String path, File [] files) {
		FolderNode n = new FolderNode(this, path);
		folders.add(n);
		n.addFiles(files);
	}
	
	public void save(PrintWriter pw) {
		if (items.size() > 0 || folders.size() > 0) {
			pw.println((parent == null ? '>' : '<') + path);
			
			for(ListItem item : items)
				pw.println(item.SaveInfo());
			
	    	for(FolderNode folder : folders) 
	    		folder.save(pw);
	    	pw.println('$');
		}
	}
}