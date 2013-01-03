package filelist;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class MyListModel<E> extends DefaultListModel<E> {
	private static final long serialVersionUID = -3009647312409052639L;
	private int updatecount = 10;
	
	public MyListModel(final FileList list) { addListDataListener(new ListDataListener() {
		public void intervalAdded(ListDataEvent e) { list.parent.UpdateCounter(); }
		public void intervalRemoved(ListDataEvent e) { list.parent.UpdateCounter();	}
		public void contentsChanged(ListDataEvent e) {}}); 
	}
	
	public void AddRangeOfElements(E [] items) {
		int interval = 0;
		for(int loop1 = 0, start = this.size(); loop1 < items.length; loop1++, interval++ ) {
			addElement(items[loop1]);
			if (interval == updatecount) {
				fireIntervalAdded(this, start, start+=interval);
				interval = 0;
			}
		}
		fireIntervalAdded(this, size() - interval, size());
//		fireContentsChanged(arg0, arg1, arg2)
	}
}