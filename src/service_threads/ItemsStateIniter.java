package service_threads;

import java.util.ArrayList;
import filelist.ListItem;

import service.Common;

public class ItemsStateIniter extends BaseThread {
	ArrayList<ListItem> items_collection = new ArrayList<ListItem>();
	boolean locked = false;
	
    public ItemsStateIniter() {
		setDaemon(true);
//		setPriority(Thread.NORM_PRIORITY);
//		start();
    }

    public synchronized void run() { routing(); }
    
    public void AddItem(ListItem item) {
    	items_collection.add(item);
    }
      
    void routing() {
    	while(!closeRequest()) {
	        while(items_collection.size() > 0) {
	        	if (closeRequest()) return;
	        	while(locked) sleepy();
	        	
	        	Common.library.ProceedItem(items_collection.get(0));
	        	items_collection.remove(0);
	        }
	        
	        sleepy();
    	}
    }
    
    
}