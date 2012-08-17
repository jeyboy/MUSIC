package service_threads;

import java.util.ArrayList;
import filelist.ListItem;

import service.Common;
import service.Errorist;

public class ItemsStateIniter extends BaseThread {
	ArrayList<ListItem> items_collection = new ArrayList<ListItem>();
	
    public ItemsStateIniter() {
		this.setDaemon(true);
		this.start();
//		this.setPriority(Thread.NORM_PRIORITY);
    }

    public synchronized void run() { routing(); }
    
    public void AddItem(ListItem item) {
    	items_collection.add(item);
    }
      
    void routing() {
    	while(!closeRequest()) {
	        while(items_collection.size() > 0) {
	        	if (closeRequest()) return;
	        	Common.library.ProceedItem(items_collection.get(0));
	        	items_collection.remove(0);
	        }
	        
	        try { wait(2000); }
	        catch (InterruptedException e) { Errorist.printLog(e); }
    	}
    }
}
