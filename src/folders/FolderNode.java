package folders;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
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
import tabber.Tab;

public class FolderNode extends Base {
	FolderNode parent = null;
	FolderNode next = null;
	FolderNode prev = null;
	public FileList list;
	JPanel pane;

	public String path;
	public IconListRenderer listrender = new IconListRenderer();
	
	ArrayList<ListItem> items = new ArrayList<ListItem>(10);	
	public ArrayList<ListItem> elems() { return items; } 
	
	void init(String path) {
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
	
//	public String fullPath() { return parent != null ? Utils.joinPaths(parent.fullPath(), path) : path;} 
	
	void syncTree() {
		FolderNode iter = parent;
		
		while(iter != null) {
			iter.last = this;
			iter = iter.parent;
		}
	}
	
    void addAssocIcon(String ext, Icon icon) {
    	if (listrender.icons.containsKey(ext) || icon == null) return; 
    	listrender.icons.put(ext, icon); 
    }	
		
	public void addItem(ListItem listItem) {
        addAssocIcon(listItem.ext, FileSystemView.getFileSystemView().getSystemIcon(listItem.file));
        list.model.addElement(listItem);		
		tab.catalog.iterateCount();
		Common._initer.AddItem(listItem);
	}
	
	public void delete(ListItem listItem) {
		list.model.removeElement(listItem);
		tab.catalog.deiterateCount();
	}
	
	public void addFiles(File ... files) {
		for(File f : files)
			addItem(new ListItem(this, f));
	}
	
/////////////////////////////////////////////////////////////////////////////////////	
	public int getPlayedIndex() { return list.model.indexOf(tab.catalog.activeItem); }

//	private int checkRange(int index) {
//		if (index >= list.model.getSize()) index = (list.model.getSize() - 1);
//		return (index < 0) ? 0 : index;
//	}    
//    
//	private int inverseCheckRange(int index) {
//		if (index >= list.model.getSize()) index = 0;
//		return (index < 0) ? (list.model.getSize() - 1) : index;
//	}
//
//	public int calcSelect(int curr, boolean next) {
//		return inverseCheckRange(curr + (next ? 1 : -1));
//	}	
//	
//	public ListItem currOrFirst() {
//		return list.model.getElementAt(checkRange(getPlayedIndex()));	
//	}	
//	public int MoveSelect(int index, boolean next) 	{ return calcSelect(index, next); }
//	public ListItem next(boolean next) {
//		return list.model.getElementAt(MoveSelect(getPlayedIndex(), next));	
//	}
//	public ListItem delCurrAndNext() {
//		int selected = getPlayedIndex();
//		if (selected == -1)
//			return next(true);
//		list.model.removeElement(selected);
//		
//		if ((selected = inverseCheckRange(selected)) == -1) return;
//	}
	
	private int CheckRange(int index) {
		if (index >= list.model.getSize()) index = (list.model.getSize() - 1);
		return (index < 0) ? 0 : index;
	}    
    
	private int InverseCheckRange(int index) {
		if (index >= list.model.getSize()) index = 0;
		return (index < 0) ? (list.model.getSize() - 1) : index;
	}

	public int calcSelect(int curr, boolean next) {
		int index = InverseCheckRange(curr + (next ? 1 : -1));
		list.setSelectedIndex(index);
		return index;
	}
	public int moveSelect(int index, boolean next) 	{ return calcSelect(index, next); }
	
	public void currOrFirst() {
		list.model.getElementAt(CheckRange(getPlayedIndex()));	
	}	
	public void next(boolean next) {
		list.model.getElementAt(moveSelect(getPlayedIndex(), next));	
	}
	public void delCurrAndNext() {
		int selected = getPlayedIndex();
		if (selected == -1) {
			next(true);
			return;
		}
		
		list.model.removeElement(selected);
		
		if ((selected = InverseCheckRange(selected)) == -1) return;
//		ensureIndexIsVisible(selected);
//		SetPlayed(model.elementAt(selected));
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
				pw.println(item.SaveInfo());
			
	    	for(FolderNode folder : folders) 
	    		folder.save(pw);
	    	pw.println('$');
		}
	}
}