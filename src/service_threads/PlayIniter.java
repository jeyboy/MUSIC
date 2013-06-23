package service_threads;

import service.Common;
import tabber.Tab;

public class PlayIniter extends BaseThread {
	int wait_time = -100000;
	Tab tab;
	
    public PlayIniter() {
		this.setDaemon(true);
    	start();
    }

    synchronized public void run() { routing(); }
    
    public void addElem(Tab play_tab, int wait) {
    	if (play_tab.options.play_next) {
	    	wait_time = wait * 1000 + 2000;
	    	tab = play_tab;
    	}
    }
    
    public void stopRoutind() { wait_time = -100000; }

//    TODO: add behavior for stop playing and reaction on selecting next track for outer player
    // use process state maybe 
    
    void routing() {
    	while(!closeRequest()) {           
            sleepy();
            
            if (wait_time > 0)
            	wait_time -= sleep_time;
            
            if (wait_time != -100000 && wait_time <= 0) {
            	stopRoutind();
            	if (Common.tabber.currTab() == tab) 
            		tab.catalog.execNext(true);
            }
    	}
    }
}