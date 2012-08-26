package components;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import service.Common;
import service.Errorist;
import service.IOOperations;
import service.Settings;
import tabber.Tabber;

import drop_panel.DropPanelsManager;

public class MainWnd {
	static public Shell wnd;
	static public void init(Shell window) 	{ wnd = window; initializeWnd(); }
	static public void Toggle() 			{ wnd.setVisible(!wnd.isVisible());	}
	
	static private Layout initLayout() {
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        return layout;
	}
	
	static private boolean Load() {
		boolean res = true;
		Tabber.Load(wnd);
		
		try {
	  		BufferedReader br = IOOperations.GetReader(service.Settings.settingspath);
	  		String strLine;
	  		int wi = 600, he = 600;
	  		
	  		while ((strLine = br.readLine()) != null) {
	  			if (strLine.length() == 0) continue;
	  			switch(strLine.charAt(0)) {
	  				case 't': Common.drop_manager.drop_top.setVisible(strLine.charAt(1) == '1');  break;
	  				case 'b': Common.drop_manager.drop_bottom.setVisible(strLine.charAt(1) == '1'); break;
	  				case 'w': wi = Integer.parseInt(strLine.substring(1, strLine.length())); break;
	  				case 'h': he = Integer.parseInt(strLine.substring(1, strLine.length())); break;
	  			}
	  		}
	  		br.close();
	  		wnd.setSize(wi, he);
	  		wnd.pack();
		} 
		catch (Exception e) {
			Errorist.printLog(e);
			res = false;
		}

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		wnd.setLocation(dim.width - wnd.getSize().x, dim.height - wnd.getSize().y - 40);
		return res;
	}
	static private void Save() {
	    Common.tabber.Save();
	    Common.library.Save();
	    Common.drop_manager.saveDropPanels();		
		
	    PrintWriter pw = null;
	    File f = new File(Settings.settingspath);

	    try {
	    	pw = new PrintWriter(new FileWriter(f));
	        
	        pw.println('t' + (Common.drop_manager.drop_top.isVisible() ? "1" : "0"));
	        pw.println('b' + (Common.drop_manager.drop_bottom.isVisible() ? "1" : "0"));
	        pw.println("w" + wnd.getSize().x);
	        pw.println("h" + wnd.getSize().y);
	        
	        pw.flush();
	    }
	    catch (IOException e) { Errorist.printLog(e); }
	    finally { if (pw != null)  pw.close(); }
	}	
	
	static void initializeWnd() {
		wnd.setSize(240, 400);
		wnd.setMinimumSize(240, 200);

		wnd.setLayout(initLayout());
		new MenuBar().PrepareToolBar(wnd, Common.tabber);		
		
		Common.drop_manager = new DropPanelsManager(wnd);
		Common.drop_manager.initializeUpArrowButtons();
		boolean close_all = Load();
		Common.drop_manager.initializeDownArrowButtons();
		if (close_all) Common.drop_manager.CloseAll();
		wnd.setVisible(true);	
	}
	
	static public void destroy() {
		Common.Shutdown();
		Save();
	}
}