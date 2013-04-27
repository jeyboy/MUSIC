package folders;

import java.io.File;
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
	
	public FolderNode getNode(String root_path) {
		FolderNode node;
		int pos = folders.indexOf(root_path);
		if (pos == -1) {
//			sincronizeTail();
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
	
	public FolderNode addItem(String root_path, File ... files) {
		FolderNode node = getNode(root_path);		
		node.addFiles(files);
		return node;
	}
	
	public void setPlayed(ListItem item) {	
		if (activeItem != null)
			activeItem.SetStatusUnPlayed();
		
		if ((activeItem = item) != null) {
			activeItem.SetStatusPlayed();
			activeItem.Exec();
			activeItem.getList().setSelectedValue(activeItem, false);
		}
//		this.repaint(this.getCellBounds(getPlayedIndex(), getPlayedIndex() + 1));
		
//		activeItem.node.list.ensureIndexIsVisible(selected);
	}

	public void execCurrOrFirst() {
		if (root == null) return;
		
		FolderNode iter = activeItem != null ? activeItem.node : root;
		
		while(iter != last) {
			if (iter.list.getSelectedIndex() == -1)
				if (iter.list.model.getSize() > 0) {
					setPlayed(iter.list.model.getElementAt(0));
					return;
				}
			else {
				setPlayed(iter.list.model.getElementAt(iter.list.getSelectedIndex()));
				return;
			}
		}
	}
	
//	TODO: add realization	
	public void execNext(boolean next) {
		if (root == null) return;
		
		FolderNode iter = activeItem != null ? activeItem.node : root;
//		iter.next.activate();
//		setPlayed(iter.next.list.model.getElementAt(0));
		
//		while(iter != last) {
//			if (iter.list.getSelectedIndex() == -1)
//				if (iter.list.model.getSize() > 0) {
//					setPlayed(iter.list.model.getElementAt(0));
//					return;
//				}
//			else {
//				setPlayed(iter.list.model.getElementAt(iter.list.getSelectedIndex()));
//				return;
//			}
//		}		
		
		
//		setPlayed(activeItem.node.list.model.getElementAt(MoveSelect(getPlayedIndex(), next)));	
	}
//	TODO: add realization	
	public void delCurrAndExecNext() {
//		int selected = getPlayedIndex();
//		if (selected == -1) {
//			execNext(true);
//			return;
//		}
//		activeItem.node.list.model.removeElement(selected);
//		
//		if ((selected = inverseCheckRange(selected)) == -1) return;
//		activeItem.node.list.ensureIndexIsVisible(selected);
//		setPlayed(activeItem.node.list.model.getElementAt(selected));
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