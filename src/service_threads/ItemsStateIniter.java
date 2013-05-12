package service_threads;

import java.util.ArrayList;
import java.util.List;

import filelist.ListItem;

import service.Common;

public class ItemsStateIniter extends BaseThread {
	List<ListItem> items_collection = new ArrayList<ListItem>();
	boolean locked = false;
	
    public ItemsStateIniter() {
		setDaemon(true);
//		setPriority(Thread.NORM_PRIORITY);
		start();
    }

    public synchronized void run() { routing(); }
    
    public void addItem(ListItem item) {
    	items_collection.add(item);
    }
    
    public void prependItem(ListItem item) {
    	if (items_collection.size() > 0)
    		items_collection.set(0, item);
    	else addItem(item);
    }
      
    void routing() {
    	while(!closeRequest()) {
	        while(items_collection.size() > 0) {
	        	if (closeRequest()) return;
	        	while(locked) sleepy();
	        	
//	        	System.out.println("Init " + items_collection.get(0));
	        	Common.library.ProceedItem(items_collection.get(0));
	        	items_collection.remove(0);
	        }
	        
	        sleepy();
    	}
    }
}