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
		System.out.println(select_add);
		
		if (!select_add) clearAll();		
		ArrayList<int []> new_item = new ArrayList<int []>();
		new_item.add(indexes);		
		folders.put(node, new_item);
	}
	
	public void clearAll() {
		if (!routine) {
			routine = true;
			System.out.println("Clear");
			for(Entry<FolderNode, ArrayList<int []>> node : folders.entrySet())
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