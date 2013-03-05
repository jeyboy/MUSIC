package folders;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import tabber.Tab;

public class Catalog {
	Tab tab;
	
	public Catalog(Tab container) { tab = container; }
	
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
	
	public FolderNode getNode(String root_path) {
		FolderNode node;
		int pos = folders.indexOf(root_path);
		if (pos == -1) {
			node = new FolderNode(tab, root_path);
			folders.add(node);
		}
		else node = folders.get(pos);
		return node;
	}	
	
	public FolderNode addItem(String root_path, File ... files) {
		FolderNode node = getNode(root_path);		
		node.addFiles(files);
		return node;
	}
	
	public void addItem(FolderNode node, String name, File ... files) {	
		node.addFolder(name, files);
	}
	
	public void save(PrintWriter pw) {
    	for(FolderNode folder : folders)
    		folder.save(pw);
	}
}