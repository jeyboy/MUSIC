package service;
import drop_panel.DropPanelsManager;
import hot_keys.HotKeyManager;
import service_threads.DropIniter;
import service_threads.ItemsStateIniter;
import service_threads.ItemsStateRefresher;
import service_threads.Trasher;
import tabber.Tabber;

public class Common {
	public static final String[] DEFAULT_GENRES = { "Blues", "Classic Rock",
		"Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz",
		"Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap",
		"Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska",
		"Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient",
		"Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical",
		"Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel",
		"Noise", "AlternRock", "Bass", "Soul", "Punk", "Space",
		"Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic",
		"Gothic", "Darkwave", "Techno-Industrial", "Electronic",
		"Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy",
		"Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle",
		"Native American", "Cabaret", "New Wave", "Psychadelic", "Rave",
		"Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk",
		"Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll",
		"Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing",
		"Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass",
		"Avantgarde", "Gothic Rock", "Progressive Rock",
		"Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band",
		"Chorus", "Easy Listening", "Acoustic", "Humour", "Speech",
		"Chanson", "Opera", "Chamber Music", "Sonata", "Symphony",
		"Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam",
		"Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad",
		"Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo",
		"A capella", "Euro-House", "Dance Hall" };		
	
	static public boolean save_flag = false;
	
	static public Tabber tabber;
	static public NamesLibrary library = new NamesLibrary();
	static public HotKeyManager hotkey_manager = new HotKeyManager();
	static public DropPanelsManager drop_manager;
	
	static public ItemsStateIniter _initer = new ItemsStateIniter();
	static public ItemsStateRefresher _refresher = new ItemsStateRefresher();
	static public DropIniter _drop_initer = new DropIniter();
	static public Trasher _trash = new Trasher();
	
	static public void Shutdown() {
		_trash.close();
		hotkey_manager.Shutdown();
		_initer.close();
		_refresher.close();
		Errorist.close();
	} 
}
