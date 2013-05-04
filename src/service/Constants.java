package service;

public class Constants {
	static public String imagepath = "files/images/";
	static public String apppath = Utils.ProgramPath();

	static public String default_torrent_path = Utils.joinPaths(apppath, "download");
	static public String libraryroot = Utils.joinPaths(apppath, "library");
	static public String librarypath = Utils.joinPaths(libraryroot, "cat_");
	static public String settingspath = Utils.joinPaths(apppath, "settings");
	static public String tabspath = Utils.joinPaths(apppath, "tabs");
	static public String logpath = Utils.joinPaths(apppath, "logs");
	static public String trashpath = Utils.joinPaths(apppath, "trash");
	static public String outpath = Utils.joinPaths(apppath, "out");
	
	static String droppannelspath = Utils.joinPaths(apppath, "drops");
	static public String drop_left_path = droppannelspath + "_left" ;
	static public String drop_right_path = droppannelspath + "_right";
	static public String drop_top_path = droppannelspath + "_top";
	static public String drop_bottom_path = droppannelspath + "_bottom";
}