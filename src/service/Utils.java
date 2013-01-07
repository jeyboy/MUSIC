package service;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Utils {
	
	/////////////////// OS funcs //////////////////////
	
    public static Set<Map.Entry<Object, Object>> getCurrentOSInfo() {
    	return System.getProperties().entrySet();
//        for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
//            System.out.println(e);
//        }
    }
    
    public static String getOsName() { return System.getProperty("os.name"); }
    public static boolean isWindows() { return getOsName().startsWith("Windows"); }
    public static boolean isUnix() { return getOsName().startsWith("Unix"); }
    public static boolean isMac() { return getOsName().startsWith("Mac"); }
    public static boolean isSolaris() { return getOsName().startsWith("Solaris") || getOsName().startsWith("SunOS"); }
    public static boolean isFreeBSD() { return getOsName().startsWith("FreeBSD"); }
    
	static String UnuxProgramPath() {
		String path = Constants.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		return IOOperations.path(path, '/');
	}
	static String WindowsProgramPath() { return System.getProperty("user.dir"); }
	static public String ProgramPath() { return isWindows() ? WindowsProgramPath() : UnuxProgramPath(); }
    
    //////////////////////////////////////////////////
    
    public static Dimension GetScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
    
    //////////////////////////////////////////////////
    
    public static PopupMenu BuildMenu(ActionBind ... items) {
		PopupMenu popup = new PopupMenu();
		
		for(ActionBind act : items) {
		    MenuItem messageItem = new MenuItem(act.name);
		    messageItem.addActionListener(act.action);
		    popup.add(messageItem);		
		}
		
	    return popup;   	
    }
    
	public static int showDialog(Component parent, String title, Object ... elems) {
		return JOptionPane.showOptionDialog(  
				parent, elems,  
				title, JOptionPane.OK_CANCEL_OPTION,  
				JOptionPane.PLAIN_MESSAGE, null, null, null 
        );		
	}    
    
    //////////////////////////////////////////////////    
    
    public static String JoinPaths(String ... parts) {
    	if (parts.length == 0) return "";
    	String res = parts[0];
    	for(int loop1 = 1 ; loop1 < parts.length; loop1++)
    		res += File.separator + parts[loop1]; 
    	return res;
    }
    
    //////////////////////////////////////////////////

    public static BufferedImage GetImage(String path) {
		try { return ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(Constants.imagepath + path)); } 
		catch (IOException e1) { Errorist.printLog(e1); }
		return null;     	
    }    
    
    public static ImageIcon GetIcon(String path) { return new ImageIcon(GetImage(path)); }
    
    ////////////////////////////////////////////////
    
    public static String MilliToTime(long millis) {
    	long h = (millis / 3600000000l) % 24;
    	
    	return h > 0 ?
    			String.format("%02d:%02d:%02d", 
    			h,
    			((millis / (60000000)) % 60),
    			(millis / 1000000) % 60)
    			:
    			String.format("%02d:%02d", 
    			((millis / (60000000)) % 60),
    			(millis / 1000000) % 60);
    }
}