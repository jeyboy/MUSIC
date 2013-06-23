package service_threads;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import folders.FolderNode;

import service.Errorist;

public class NativeWatcher extends BaseThread {
	ArrayList<WatchCell> path_collection = new ArrayList<WatchCell>();
	WatchService watcher;
	
    public NativeWatcher() throws IOException {
    	watcher = FileSystems.getDefault().newWatchService();
    	
//		this.setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
    	start();
    }

    synchronized public void run() { routing(); }
    
    WatchKey registerWatcher(File f) throws IOException, InterruptedException {
        return f.toPath().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE,
        		StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.OVERFLOW);   	
    }
    
    public void addElem(FolderNode folder) {
    	File f = new File(folder.path);
    	if (f.exists()) {
    		try { path_collection.add(new WatchCell(folder, registerWatcher(f))); }
    		catch (IOException | InterruptedException e) { Errorist.printLog(e); }
    	}
    }

    void routing() {
    	WatchCell temp;
    	while(!closeRequest()) {
            while(path_collection.size() > 0) {
            	for(int loop1 = path_collection.size() - 1; loop1 >= 0 ; loop1--) {
	            	if (closeRequest()) return;
	            	while(locked) sleepy();
	            	
	            	temp = path_collection.get(loop1);
	            	
	    	        List<WatchEvent<?>> events = temp.watchKey.pollEvents();
	    	        for (WatchEvent<?> event : events) {
	    	             if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
	    	                 System.out.println("Created: " + event.context().toString());
//	    	                 WatchEvent<Path> ev = (WatchEvent<Path>)event;
//	    	                 Path filename = ev.context();
//	    	            	 temp.folder.addFiles((String)event.context());
	    	             }
	    	             if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
	    	                 System.out.println("Delete: " + event.context().toString());
	    	             }
	    	             if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
	    	                 System.out.println("Modify: " + event.context().toString());
	    	             }
	    	             if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
	    	                 System.out.println("Overflow: " + event.context().toString());
	    	             }	             
	    	         }	            	
            	}
            }

            sleepy();
    	}
    }
    
    class WatchCell {
    	WatchKey watchKey;
    	FolderNode folder;
    	
    	public WatchCell(FolderNode t, WatchKey key) {
    		folder = t;
    		watchKey = key;
    	}
    }
}
