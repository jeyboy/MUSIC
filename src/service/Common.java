package service;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import Media.MediaPlayer;

import jb_player.JBPlayer;

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
	static public Color color_background = Color.black;
	static public Color color_foreground = Color.white;
	
	static public boolean save_flag = false;
	static public boolean raw_flag() {
		try { return drop_manager.player_panel.isVisible(); }
		catch(Exception e) { return false;}
	}
	
	static public Tabber tabber;
	static public NamesLibrary library = new NamesLibrary();
	static public HotKeyManager hotkey_manager = new HotKeyManager();
	static public DropPanelsManager drop_manager;
	
	static public ItemsStateIniter _initer = new ItemsStateIniter();
	static public ItemsStateRefresher _refresher = new ItemsStateRefresher();
	static public DropIniter _drop_initer = new DropIniter();
	static public Trasher _trash = new Trasher();
	static public LibraryDumper _library_dumper = new LibraryDumper();
	
	static public MediaPlayer player = new MediaPlayer();
	static public TorrentWindow torrent_window = new TorrentWindow();
	static public NumberFormat formatter = new DecimalFormat("#0.00");
	static public JBPlayer bplayer;
	
	static public void Shutdown() {
		_trash.close();
		hotkey_manager.Shutdown();
		_initer.close();
		_refresher.close();
		_library_dumper.close();
		Errorist.close();
		player.exit();
	} 
}
