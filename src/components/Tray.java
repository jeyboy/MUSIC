package components;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.swing.JOptionPane;

import service.Errorist;

public class Tray {
	static Image image; 
	static TrayIcon trayIcon;
	
	static ActionListener actions =
		new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("In here");
				trayIcon.displayMessage("Tester!", "Some action performed", TrayIcon.MessageType.INFO);
			}
		};
	static MouseListener mouse_listens = 
		new MouseListener() {
	        public void mouseClicked(MouseEvent e) {
	          System.out.println("Tray icon: Mouse clicked");
	        }
	        public void mouseEntered(MouseEvent e) {
	          System.out.println("Tray icon: Mouse entered");
	        }
	        public void mouseExited(MouseEvent e) {
	          System.out.println("Tray icon: Mouse exited");
	        }
	        public void mousePressed(MouseEvent e)  {}
	        public void mouseReleased(MouseEvent e) {}
	   };
	static MouseMotionListener mouse_motion_listens = 	      
	    new MouseMotionListener() {
	        public void mouseDragged(MouseEvent e) 	{}
	        public void mouseMoved(MouseEvent e) 	{}
	    };
	
	static PopupMenu BuildMenu() {
		PopupMenu popup = new PopupMenu();
		MenuItem miExit = new MenuItem("Exit");
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Goodbye");
				System.exit(0);
		    }
		};
		miExit.addActionListener(al);
		popup.add(miExit);
	    MenuItem messageItem = new MenuItem("Show window");
	    messageItem.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        JOptionPane.showMessageDialog(null, AudioSystem.getAudioFileTypes());
//	    	MainWnd.wnd.setVisible(true);
	      }
	    });
	    popup.add(messageItem);
	    return popup;
	}   
	static boolean TrayIconInitialization()
	{
		if (SystemTray.isSupported()) {
			try { image = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(service.Settings.imagepath + "tray.png")); }
			catch (IOException e) { Errorist.printLog(e); }
			trayIcon = new TrayIcon(image, "OLOLO");
			
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(actions);
			trayIcon.addMouseListener(mouse_listens);
			trayIcon.addMouseMotionListener(mouse_motion_listens);
			trayIcon.setPopupMenu(BuildMenu());
		}
		return SystemTray.isSupported(); 
	}

	public static boolean Add() throws Exception {
		if (TrayIconInitialization()) {
			try { SystemTray.getSystemTray().add(trayIcon);	}
			catch (AWTException e) { Errorist.printLog(e);
				return false;
			}
			return true;
		}
		else return false;
	}
	
	public static void Remove()  throws Exception {
		SystemTray.getSystemTray().remove(trayIcon);
	}
}
