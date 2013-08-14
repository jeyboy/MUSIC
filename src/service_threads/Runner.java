package service_threads;

import filelist.ListItem;

public class Runner extends BaseThread {
	ListItem temp = null;
	
    public Runner() {
		this.setDaemon(true);
    	start();
    }

    synchronized public void run() { routing(); }
    
    public void AddElem(ListItem f) {
    	temp = f;
    }

    void routing() {
    	while(!closeRequest()) {
            if(temp != null) {
            	temp.exec();
            	temp = null;
            }
            
            sleepy(100);
    	}
    }
}