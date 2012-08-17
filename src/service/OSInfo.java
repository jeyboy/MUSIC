package service;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Map;
import java.util.Set;

public class OSInfo {
	
    public static Set<Map.Entry<Object, Object>> getCurrentOSInfo() {
    	return System.getProperties().entrySet();
//        for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
//            System.out.println(e);
//        }
    }
    
    private static String OS = null;
    public static String getOsName()
    {
       if(OS == null) { OS = System.getProperty("os.name"); }
       return OS;
    }
    public static boolean isWindows() {
       return getOsName().startsWith("Windows");
    }

    public static boolean isUnix() {
    	return getOsName().startsWith("Unix");
    }
    public static boolean isMac() {
    	return getOsName().startsWith("Mac");
    }
    public static boolean isSolaris() {
    	return getOsName().startsWith("Solaris");
    }
    
    public static Dimension GetScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}