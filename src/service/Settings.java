package service;

public class Settings {
	static public String apppath = System.getProperty("user.dir");
	final static public String imagepath = "files/images/";
	final static public String libraryroot = apppath + "/library";
	final static public String librarypath = libraryroot + "/cat_";
	final static public String settingspath = apppath + "/settings";
	final static public String tabspath = apppath + "/tabs";
	final static public String logpath = apppath + "/logs";
	
	
	final static String droppannelspath = apppath + "/drops";
	final static public String drop_left_path = droppannelspath + "_left";
	final static public String drop_right_path = droppannelspath + "_right";
	final static public String drop_top_path = droppannelspath + "_top";
	final static public String drop_bottom_path = droppannelspath + "_bottom";
}