package components;

import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import service.ActionBind;
import service.Errorist;
import service.Utils;

public class Tray {
	static TrayIcon trayIcon;
	
	static PopupMenu BuildMenu() {
		return Utils.BuildMenu(
			new ActionBind("Toggle window", new ActionListener() {
		      public void actionPerformed(ActionEvent e) { MainWnd.Toggle(); }
		    }),
			new ActionBind("Exit", new ActionListener() {
			  public void actionPerformed(ActionEvent e) { System.exit(0); }
			})		    
		);
	}
	
	static void TrayIconInitialization()
	{
		trayIcon = new TrayIcon(Utils.GetImage("tray.png"), "(O_o)");
		
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseMotionListener(new MouseMotionListener() {
	        public void mouseDragged(MouseEvent e) 	{}
	        public void mouseMoved(MouseEvent e) {
	        	MainWnd.Show();	        	
	        	
//	        	ListItem temp_li = Common.tabber.GetCurrentItem();
//	        	String temp = temp_li == null ? "None" : temp_li.title;
//	        	
//	        	trayIcon.setToolTip(temp);
//	        	trayIcon.displayMessage("Now active...", temp, TrayIcon.MessageType.INFO);	        	
	        }
	    });
		trayIcon.setPopupMenu(BuildMenu());
	}

	public static boolean Add() throws Exception {
		if (SystemTray.isSupported()) {
			try {
				TrayIconInitialization();
				SystemTray.getSystemTray().add(trayIcon);
				return true;
			}
			catch (AWTException e) { Errorist.printLog(e); }
		}
		return false;
	}
	
	public static void Remove()  throws Exception { SystemTray.getSystemTray().remove(trayIcon); }
}