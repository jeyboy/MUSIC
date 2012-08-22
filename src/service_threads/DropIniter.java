package service_threads;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import service.Errorist;
import service.Settings;
import tabber.Tab;

public class DropIniter extends BaseThread {
	ArrayList<DropCell> drops_collection = new ArrayList<DropCell>();
	
    public DropIniter() {
//		this.setDaemon(true);
		this.start();
		this.setPriority(Thread.MAX_PRIORITY);
    }

    public synchronized void run() { routing(); }
    
    public void ProceedDrop(Tab curr_tab, File [] curr_elems) {
    	drops_collection.add(new DropCell(curr_tab, curr_elems));
    }
    
    void ProcCollection(final DropCell temp, final int max_deep_level) {
        try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    public void run() {
			    	DefaultMutableTreeNode node = temp.tab.GetNode(temp.elems[0].getParent());
			    	Vector<File> filelist = addFilesRecursively(node, temp.elems, 1, max_deep_level, new Vector<File>());
			    	temp.tab.AddFileItems(node, filelist);
			    	temp.tab.LockPaint(false);
			    }
			    
				Vector<File> addFilesRecursively(DefaultMutableTreeNode node, File[] children, int level, int max_level, Vector<File> filelist) {
				    if (children != null) {
				    	Vector<File> flist = new Vector<File>();
				    	for (File child : children) {
				    		File [] ch = child.listFiles();
				    		if (ch == null)
				    			filelist.add(child);
				    		else {
			    		    	if ((level > max_level)) {
			    		    		filelist = addFilesRecursively(node, ch, level + 1, max_level, filelist);    		    		
			    		    	} else {
			    		    		DefaultMutableTreeNode child_node = new DefaultMutableTreeNode(child.getName());
			    		    		node.add(child_node);
			    		        	flist = addFilesRecursively(child_node, ch, level + 1, max_level, flist);
			    		        	temp.tab.AddFileItems(child_node, flist);
			    		        	flist.clear();
			    		    	}
				    		}
				    	}
				    }
				    return filelist;
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
      
    void routing() {
    	while(!closeRequest()) {
            while(drops_collection.size() > 0) {
            	if (closeRequest()) return;
            	ProcCollection(drops_collection.get(0), Settings.scan_deep_level);
            	drops_collection.remove(0);
            }
            
	        try { wait(sleep_time); }
	        catch (InterruptedException e) { Errorist.printLog(e); }
    	}    	
    }
	
	class DropCell {
		public Tab tab;
		public File [] elems;
		
		public DropCell(Tab curr_tab, File [] curr_elems) {
			tab = curr_tab;
			elems = curr_elems;
		}
	}
}
