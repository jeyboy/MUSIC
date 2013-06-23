package service_threads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import filelist.ListItem;
import folders.FolderNode;

import service.Errorist;
import service.IOOperations;
import service.Utils;

public class Watcher extends BaseThread {
	ArrayList<WatchCell> path_collection = new ArrayList<WatchCell>();
	
    public Watcher() throws IOException {   	
//		this.setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
    	start();
    }

    synchronized public void run() { routing(); }
       
    public void addElem(FolderNode folder) {
    	File f = new File(folder.path);
    	if (f.exists()) {
    		try { path_collection.add(new WatchCell(folder)); }
    		catch (JNotifyException e) { Errorist.printLog(e); }
    	}
    }

    void routing() {
    	while(!closeRequest()) { sleepy(); }
    	
    	for(int loop1 = path_collection.size() - 1; loop1 >= 0 ; loop1--) {
			try { path_collection.get(loop1).stopWatching(); }
			catch (JNotifyException e) { Errorist.printLog(e); }	
    	}
    }
    
    class WatchCell {
    	int watchID;
    	
    	public WatchCell(FolderNode t) throws JNotifyException {
    	    // watch mask, specify events you care about,
    	    // or JNotify.FILE_ANY for all events.
    	    int mask = JNotify.FILE_CREATED  | 
    	               JNotify.FILE_DELETED  | 
//    	               JNotify.FILE_MODIFIED | 
    	               JNotify.FILE_RENAMED;

    	    // watch subtree?
    	    boolean watchSubtree = true;
    	    watchID = JNotify.addWatch(t.path, mask, watchSubtree, new Listener(t));
    	}
    	
    	public boolean stopWatching() throws JNotifyException {
    		return JNotify.removeWatch(watchID);
    	}
    }
    
    class Listener implements JNotifyListener {
    	FolderNode node;
    	
    	public Listener(FolderNode folder) { node = folder;	} 
    	
        public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        	ListItem item = findItem(oldName);
        	if (item != null) {
        		item.title = IOOperations.filename(newName);
        		item.ext = IOOperations.extension(newName);
        	}
        }
        
        public void fileDeleted(int wd, String rootPath, String name) {
        	ListItem item = findItem(name);
        	if (item != null)
        		item.delete();
        }
        
        public void fileCreated(int wd, String rootPath, String name) {
        	node.addFiles(Utils.joinPaths(rootPath, name));
        }
        
        public void fileModified(int wd, String rootPath, String name) {}
        
        ListItem findItem(String name) {
        	return node.list.model.findByTitle(IOOperations.filename(name));
        }
    }    
}
