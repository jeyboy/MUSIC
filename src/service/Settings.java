package service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Settings {
	static public String imagepath = "files/images/";
	static public String apppath;
	
	static String Linux() {
		String path = Settings.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		int sep = path.lastIndexOf('/');
		path = path.substring(0, sep);		
		try { path = URLDecoder.decode(path, "UTF-8");}
		catch (UnsupportedEncodingException e1) { e1.printStackTrace();}
		return path;
	}
	
	static String Windows() {
		return System.getProperty("user.dir");
	}
	
	public static void init() {
		apppath = OSInfo.isWindows() ? Windows() : Linux(); 
	}
	
	
	
	static public String libraryroot() { return apppath + File.separator + "library"; }
	static public String librarypath() { return libraryroot() + File.separator + "cat_"; }
	static public String settingspath() { return apppath + File.separator + "settings"; }
	static public String tabspath() { return apppath + File.separator + "tabs"; }
	static public String logpath() { return apppath + File.separator + "logs"; }
	static public String trashpath() { return apppath + File.separator + "trash"; }
	static public String outpath() { return apppath + File.separator + "out"; }
	
	
	static String droppannelspath() { return apppath + File.separator + "drops"; }
	static public String drop_left_path() { return droppannelspath() + "_left"; }
	static public String drop_right_path() { return droppannelspath() + "_right"; }
	static public String drop_top_path() { return droppannelspath() + "_top"; }
	static public String drop_bottom_path() { return droppannelspath() + "_bottom"; }
}