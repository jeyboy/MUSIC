package components;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JRootPane;

import service.Common;
import service.Errorist;
import service.IOOperations;
import service.Constants;
import tabber.Tabber;

import drop_panel.DropPanelsManager;

public class MainWnd {
	static public Container wnd;
	static public GridBagLayout gridbag = new GridBagLayout();
	static public void init(Container window) 	{ wnd = window; initializeWnd(); }
	
	static void SetState(boolean show) {
		JFrame temp = ((JFrame)wnd);
		if (show) temp.setAlwaysOnTop(true);
		temp.setState((show ? JFrame.NORMAL : JFrame.ICONIFIED));	
		wnd.setVisible(show);
		temp.setAlwaysOnTop(false);
	}
	static public void Show() 					{ SetState(true); }
	static public void Toggle() 				{ SetState(!wnd.isVisible()); }
	
	
	static private void procElem(GridBagConstraints c, int x, int y, int w, int h, Component elem) {
		c.gridheight = h; c.gridwidth = w; c.gridx = x; c.gridy = y;
		gridbag.setConstraints(elem, c);		
	}
	static private void initLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        
        c.weightx = c.weighty = 0;
        c.ipadx = 15;
        
        procElem(c, 0, 0, 1, 7, wnd.add(Common.drop_manager.drop_left));       
        procElem(c, 4, 0, 1, 7, wnd.add(Common.drop_manager.drop_right));
        
        c.ipady = 15;
        
        procElem(c, 1, 0, 3, 1, wnd.add(Common.drop_manager.player_panel));       
        procElem(c, 2, 1, 1, 1, wnd.add(Common.drop_manager.drop_top));       
        
        c.ipady = 35;
        
        procElem(c, 1, 5, 3, 1, wnd.add(Common.drop_manager.drop_bottom));
        
        c.ipady = c.ipadx = 0;
        
        procElem(c, 1, 1, 1, 5, wnd.add(Common.drop_manager.arrow_left));
        procElem(c, 2, 2, 1, 1, wnd.add(Common.drop_manager.arrow_top));
        procElem(c, 2, 4, 1, 5, wnd.add(Common.drop_manager.arrow_bottom));                
        procElem(c, 3, 1, 1, 5, wnd.add(Common.drop_manager.arrow_right));
        
        c.weightx = c.weighty = 1;
        
        procElem(c, 2, 3, 1, 1, wnd.add(Common.tabber));
	}
	
	static private void load() {
		Tabber.load();
		int wi = 200, he = 400;
		
		try {
	  		BufferedReader br = IOOperations.getReader(Constants.settingspath);
	  		String strLine;
	  		
	  		while ((strLine = br.readLine()) != null) {
	  			if (strLine.length() == 0) continue;
	  			switch(strLine.charAt(0)) {
	  				case 't': Common.drop_manager.drop_top.setVisible(strLine.charAt(1) == '1');  break;
	  				case 'b': Common.drop_manager.drop_bottom.setVisible(strLine.charAt(1) == '1'); break;
	  				case 'l': Common.drop_manager.drop_left.setVisible(strLine.charAt(1) == '1'); break;
	  				case 'r': Common.drop_manager.drop_right.setVisible(strLine.charAt(1) == '1'); break;
	  				case 'w': wi = Integer.parseInt(strLine.substring(1, strLine.length())); break;
	  				case 'h': he = Integer.parseInt(strLine.substring(1, strLine.length())); break;
	  			}
	  		}
	  		br.close();
		} 
		catch (Exception e) {
			Common.drop_manager.CloseAll();
			Errorist.printLog(e);
		}

		wnd.setSize(wi, he);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		wnd.setLocation(new Point(dim.width - wnd.getWidth(), dim.height - wnd.getHeight() - 40));
	}
	static private void save() {
		Common._trash.save();
	    Common.tabber.save();
	    Common.library.save();
	    Common.drop_manager.saveDropPanels();		
		
	    PrintWriter pw = null;
	    File f = new File(Constants.settingspath);

	    try {
	    	pw = new PrintWriter(new FileWriter(f));
	        
	        pw.println('t' + (Common.drop_manager.drop_top.isVisible() ? "1" : "0"));
	        pw.println('b' + (Common.drop_manager.drop_bottom.isVisible() ? "1" : "0"));
	        pw.println('l' + (Common.drop_manager.drop_left.isVisible() ? "1" : "0"));
	        pw.println('r' + (Common.drop_manager.drop_right.isVisible() ? "1" : "0"));
	        pw.println("w" + wnd.getWidth());
	        pw.println("h" + wnd.getHeight());
	        
	        pw.flush();
	    }
	    catch (IOException e) { Errorist.printLog(e); }
	    finally { if (pw != null)  pw.close(); }
	}	
	
	static void initFrame(JFrame frame) {
		frame.getContentPane().setLayout(gridbag);
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(new MenuBar(Common.tabber));
		
		frame.setUndecorated(true);
		frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
	}
	static void initApplet(Applet applet) { applet.setLayout(gridbag);	}
	
	static void initializeWnd() {
		wnd.setSize(240, 400);
		wnd.setMinimumSize(new Dimension(240, 200));
		
		Common.drop_manager = new DropPanelsManager(wnd);
		load();
        initLayout();
        
		if (wnd instanceof JFrame)
			initFrame((JFrame) wnd);
		else initApplet((Applet) wnd);
		
		wnd.setVisible(true);	
	}
	
	static public void destroy() {
		Common.shutdown();
		save();
	}
	
	static public void setTitle(String title) { ((JFrame)wnd).setTitle(title);	}
}