package service_threads;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import service.Common;
import service.Errorist;
import service.IOOperations;
import service.Settings;
import tabber.TabOptions;

public class TabberLoader extends BaseThread {
	
    public TabberLoader() {
		setPriority(Thread.MAX_PRIORITY);    	
		start();
    }

    public synchronized void run() { routing(); }
    
    synchronized void routing() {
		try {
			BufferedReader bin = IOOperations.GetReader(service.Settings.tabspath);
	  		String strLine;
	  		
	  		while ((strLine = bin.readLine()) != null) {
	  			if (strLine.startsWith(Settings._tab))
	  				Common.tabber.AddTab(strLine.substring(2), new TabOptions(strLine.charAt(1))).Load(bin);
	  		}
	  		bin.close();
	  		Common.save_flag = true;
		}
		catch (FileNotFoundException e) { Common.save_flag = true; }
		catch (Exception e) { Errorist.printLog(e); }
		System.out.println("===========" + Common.save_flag);
    } 
}
