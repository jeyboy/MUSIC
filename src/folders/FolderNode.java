package folders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
	FileList list;
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
						BorderFactory.createEmptyBorder(1, add_area ? 6 : 1, 1, 1),
						BorderFactory.createLineBorder(Color.white, 2, true)
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
		if (parentNode != null)
			parentNode.folders.add(this);
		init(path);
	}
	public FolderNode(Tab container, String path) {
		super(container);
		init(path); 
	}
	
    void AddAssocIcon(String ext, Icon icon) {
    	if (listrender.icons.containsKey(ext) || icon == null) return; 
    	listrender.icons.put(ext, icon); 
    }	
		
	public void addItem(ListItem listItem) {
        AddAssocIcon(listItem.ext, FileSystemView.getFileSystemView().getSystemIcon(listItem.file));
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