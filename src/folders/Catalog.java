package folders;

import java.io.PrintWriter;

import filelist.ListItem;
import tabber.Tab;

public class Catalog extends Base {
	int itemsCount = 0;
	public ListItem activeItem = null;
	FolderNode root = null;
	public CatalogSelection selection = new CatalogSelection();
	
	public Catalog(Tab container) { super(container); }
	
	public int itemsCount() { return itemsCount; }
	public void iterateCount() { itemsCount++;}
	public void deiterateCount() { itemsCount--;}

//	FolderNode buildPath(String ... levels) {
//		
//	} 
	
	public FolderNode getNode(String root_path) {
		FolderNode node;
		
//TODO:	use list of full pathes for search
		int pos = folders.indexOf(root_path);
		if (pos == -1) {
			sincronizeTail();
			node = new FolderNode(tab, root_path);
			folders.add(node);
			if (root == null) 
				last = root = node;
			else {
				last.next = node;
				node.prev = last;
				last = node;
			}
		}
		else node = folders.get(pos);
		return node;
	}	
	
	public FolderNode addItem(String root_path, String ... pathes) {
		FolderNode node = getNode(root_path);		
		node.addFiles(pathes);
		return node;
	}
	
	public void setPlayed(ListItem item) {	
		if (activeItem != null)
			activeItem.setStatusUnPlayed();
		
		if ((activeItem = item) != null) {
			activeItem.setStatusPlayed();
			activeItem.exec();
			activeItem.getList().setSelectedValue(activeItem, false);
		}
		
		root.tab.pane.repaint();		
	}

	public void execCurrOrFirst() {
		if (root == null) return;
		
		FolderNode iter = activeItem != null ? activeItem.node : root;
		FolderNode start_node = iter;
		ListItem item = null;
		
		do {
			if ((item = iter.currOrFirst()) == null)
				iter = iter.next == null ? root : iter.next;
			else break;
		}
		while(iter != start_node);
		
		setPlayed(item);		
	}
	
	public void execNext(boolean next) {
		if (root == null) return;
		sincronizeTail();
		
		FolderNode iter = activeItem != null ? activeItem.node : root;
		FolderNode start_node = iter;
		ListItem item = null;
		
		do {
			if ((item = iter.nextItem(next)) == null)
				if (next)
					iter = iter.next == null ? root : iter.next;
				else
					iter = iter.prev == null ? last : iter.prev;
			else break;
		}
		while(iter != start_node);
		
		setPlayed(item);
	}
	

	public void delCurrAndExecNext() {
		if (root == null) return;
		
		FolderNode iter = activeItem != null ? activeItem.node : root;
		FolderNode start_node = iter;
		ListItem item = null;
		
		do {
			if ((item = iter.delCurrAndNext()) == null)
				iter = iter.next == null ? root : iter.next;
			else break;
		}
		while(iter != start_node);
		
		setPlayed(item);		
	}
	
	public void save(PrintWriter pw) {
    	for(FolderNode folder : folders)
    		folder.save(pw);
	}
	
	void sincronizeTail() {
		if (last == null) return;
		while(last.next != null)
			last = last.next;
	}	
}