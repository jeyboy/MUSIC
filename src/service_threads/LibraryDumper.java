package service_threads;

import java.text.SimpleDateFormat;
import java.util.Date;

import components.MainWnd;

import service.Common;

public class LibraryDumper extends BaseThread {
	
    public LibraryDumper() {
		this.setDaemon(true);
    	start();
    }

    synchronized public void run() { routing(); }

    void routing() {
    	while(!closeRequest()) {
    		while(locked) sleepy();
    		synchronized(Common.library) {
    			System.out.println("Try dump library at " + new Date());
    			int res = Common.library.save();
    			MainWnd.setTitle(new SimpleDateFormat("HH:mm").format(new Date()) + ": Saved - " + res);
    		}	            		
    		
    		sleepy(60000);
    	}
    }
}