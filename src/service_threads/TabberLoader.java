package service_threads;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;

import service.Common;
import service.Constants;
import service.Errorist;
import service.IOOperations;
import tabber.Tab;
import tabber.TabOptions;
import filelist.FileList;
import filelist.ListItem;

public class TabberLoader extends BaseThread {
	
    public TabberLoader() {
    	setPriority(Thread.MAX_PRIORITY);    	
		start();
    }

    public synchronized void run() { routing(); }
    
    synchronized void routing() {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				synchronized public void run(){
					FileList list = null;
					try {
						BufferedReader bin = IOOperations.GetReader(Constants.tabspath);
				  		String strLine;
				  		Tab curr_tab = null;
				  		
				  		while ((strLine = bin.readLine()) != null) {
				  			if (strLine.length() == 0) continue;
				  			switch(strLine.charAt(0)) {
				  				case '*':
				  					curr_tab = Common.tabber.AddTab(strLine.substring(2), new TabOptions(strLine.charAt(1)));
				  					list = curr_tab.Files();
				  					break;
				  				case 'f': 
				  					ListItem t_item = ListItem.Load(strLine);
				  					if (!curr_tab.options.interactive || (curr_tab.options.interactive && t_item.file.exists()))
				  						list.ProceedElem(t_item);
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
			});
		} 
		catch( Exception e) { Errorist.printLog(e); }
    }
}
