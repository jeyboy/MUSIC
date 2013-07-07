package folders;

import java.io.PrintWriter;
import java.util.ArrayList;

import tabber.Tab;

public abstract class Base {
	public Tab tab;
	FolderNode last = null;
	
	ArrayList<FolderNode> folders = new ArrayList<FolderNode>(1) {
		private static final long serialVersionUID = -2235109783338833765L;

		public int indexOf(Object o) {
			if (o instanceof String) {
			    for (int i = 0; i < this.size(); i++)
			        if (o.equals(this.get(i).path))
			            return i;
			} 
			else super.indexOf(o);
			return -1;
		};
	};

	public Base(Tab container) { tab = container; }

	public FolderNode find(String folder) {
		int i = folders.indexOf(folder);
		return i == -1 ? null : folders.get(i);
	}
	
	public abstract void save(PrintWriter pw);
}