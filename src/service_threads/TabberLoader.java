package service_threads;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.SwingWorker;

import service.Common;
import service.Constants;
import service.Errorist;
import service.IOOperations;
import service.Utils;
import tabber.Tab;
import tabber.TabOptions;
import filelist.ListItem;
import folders.FolderNode;

public class TabberLoader extends SwingWorker<Boolean, Cell> {
	public TabberLoader() { }
	
    public void proc(BufferedReader bin, Tab curr_tab, String path, FolderNode folder) throws IOException {
  		String strLine;   	
    	
  		while ((strLine = bin.readLine()) != null) {
  			if (strLine.length() == 0) continue;
 
  			switch(strLine.charAt(0)) {
  				case '$': return;
  				case '<':
  					String name = strLine.substring(1);
  					proc(bin, curr_tab, Utils.joinPaths(path, name), new FolderNode(folder, name));
  					break;				  				
  				case 'f': 
  					ListItem t_item = ListItem.load(folder, path, strLine);
  					if (!curr_tab.options.interactive || (curr_tab.options.interactive && t_item.file().exists()))
  						publish(new Cell(folder, t_item));
  					
//  					setProgress( (i+1) * 100 / size);
  					
  					break;
  			}
  		}
    }

	protected Boolean doInBackground() throws Exception {
		try {			
			try {
				BufferedReader bin = IOOperations.getReader(Constants.tabspath);
		  		String strLine;
		  		Tab curr_tab = null;
		  		String path = null;
		  		FolderNode folder = null;
		  		
		  		while ((strLine = bin.readLine()) != null) {
		  			if (strLine.length() == 0) continue; 
		  			switch(strLine.charAt(0)) {
		  				case '*':
		  					curr_tab = Common.tabber.addTab(strLine.substring(2), new TabOptions(strLine.charAt(1)));
		  					break;
		  				case '>': 
		  					path = strLine.substring(1);
		  					folder = curr_tab.catalog.getNode(path);
		  					proc(bin, curr_tab, path, folder);
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
		catch( Exception e) { Errorist.printLog(e); }
		return Common.save_flag;
	}
	
	protected void process(List<Cell> cells) {
		for(Cell item : cells)
			item.folder.addItem(item.item);
	}
}

class Cell {
	ListItem item;
	FolderNode folder;
	
	public Cell(FolderNode folder, ListItem item) {
		this.folder = folder;
		this.item = item;
	}
}