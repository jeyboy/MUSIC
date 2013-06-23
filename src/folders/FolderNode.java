package folders;

import java.awt.Color;
import java.awt.Component;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

import filelist.FileList;
import filelist.IconListRenderer;
import filelist.ListItem;
import service.Common;
import service.Utils;

import java.util.Random;
import tabber.Tab;

public class FolderNode extends Base {
	FolderNode parent = null;
	FolderNode next = null;
	FolderNode prev = null;
	
	public FileList list;
	JPanel pane;

	public String path;
	public IconListRenderer listrender = new IconListRenderer();
	
	ArrayList<ListItem> items;	
	public ArrayList<ListItem> elems() { return items; } 
	
	public void freeMemory() { items.trimToSize(); }
	
	void init(String path) {
		items = new ArrayList<ListItem>(100);
		boolean add_area = parent != null;
		
		this.path = path;
		list = new FileList(this);
		pane = new JPanel();
		pane.setBackground(Common.color_background);
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(0, add_area ? 4 : 1, 0, 0),
						BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)
				)
		);
		
		JLabel label = new JLabel(path);
		label.setForeground(Common.color_foreground);
		label.setBackground(Common.color_background);
		pane.add(label, Component.LEFT_ALIGNMENT);
				
		pane.add(list);
		list.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		if (!add_area)
			tab.addFileList(pane);
		else
			parent.pane.add(pane);
				
		if (tab.options.interactive && Common._watcher != null)
			Common._watcher.addElem(this);
	}
	
	public FolderNode(FolderNode parentNode, String path) {
		super(parentNode.tab);
		parent = parentNode;
		last = this;
		if (parentNode != null) {
			parentNode.folders.add(this);
			
			this.next = parentNode.last.next;
			this.prev = parentNode.last;
			if (parentNode.last.next != null)
				parentNode.last.next.prev = this;
			parentNode.last.next = this;
			syncTree();
		}
		init(path);
	}
	public FolderNode(Tab container, String path) {
		super(container);
		last = this;
		init(path); 
	}
	
	public String fullPath() { return parent != null ? Utils.joinPaths(parent.fullPath(), path) : path;} 
	
	void syncTree() {
		FolderNode iter = parent;
		
		while(iter != null) {
			iter.last = this;
			iter = iter.parent;
		}
	}
		
    void addAssocIcon(String ext, ListItem listItem) {
    	if (listrender.icons.containsKey(ext)) return;
    	Icon icon = FileSystemView.getFileSystemView().getSystemIcon(listItem.file());
    	if (icon != null)
    		listrender.icons.put(ext, icon); 
    }	
		
	public void addItem(ListItem listItem) {
        addAssocIcon(listItem.ext, listItem);
        list.model.addElement(listItem);		
	}
	
	public void delete(ListItem listItem) {
		list.model.removeElement(listItem);
	}
	
	public void addFiles(String ... pathes) {
		for(String f : pathes)
			addItem(new ListItem(this, f));
	}
	
	public void shuffle() {		
		Random rgen = new Random();
		for (int i = 0; i < items.size(); i++) {
			int randomPosition = rgen.nextInt(items.size());
			ListItem temp = items.get(i);
			items.set(i, items.get(randomPosition));
			items.set(randomPosition, temp);
		}
		list.model.repaint(0);
	}	
	
/////////////////////////////////////////////////////////////////////////////////////	
	public int getPlayedIndex() { return list.model.indexOf(tab.catalog.activeItem); }

	ListItem checkRange(int index) {
		if (index >= list.model.getSize() || index < 0) return null;
		list.setSelectedIndex(index);
		list.ensureIndexIsVisible(index);	
		return list.model.getElementAt(index);
	}    

	public ListItem calcSelect(int curr, boolean next) { return checkRange(curr == -1 ? next ? 0 : list.model.getSize() - 1 : curr + (next ? 1 : -1)); }
	ListItem nextItem(boolean next) { return moveSelect(getPlayedIndex(), next); }	
	
	public ListItem moveSelect(int index, boolean next) { return calcSelect(index, next); }
	
	public ListItem currOrFirst() {	return checkRange(getPlayedIndex()); }
	
	public ListItem delCurrAndNext() {
		int selected = getPlayedIndex();
		if (selected == -1)
			return nextItem(true);

		tab.catalog.activeItem.delete();
		return checkRange(selected);
	}	
	
/////////////////////////////////////////////////////////////////////////////////////	
	public FolderNode getPrevFolder() { return next == null ? tab.catalog.root : next; }
	
	public FolderNode getNextFolder() { return prev == null ? tab.catalog.last : prev; }
	
	public void activate() { this.list.requestFocus(); }
/////////////////////////////////////////////////////////////////////////////////////	
	
	public void save(PrintWriter pw) {
		if (items.size() > 0 || folders.size() > 0) {
			pw.println((parent == null ? '>' : '<') + path);
			
			for(ListItem item : items)
				pw.println(item.saveInfo());
			
	    	for(FolderNode folder : folders) 
	    		folder.save(pw);
	    	pw.println('$');
		}
	}
}