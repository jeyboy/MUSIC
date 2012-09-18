package service;

import java.io.File;

public class Settings {
	static public String apppath = System.getProperty("user.dir");
	final static public String imagepath = "files/images/";
	final static public String libraryroot = apppath + File.separator +  "library";
	final static public String librarypath = libraryroot + File.separator + "cat_";
	final static public String settingspath = apppath + File.separator + "settings";
	final static public String tabspath = apppath + File.separator + "tabs";
	final static public String logpath = apppath + File.separator + "logs";
	final static public String trashpath = apppath + File.separator + "trash";
	final static public String outpath = apppath + File.separator + "out";
	
	
	final static String droppannelspath = apppath + File.separator + "drops";
	final static public String drop_left_path = droppannelspath + "_left";
	final static public String drop_right_path = droppannelspath + "_right";
	final static public String drop_top_path = droppannelspath + "_top";
	final static public String drop_bottom_path = droppannelspath + "_bottom";
}