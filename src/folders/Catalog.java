package folders;

import java.io.PrintWriter;

import filelist.ListItem;
import service.Common;
import service.IOOperations;
import tabber.Tab;

public class Catalog extends Base {
	int itemsCount = 0;
	public ListItem activeItem = null;
	FolderNode root = null;
	public CatalogSelection selection = new CatalogSelection();
	
	public Catalog(Tab container) { super(container); }
	
	public int itemsCount() { return itemsCount; }
	public void iterateCount() { ++itemsCount; tab.updateCounter(); }
	public void deiterateCount() { --itemsCount; tab.updateCounter(); }

	FolderNode buildNodeBranch(FolderNode root_node, String [] levels, int start_index) {
		for(int loop1 = start_index; loop1 < levels.length; loop1++)
			root_node = new FolderNode(root_node, levels[loop1]);
		return root_node;
	}
	
	public FolderNode getNode(String path) {
		FolderNode node;
		
		String [] levels = IOOperations.splitPath(path);
		node = find(levels[0]);
		
		if (node == null)
			node = buildNodeBranch(createNode(levels[0]), levels, 1);
		else {
			FolderNode temp;
			for(int loop1 = 1; loop1 < levels.length; loop1++) {
				temp = node.find(levels[loop1]);
				if (temp == null) {
					node = buildNodeBranch(node, levels, loop1);
					break;
				}
				else node = temp;
			}
		}
		
		return node;
	}
	
//	public FolderNode getNode(String root_path) {
//		FolderNode node;
//		
//		int pos = folders.indexOf(root_path);
//		if (pos == -1) {
//			sincronizeTail();
//			node = new FolderNode(tab, root_path);
//
//			folders.add(node);
//			if (root == null) 
//				last = root = node;
//			else {
//				last.next = node;
//				node.prev = last;
//				last = node;
//			}
//		}
//		else node = folders.get(pos);
//		return node;
//	}	
//	
	public FolderNode addItem(String root_path, String ... pathes) {
		FolderNode node = getNode(root_path);		
		node.addFiles(pathes);
		return node;
	}
	
	public void setPlayed(ListItem item) {	
		if (activeItem != null) {
			activeItem.setStatusUnPlayed();
			Common.player.stop();
		}
		
		if ((activeItem = item) != null) {
			activeItem.setStatusPlayed();
			activeItem.exec();
		}
		else {
			try { root.tab.pane.repaint(); }
			catch(Exception e) {}
		}
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
	
//	TODO : write correct selection movement
	public void moveSelect(boolean next) {
		
		
//		if (root == null) return;
//		sincronizeTail();
//		
//		FolderNode iter = activeItem != null ? activeItem.node : root;
//		FolderNode start_node = iter;
//		ListItem item = null;
//		
//		do {
//			if ((item = iter.nextItem(next)) == null)
//				if (next)
//					iter = iter.next == null ? root : iter.next;
//				else
//					iter = iter.prev == null ? last : iter.prev;
//			else break;
//		}
//		while(iter != start_node);
//		
//		setPlayed(item);
	}	
	
	public void save(PrintWriter pw) {
    	for(FolderNode folder : folders)
    		folder.save(pw);
	}
	
	FolderNode createNode(String path) {
		sincronizeTail();
		FolderNode node = new FolderNode(tab, path);
		appendNode(node);
		return node;
	}
	
	void sincronizeTail() {
		if (last == null) return;
		while(last.next != null)
			last = last.next;
	}
	
	void appendNode(FolderNode node) {
		folders.add(node);
		if (root == null) 
			last = root = node;
		else {
			last.next = node;
			node.prev = last;
			last = node;
		}		
	}	
}