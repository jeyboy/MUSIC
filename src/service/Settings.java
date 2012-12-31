package service;

import java.io.File;

public class Settings {
	static public String imagepath = "files/images/";
	static public String apppath;
	
	static String Unux() {
		String path = Settings.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		return IOOperations.path(path, '/');
	}
	static String Windows() { return System.getProperty("user.dir"); }
	
	public static void init() {
		apppath = OSInfo.isWindows() ? Windows() : Unux();
		
		default_torrent_path = apppath + File.separator + "download" + File.separator;
		
		libraryroot = apppath + File.separator + "library";
		librarypath = libraryroot + File.separator + "cat_";
		settingspath = apppath + File.separator + "settings";
		tabspath = apppath + File.separator + "tabs";
		logpath = apppath + File.separator + "logs";
		trashpath = apppath + File.separator + "trash";
		outpath = apppath + File.separator + "out";
		
		
		droppannelspath = apppath + File.separator + "drops";
		drop_left_path = droppannelspath + "_left";
		drop_right_path = droppannelspath + "_right";
		drop_top_path = droppannelspath + "_top";
		drop_bottom_path = droppannelspath + "_bottom";		
	}
	
	static public String default_torrent_path;
	static public String libraryroot;
	static public String librarypath;
	static public String settingspath;
	static public String tabspath;
	static public String logpath;
	static public String trashpath;
	static public String outpath;
	
	
	static String droppannelspath;
	static public String drop_left_path;
	static public String drop_right_path;
	static public String drop_top_path;
	static public String drop_bottom_path;
}