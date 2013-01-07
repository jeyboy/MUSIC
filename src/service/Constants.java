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
	
//	public static final String[] DEFAULT_GENRES = { "Blues", "Classic Rock",
//		"Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz",
//		"Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap",
//		"Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska",
//		"Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient",
//		"Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical",
//		"Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel",
//		"Noise", "AlternRock", "Bass", "Soul", "Punk", "Space",
//		"Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic",
//		"Gothic", "Darkwave", "Techno-Industrial", "Electronic",
//		"Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy",
//		"Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle",
//		"Native American", "Cabaret", "New Wave", "Psychadelic", "Rave",
//		"Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk",
//		"Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll",
//		"Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing",
//		"Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass",
//		"Avantgarde", "Gothic Rock", "Progressive Rock",
//		"Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band",
//		"Chorus", "Easy Listening", "Acoustic", "Humour", "Speech",
//		"Chanson", "Opera", "Chamber Music", "Sonata", "Symphony",
//		"Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam",
//		"Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad",
//		"Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo",
//		"A capella", "Euro-House", "Dance Hall" };	
}