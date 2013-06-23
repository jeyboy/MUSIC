package service_threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import service.Errorist;
import service.IOOperations;
import service.Constants;

public class Trasher extends BaseThread {
	ArrayList<File> path_collection = new ArrayList<File>();
	
    public Trasher() {
//		this.setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
    	start();
    }

    synchronized public void run() { routing(); }
    
    public void AddElem(String f) {
    	File file = new File(f);
    	if (file.exists())
    		path_collection.add(file);
    }

    void routing() {
    	load();
    	File temp;
    	while(!closeRequest()) {
            while(path_collection.size() > 0) {
            	for(int loop1 = path_collection.size() - 1; loop1 >= 0 ; loop1--) {
	            	if (closeRequest()) return;
	            	while(locked) sleepy();
	            	
	            	temp = path_collection.get(loop1);
	            	try {
	            		if (!temp.exists() || IOOperations.deleteFile(temp))
	            			path_collection.remove(loop1);
	            	}
	                catch(Exception e) {}
            	}
            }
            
            sleepy();
    	}
    }
    
    void load() {
    	BufferedReader reader = null;
    	
    	try {
    		reader = IOOperations.getReader(Constants.trashpath);
    		String temp;
    		
    		while((temp = reader.readLine()) != null)
    			AddElem(temp);
		}
    	
    	catch (IOException e) { Errorist.printLog(e); }
    	if (reader != null) {
			try { reader.close(); }
    		catch (IOException e) { Errorist.printLog(e); }
    	}
    }
    
    public void save() {
    	PrintWriter wri = null;
		try {
			wri = IOOperations.getWriter(Constants.trashpath, true, false);
			for(File f : path_collection)
				wri.println(f.getAbsolutePath());
		} 
		catch (Exception e) { Errorist.printLog(e); }
		
		if (wri != null) wri.close();
    }
}