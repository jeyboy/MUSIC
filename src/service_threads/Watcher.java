package service_threads;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import service.Errorist;
import tabber.Tab;

public class Watcher extends BaseThread {
	ArrayList<WatchCell> path_collection = new ArrayList<WatchCell>();
	WatchService watcher;
	
    public Watcher() throws IOException {
    	watcher = FileSystems.getDefault().newWatchService();
    	
//		this.setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
    	start();
    }

    synchronized public void run() { routing(); }
    
    WatchKey registerWatcher(File f) throws IOException, InterruptedException {
        f.toPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
        		StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW);

        return watcher.take();    	
    }
    
    public void AddElem(Tab tab, File f) {
    	if (f.exists()) {
    		try { path_collection.add(new WatchCell(tab, f, registerWatcher(f))); }
    		catch (IOException | InterruptedException e) { Errorist.printLog(e); }
    	}
    }

    void routing() {
    	WatchCell temp;
    	while(!closeRequest()) {
            while(path_collection.size() > 0) {
            	for(int loop1 = path_collection.size() - 1; loop1 >= 0 ; loop1--) {
	            	if (closeRequest()) return;
	            	while(locked) sleepy();
	            	
	            	temp = path_collection.get(loop1);
	            	
	    	        List<WatchEvent<?>> events = temp.watchKey.pollEvents();
	    	        for (WatchEvent<?> event : events) {
	    	             if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
	    	                 System.out.println("Created: " + event.context().toString());
//	    	            	 temp.tab.
	    	             }
	    	             if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
	    	                 System.out.println("Delete: " + event.context().toString());
	    	             }
	    	             if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
	    	                 System.out.println("Modify: " + event.context().toString());
	    	             }
	    	             if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
	    	                 System.out.println("Modify: " + event.context().toString());
	    	             }	             
	    	         }	            	
            	}
            }

            sleepy();
    	}
    }
    
//    void load() {
//    	BufferedReader reader = null;
//    	
//    	try {
//    		reader = IOOperations.GetReader(Constants.trashpath);
//    		String temp;
//    		
//    		while((temp = reader.readLine()) != null)
//    			AddElem(new File(temp.substring(1)), temp.charAt(0) == '1');
//		}
//    	
//    	catch (IOException e) { Errorist.printLog(e); }
//    	if (reader != null) {
//			try { reader.close(); }
//    		catch (IOException e) { Errorist.printLog(e); }
//    	}
//    }
//    
//    public void save() {
//    	PrintWriter wri = null;
//		try {
//			wri = IOOperations.GetWriter(Constants.trashpath, true, false);
//			for(TrashCell f : path_collection)
//				wri.println(f.ToString());
//		} 
//		catch (FileNotFoundException | UnsupportedEncodingException e) { Errorist.printLog(e); }
//		
//		if (wri != null) wri.close();
//    }
    
    class WatchCell {
    	File folder;
    	WatchKey watchKey;
    	Tab tab;
    	
    	public WatchCell(Tab t, File folder, WatchKey key) {
    		tab = t;
    		this.folder = folder;
    		watchKey = key;
    	}
    }
}

//public void AddWatcher(String path) {
//	Path myDir = Paths.get(path); 
//	
//    try {
//        WatchService watcher = myDir.getFileSystem().newWatchService();
//        myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
//        		StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW);
//
//        WatchKey watckKey = watcher.take();
//
//        List<WatchEvent<?>> events = watckKey.pollEvents();
//        for (WatchEvent<?> event : events) {
//             if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
//                 System.out.println("Created: " + event.context().toString());
//             }
//             if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
//                 System.out.println("Delete: " + event.context().toString());
//             }
//             if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
//                 System.out.println("Modify: " + event.context().toString());
//             }
//             if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
//                 System.out.println("Modify: " + event.context().toString());
//             }	             
//         }
//     }
//     catch (Exception e) { Errorist.printLog(e); }		
//}


//public static void watch(final File dir,final WatchService watcher) {
//	
//    Path path = dir.toPath();
//    try {
//        final WatchKey bDirWatchKey = path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
//        new Thread(new Runnable() {
//            public void run() {
//                System.out.println("Watching: "+dir.getName());
//                while(true) {
//                    try {Thread.sleep(1000);} catch (InterruptedException e) {}
//                    List<WatchEvent<?>> events = bDirWatchKey.pollEvents();
//                    for(WatchEvent<?> event:events) {
//                        System.out.println(dir.getName()+" event: #"+event.count()+","+event.kind()+" File="+event.context());
//                    }
//                }                   
//            }
//        }).start();
//    } catch (IOException x) {
//        x.printStackTrace();
//    }
//}