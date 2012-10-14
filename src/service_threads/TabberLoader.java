package service_threads;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import service.Common;
import service.Errorist;
import service.IOOperations;
import tabber.Tab;
import tabber.TabOptions;
import filelist.FileList;
import filelist.ListItem;

public class TabberLoader extends BaseThread {
	
    public TabberLoader() {
//		setDaemon(true);
    	setPriority(Thread.MAX_PRIORITY);    	
		start();
    }

    public synchronized void run() { routing(); }
    
    synchronized void routing() {
    	int limit = 75;
    	FileList list = null;
    	ArrayList<ListItem> files = new ArrayList<ListItem>(limit);
		
		try {
			BufferedReader bin = IOOperations.GetReader(service.Settings.tabspath);
	  		String strLine;
	  		Tab curr_tab = null;
	  		
	  		while ((strLine = bin.readLine()) != null) {
	  			if (strLine.length() == 0) continue;
	  			switch(strLine.charAt(0)) {
	  				case '*':
	  					curr_tab = Common.tabber.AddTab(strLine.substring(2), new TabOptions(strLine.charAt(1)));
	  					list = curr_tab.Files();
	  					list.setEnabled(false);
	  					list.setVisible(false);
	  					list.setIgnoreRepaint(true);
	  					break;
	  				case 'f': 
	  					ListItem t_item = ListItem.Load(strLine);
	  					if (!curr_tab.options.interactive || (curr_tab.options.interactive && t_item.file.exists())) {
	  						files.add(t_item);
	  						if (files.size() == limit) {
	  							proceedTab(list, files);
	  							files.clear();
	  						}
	  					}
	  					break;
	  				case ' ':
	  					proceedTab(list, files);
	  					//curr_tab.Files().AddElemsLI(files);
	  					files.clear();	  					
	  					list.setEnabled(true);
	  					list.setVisible(true);
	  					list.setIgnoreRepaint(false);
	  					list.repaint();
	  					break;
	  			}
	  		}
	  		bin.close();
	  		Common.save_flag = true;
		}
		catch (FileNotFoundException e) { Common.save_flag = true; }
		catch (Exception e) { Errorist.printLog(e); }
		System.out.println("===========" + Common.save_flag);
    }
    
    void proceedTab(final FileList list, final ArrayList<ListItem> items) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				synchronized public void run(){
			    	list.AddElemsLI(items);
			    }
			});
		} 
		catch (InvocationTargetException | InterruptedException e) { Errorist.printLog(e); }
    }
    
//    class Temp implements Runnable {
//    	Tab curr_tab;
//    	ArrayList<ListItem> files;
//
//    	public Temp(Tab tab, ArrayList<ListItem> tab_files) {
//    		curr_tab = tab;
//    		files = tab_files;
//    	}
//    	
//		@Override
//		public void run() {
//			curr_tab.Files().AddElemsLI(files);
//		}
//    } 
}
