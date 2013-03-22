package folders;

import java.io.File;
import java.io.PrintWriter;

import filelist.ListItem;
import tabber.Tab;

public class Catalog extends Base {
	int itemsCount = 0;
	public ListItem activeItem = null; 
	
	public Catalog(Tab container) { super(container); }
	
	public int itemsCount() { return itemsCount; }
	public void iterateCount() { itemsCount++;}
	public void deiterateCount() { itemsCount--;}
	
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
	
	public void setPlayed(ListItem item) {	
		if (activeItem != null)
			activeItem.SetStatusUnPlayed();
		
		if ((activeItem = item) != null) {
			activeItem.SetStatusPlayed();
			activeItem.Exec();
			activeItem.node.list.setSelectedValue(activeItem, false);
		}
//		this.repaint(this.getCellBounds(getPlayedIndex(), getPlayedIndex() + 1));
	}
//	
	public int getPlayedIndex() { return activeItem.node.list.model.indexOf(activeItem); }
	
	/// Helper methods

	private int checkRange(int index) {
		if (index >= activeItem.node.list.model.getSize()) index = (activeItem.node.list.model.getSize() - 1);
		return (index < 0) ? 0 : index;
	}    
    
	private int inverseCheckRange(int index) {
		if (index >= activeItem.node.list.model.getSize()) index = 0;
		return (index < 0) ? (activeItem.node.list.model.getSize() - 1) : index;
	}

	public int calcSelect(int curr, boolean next) {
		int index = inverseCheckRange(curr + (next ? 1 : -1));
		activeItem.node.list.setSelectedIndex(index);
		return index;
	}	
	
	public void execCurrOrFirst() 					{ setPlayed(activeItem.node.list.model.getElementAt(checkRange(getPlayedIndex())));	}	
	public int MoveSelect(int index, boolean next) 	{ return calcSelect(index, next); }
	public void execNext(boolean next) 				{ setPlayed(activeItem.node.list.model.getElementAt(MoveSelect(getPlayedIndex(), next)));	}
	public void delCurrAndExecNext() {
		int selected = getPlayedIndex();
		if (selected == -1) {
			execNext(true);
			return;
		}
		activeItem.node.list.model.removeElement(selected);
		
		if ((selected = inverseCheckRange(selected)) == -1) return;
		activeItem.node.list.ensureIndexIsVisible(selected);
		setPlayed(activeItem.node.list.model.getElementAt(selected));
	}	
	
	public void save(PrintWriter pw) {
    	for(FolderNode folder : folders)
    		folder.save(pw);
	}
}