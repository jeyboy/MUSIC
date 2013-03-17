package service_threads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

import folders.FolderNode;

import service.Errorist;
import tabber.Tab;

public class DropIniter extends BaseThread {
	ArrayList<DropCell> drops_collection = new ArrayList<DropCell>();
	
    public DropIniter() {
		setDaemon(true);
//		setPriority(Thread.MAX_PRIORITY);
		start();
    }

    public synchronized void run() {
    	try { routing(); }
    	catch (IOException e) { Errorist.printLog(e); }
    }
    
    public void ProceedDrop(Tab tab, File [] curr_elems) {
    	drops_collection.add(new DropCell(tab, curr_elems));
    }
    
	private void addFilesRecursively(FolderNode node, File [] files) throws IOException {
		for(File file : files) {
			if (file.isDirectory())
				addFilesRecursively(new FolderNode(node, file.getName()), file.listFiles());
			else node.addFiles(file);
		}
	}
      
    void routing() throws IOException {
    	while(!closeRequest()) {
            while(drops_collection.size() > 0) {
            	if (closeRequest()) return;
            	
            	while(locked) sleepy();
            	
            	final DropCell temp = drops_collection.get(0);            	
            	
                SwingUtilities.invokeLater(new Runnable() {
                    public void run(){
                    	try { addFilesRecursively(temp.tab.catalog.getNode(temp.elems[0].getParentFile().getCanonicalPath()), temp.elems);	}
                    	catch (IOException e) {	Errorist.printLog(e); }
//                    	temp.list.AddElemsF(IOOperations.ScanDirectoriesF(temp.elems));
                    }
                });
                
            	drops_collection.remove(0);
            }
            
            sleepy();
    	}    	
    }
	
	class DropCell {
		public Tab tab;
		public File [] elems;
		
		public DropCell(Tab parent_tab, File [] curr_elems) {
			tab = parent_tab;
			elems = curr_elems;
		}
	}
}
