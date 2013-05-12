package service_threads;

import java.util.ArrayList;

import filelist.ListItem;

import service.Common;
import tabber.Tab;

public class ItemsStateRefresher extends BaseThread {
	ArrayList<String[]> examples_collection = new ArrayList<String[]>();
	
    public ItemsStateRefresher() {
		setDaemon(true);
//		setPriority(Thread.NORM_PRIORITY);
		start();
    }

    synchronized public void run() { routing(); }
    
    public void AddExample(String [] pieces) {
    	examples_collection.add(pieces);
    }

    void routing() {
    	while(!closeRequest()) {
            while(examples_collection.size() > 0) {
            	if (closeRequest()) return;
            	cycle(examples_collection.get(0));
            	examples_collection.remove(0);
            }
            
            sleepy();
    	}     	
    }
    
    void cycle(String [] examples) {
        for(int loop = 0; loop < Common.tabber.getTabCount(); loop++) {
        	if (closeRequest()) return;
        	while(locked) sleepy();
        	procTab(Common.tabber.getTab(loop), examples);
        }
    }
    
    void procTab(Tab tab, String [] examples) {
    	for(int loop1 = 0; loop1 < tab.filesCount(); loop1++) {
    		if (closeRequest()) return;
    		//TODO: release
//    		procItem(tab.File(loop1), examples);
    	}
    }
    
    void procItem(ListItem item, String [] examples) {
		for(String title : item.mediaInfo().Titles) {
			if (closeRequest()) return;
			for(String coll_item : examples)
				if (title == coll_item) {
					item.setStatusLiked();
					return;
				}
		}
    }    
    
      
//    void routing() {
//        for(int loop = 0; loop < Common.tabber.getTabCount(); loop++) {
//        	if (closeRequest()) return;
//        	procTab(Common.tabber.GetTab(loop));
//        }
//    }
//    
//    void procTab(Tab tab) {
//    	for(int loop1 = 0; loop1 < tab.FilesCount(); loop1++) {
//    		if (closeRequest()) return;
//    		procItem(tab.File(loop1));
////    		Common.library.ProceedItem(tab.File(loop1));
//    	}
//    }
//    
//    void procItem(ListItem item) {
//		for(String title : item.media_info.Titles)
//			for(String[] collection : examples_collection) {
//				if (closeRequest()) return;
//				for(String coll_item : collection)
//					if (title == coll_item) {
//						item.state = STATUS.LIKED;
//						return;
//					}
//			}
//    }    
}
