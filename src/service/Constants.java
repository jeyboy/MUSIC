package service;

public class Constants {
	static public String imagepath = "files/images/";
	static public String apppath = Utils.ProgramPath();

	static public String default_torrent_path = Utils.JoinPaths(apppath, "download");
	static public String libraryroot = Utils.JoinPaths(apppath, "library");
	static public String librarypath = Utils.JoinPaths(libraryroot, "cat_");
	static public String settingspath = Utils.JoinPaths(apppath, "settings");
	static public String tabspath = Utils.JoinPaths(apppath, "tabs");
	static public String logpath = Utils.JoinPaths(apppath, "logs");
	static public String trashpath = Utils.JoinPaths(apppath, "trash");
	static public String outpath = Utils.JoinPaths(apppath, "out");
	
	static String droppannelspath = Utils.JoinPaths(apppath, "drops");
	static public String drop_left_path = droppannelspath + "_left" ;
	static public String drop_right_path = droppannelspath + "_right";
	static public String drop_top_path = droppannelspath + "_top";
	static public String drop_bottom_path = droppannelspath + "_bottom";
}