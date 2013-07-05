package service;

import java.io.File;

public class Constants {
	static public String imagepath = "files/images/";
	static public String apppath = Utils.ProgramPath();

	static public String logpath = Utils.joinPaths(apppath, "logs");	
	
	static public String default_torrent_path = Utils.joinPaths(apppath, "download");
	
	static public String libraryroot = Utils.joinPaths(apppath, "library");
	static public String librarypath = Utils.joinPaths(libraryroot, "cat_");
	
	static public String filesroot = Utils.joinPaths(apppath, "files");
	
	static public String settingspath = Utils.joinPaths(filesroot, "settings");
	static public String tabspath = Utils.joinPaths(filesroot, "tabs");
	static public String trashpath = Utils.joinPaths(filesroot, "trash");
//	static public String outpath = Utils.joinPaths(filesroot, "out");
	
	static String droppannelspath = Utils.joinPaths(filesroot, "drops");
	static public String drop_left_path = droppannelspath + "_left" ;
	static public String drop_right_path = droppannelspath + "_right";
	static public String drop_top_path = droppannelspath + "_top";
	static public String drop_bottom_path = droppannelspath + "_bottom";
	
	static {
		File temp = new File(filesroot);
		boolean success = temp.mkdirs();
		if (!success && !temp.exists())
			Errorist.printLog("Work folder", "Didnt create work directory in path : '"+filesroot+"'");
		
		temp = new File(libraryroot);
		success = temp.mkdirs();
		if (!success  && !temp.exists())
			Errorist.printLog("Library folder", "Didnt create library directory in path : '"+filesroot+"'");		
	}	
}