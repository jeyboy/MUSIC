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
	ArrayList<TrashCell> path_collection = new ArrayList<TrashCell>();
	
    public Trasher() {
//		this.setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
    	start();
    }

    synchronized public void run() { routing(); }
    
    public void AddElem(String f, boolean delete_folder) {
    	File file = new File(f);
    	if (file.exists())
    		path_collection.add(new TrashCell(file, delete_folder));
    }

    void routing() {
    	load();
    	TrashCell temp;
    	while(!closeRequest()) {
            while(path_collection.size() > 0) {
            	for(int loop1 = path_collection.size() - 1; loop1 >= 0 ; loop1--) {
	            	if (closeRequest()) return;
	            	while(locked) sleepy();
	            	
	            	temp = path_collection.get(loop1);
	            	try {
	            		if (!temp.file.exists() || IOOperations.deleteFile(temp.file))
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
    			AddElem(temp.substring(1), temp.charAt(0) == '1');
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
			for(TrashCell f : path_collection)
				wri.println(f.ToString());
		} 
		catch (Exception e) { Errorist.printLog(e); }
		
		if (wri != null) wri.close();
    }
    
    class TrashCell {
    	File file;
    	boolean delete_folder;
    	
    	public TrashCell(File del_file, boolean check_folder) {
    		file = del_file;
    		delete_folder = check_folder;
    	}
    	
    	public String ToString() { return (delete_folder ? "1" : "0") + file.getAbsolutePath();	}
    }
}