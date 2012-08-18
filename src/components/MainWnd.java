package components;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import service.Settings;
import tabber.Tabber;

import drop_panel.DropPanelsManager;

public class MainWnd {
	static public Container wnd;
	static public GridBagLayout gridbag = new GridBagLayout();
	static public void init(Container window) 	{ wnd = window; initializeWnd(); }
	static public void Toggle() 				{ wnd.setVisible(!wnd.isVisible());	}
	
//	static void initializeFrame0() {
//	    JFrame frame = new JFrame("(O_o)");
//	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//	    JDesktopPane desktop = new JDesktopPane();
//	    JInternalFrame internalFrame = new JInternalFrame("Can Do All", true, true, true, true);
//
//	    desktop.add(internalFrame);
//
//	    internalFrame.setBounds(0, 0, 200, 100);
//
//	    JLabel label = new JLabel(internalFrame.getTitle(), JLabel.CENTER);
//	    internalFrame.add(label, BorderLayout.CENTER);
//
//	    internalFrame.setVisible(true);
//	    internalFrame.setDoubleBuffered(true);
//	    
//
//	    frame.add(desktop, BorderLayout.CENTER);
//	    frame.setSize(500, 300);
//	    frame.setVisible(true);				
//	}
	
//	static private JPanel initLayout() {
//		JPanel p = new JPanel(true);
//		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
//		return p;
//	}
	
	static private void initLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
		
		c.gridheight = 5; c.gridwidth = 1; c.gridx = 0; c.gridy = 0;
		gridbag.setConstraints(Common.drop_manager.drop_left, c);	wnd.add(Common.drop_manager.drop_left);
        
		c.gridheight = 5; c.gridwidth = 1; c.gridy = 0; c.gridx = 4;
        gridbag.setConstraints(Common.drop_manager.drop_right, c);	wnd.add(Common.drop_manager.drop_right);               
        
		c.gridheight = 1; c.gridwidth = 3; c.gridy = 0; c.gridx = 1;
        gridbag.setConstraints(Common.drop_manager.drop_top, c);	wnd.add(Common.drop_manager.drop_top);
        
		c.gridheight = 1; c.gridwidth = 3; c.gridy = 4; c.gridx = 1;
        gridbag.setConstraints(Common.drop_manager.drop_bottom, c);	wnd.add(Common.drop_manager.drop_bottom);
        
        
        
		c.gridheight = 3; c.gridwidth = 1; c.gridy = 1; c.gridx = 1;
		gridbag.setConstraints(Common.drop_manager.arrow_left, c); wnd.add(Common.drop_manager.arrow_left);
        
		c.gridheight = 1; c.gridwidth = 1; c.gridy = 1; c.gridx = 2;
		gridbag.setConstraints(Common.drop_manager.arrow_top, c); wnd.add(Common.drop_manager.arrow_top);                
        
		c.gridheight = 1; c.gridwidth = 1; c.gridy = 3; c.gridx = 2;
		gridbag.setConstraints(Common.drop_manager.arrow_bottom, c); wnd.add(Common.drop_manager.arrow_bottom);
        
		c.gridheight = 3; c.gridwidth = 1; c.gridy = 1; c.gridx = 3;
		gridbag.setConstraints(Common.drop_manager.arrow_right, c); wnd.add(Common.drop_manager.arrow_right);
        
        
		
		c.gridheight = 1; c.gridwidth = 1; c.gridy = 2; c.gridx = 2; c.weightx = c.weighty = 1;
        gridbag.setConstraints(Common.tabber, c); wnd.add(Common.tabber);
	}
	
	static private void Load() {
		Tabber.Load();
		
		try {
	  		BufferedReader br = IOOperations.GetReader(service.Settings.settingspath);
	  		String strLine;
	  		int wi = 200, he = 400;
	  		
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
	  		wnd.setSize(wi, he);
		} 
		catch (Exception e) {
			Common.drop_manager.CloseAll();
			Errorist.printLog(e);
		} 
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
	static void initApplet(Applet applet) {
		applet.setLayout(gridbag);
	}
	
	static void initializeWnd() {
		wnd.setSize(240, 400);
		wnd.setMinimumSize(new Dimension(240, 200));
		
		Common.drop_manager = new DropPanelsManager(wnd);
		Load();
        initLayout();
        
		if (wnd instanceof JFrame)
			initFrame((JFrame) wnd);
		else initApplet((Applet) wnd);
		
		wnd.setVisible(true);	
	}
	
	static public void destroy() {
		Common.Shutdown();
		Save();
	}
}