package service;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import drop_panel.DropPanelsManager;
import hot_keys.HotKeyManager;
import service_threads.DropIniter;
import service_threads.ItemsStateIniter;
import service_threads.ItemsStateRefresher;
import service_threads.LibraryDumper;
import service_threads.Trasher;
import tabber.Tabber;
import torrent_window.TorrentWindow;

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
	static public boolean raw_flag = false;
	static public boolean rand_play_flag = false;
	
	static public Tabber tabber;
	static public NamesLibrary library = new NamesLibrary();
	static public HotKeyManager hotkey_manager = new HotKeyManager();
	static public DropPanelsManager drop_manager;
	
	static public ItemsStateIniter _initer = new ItemsStateIniter();
	static public ItemsStateRefresher _refresher = new ItemsStateRefresher();
	static public DropIniter _drop_initer = new DropIniter();
	static public Trasher _trash = new Trasher();
	static public LibraryDumper _library_dumper = new LibraryDumper();
	
	static public MP3 mp3 = new MP3();
	static public TorrentWindow torrent_window = new TorrentWindow();
	static public NumberFormat formatter = new DecimalFormat("#0.00");
	
	static public void Initialize() {
//		System.setErr(outputFile(args(2));
//		try { System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(Settings.outpath))));	}
//		catch (FileNotFoundException e) { e.printStackTrace();	}
	}
	
	static public void Shutdown() {
		_trash.close();
		hotkey_manager.Shutdown();
		_initer.close();
		_refresher.close();
		_library_dumper.close();
		Errorist.close();
	} 
}
