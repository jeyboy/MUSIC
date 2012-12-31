package service_threads;

import java.text.SimpleDateFormat;
import java.util.Date;

import components.MainWnd;

import service.Common;
import service.Errorist;

public class LibraryDumper extends BaseThread {
	
    public LibraryDumper() {
		this.setDaemon(true);
    	start();
    }

    synchronized public void run() { routing(); }

    void routing() {
    	while(!closeRequest()) {
    		synchronized(Common.library) {
    			System.out.println("Try dump library at " + new Date());
    			int res = Common.library.Save();
    			MainWnd.SetTitle(new SimpleDateFormat("HH:mm").format(new Date()) + ": Saved - " + res);
    		}	            		
    		
	        try { wait(60000); }
	        catch (InterruptedException e) { Errorist.printLog(e); }
    	}
    }
}
