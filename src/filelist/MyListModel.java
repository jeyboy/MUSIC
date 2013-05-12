package filelist;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import folders.FolderNode;

@SuppressWarnings("rawtypes")
public class MyListModel extends AbstractListModel {
	private static final long serialVersionUID = -3009647312409052639L;
	FolderNode list;
	
	public MyListModel(FolderNode node) {
		list = node;
		addListDataListener(new ListDataListener() {
			public void intervalAdded(ListDataEvent e) 		{ list.tab.updateCounter(); }
			public void intervalRemoved(ListDataEvent ev) 	{ list.tab.updateCounter(); }
			public void contentsChanged(ListDataEvent e) {}}
		); 
	}
	
	public void repaint(int index) {
		fireContentsChanged(this, index - 1, index + 1);
	}
	
	public void addElement(ListItem item) {
		list.elems().add(item);
		fireIntervalAdded(this, list.elems().size() - 1, list.elems().size());
	} 
	public void removeElement(int i) {
		list.elems().remove(i);
		fireIntervalRemoved(this, i, i);
	}
	public void removeElement(Object obj) 	{ removeElement(indexOf(obj)); }
	public ListItem getElementAt(int i) 	{ return list.elems().get(i); }
	public int indexOf(Object o) 			{ return list.elems().indexOf(o); }
	public int getSize() 					{ return list.elems().size(); }
}