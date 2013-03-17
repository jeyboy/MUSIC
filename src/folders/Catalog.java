package folders;

import java.io.File;
import java.io.PrintWriter;
import tabber.Tab;

public class Catalog extends Base {
	int itemsCount = 0;
	
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
	
	public void save(PrintWriter pw) {
    	for(FolderNode folder : folders)
    		folder.save(pw);
	}
}