package service_threads;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.SwingUtilities;

import service.Errorist;
import service.Settings;
import tabber.Tab;

public class DropIniter extends BaseThread {
	ArrayList<DropCell> drops_collection = new ArrayList<DropCell>();
	
    public DropIniter() {
		this.setDaemon(true);
		this.start();
		this.setPriority(Thread.MAX_PRIORITY);
    }

    public synchronized void run() { routing(); }
    
    public void ProceedDrop(Tab curr_tab, File [] curr_elems) {
    	drops_collection.add(new DropCell(curr_tab, curr_elems));
    }
    
    void ProcCollection(final DropCell temp, final int max_deep_level) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Vector<File> filelist;
            	for(File item : temp.elems) {
            		filelist = addFilesRecursively(item, 1, max_deep_level, new Vector<File>());
            		temp.tab.AddFileItems(item.getParent(), filelist);
            	}
            }
            
    		Vector<File> addFilesRecursively(File file, int level, int max_level, Vector<File> filelist) {
    		    final File[] children = file.listFiles();
    		    if (children != null) {
    		    	if ((level > max_level)) {
        		        for (File child : children) 
        		        	filelist = addFilesRecursively(child, level + 1, max_level, filelist);    		    		
    		    	} else {
        		    	Vector<File> flist;
        		        for (File child : children) {
        		        	flist = addFilesRecursively(child, level + 1, max_level, new Vector<File>());
        		        	temp.tab.AddFileItems(child.getParent(), flist);
        		        }
    		    	}
    		    }
    		    else filelist.add(file);
    		    return filelist;
    		}
        });    	
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
