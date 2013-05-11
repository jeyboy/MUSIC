package folders;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

public class CatalogSelection {
	Hashtable<FolderNode, ArrayList<int []>> folders = new Hashtable<FolderNode, ArrayList<int []>>();
	FolderNode focus_node = null;
	boolean select_add = false, routine = false;
	
	public void setInterval(FolderNode node, int [] indexes) {		
		if (!select_add) clearAll(node);		
		ArrayList<int []> new_item = new ArrayList<int []>();
		new_item.add(indexes);		
		folders.put(node, new_item);
	}
	
	public void clearAll(FolderNode except_node) {
		if (!routine) {
			routine = true;
			for(Entry<FolderNode, ArrayList<int []>> node : folders.entrySet())
				if (node.getKey() != except_node)
					node.getKey().list.clearSelection();
			
			folders.clear();
			routine = false;
		}
	}
	
	public ArrayList<int []> getNodeSelections(FolderNode node, boolean sort) {
		return folders.get(node);
	}
	
	public Set<FolderNode> getNodes() { return folders.keySet(); }
	
	public void setFocus(FolderNode node) { focus_node = node; }
	
	public FolderNode getFocus() { return focus_node; }
	
	public boolean getMaskState() { return select_add; }
	public void setMaskState(boolean state) { select_add = state; }
}