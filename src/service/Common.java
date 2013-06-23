package service;
import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import components.MainWnd;

import media.MediaPlayer;


import jb_player.JBPlayer;

import drop_panel.DropPanelsManager;
import hot_keys.HotKeyManager;
import service_threads.DropIniter;
import service_threads.ItemsStateIniter;
import service_threads.LibraryDumper;
import service_threads.PlayIniter;
import service_threads.Trasher;
import service_threads.Watcher;
import tabber.Tabber;
import torrent_window.TorrentWindow;

public class Common {
	static public Color color_background = Color.black;
	static public Color color_foreground = Color.white;
	
	static public boolean save_flag = false;
	static public boolean is_loading = false;
	static public boolean raw_flag() {
		try { return drop_manager.player_panel.isVisible(); }
		catch(Exception e) { return false;}
	}
	
	static public Tabber tabber;
	static public NamesLibrary library = new NamesLibrary();
	static public HotKeyManager hotkey_manager = new HotKeyManager();
	static public DropPanelsManager drop_manager;
	
	static public ItemsStateIniter _initer = new ItemsStateIniter();
	static public DropIniter _drop_initer = new DropIniter();
	static public Trasher _trash = new Trasher();
	static public LibraryDumper _library_dumper = new LibraryDumper();
	static public PlayIniter _play_initer = new PlayIniter();
	static public Watcher _watcher = null;
	
	static public MediaPlayer player = new MediaPlayer();
	static public TorrentWindow torrent_window = new TorrentWindow();
	static public NumberFormat formatter = new DecimalFormat("#0.00");
	static public JBPlayer bplayer;
	
	static {
		try { _watcher = new Watcher();	}
		catch (IOException e) {
			MainWnd.setTitle("File system watch are disabled");
			Errorist.printLog(e);
		}
	}
	
	static public void shutdown() {
		_trash.close();
		hotkey_manager.Shutdown();
		_initer.close();
		_library_dumper.close();
		Errorist.close();
		player.exit();
	}
	
	static public void lockWorkThreads() {
		_library_dumper.lock();
		_trash.lock();
		_initer.lock();
	}
	static public void unlockWorkThreads() {
		_library_dumper.unlock();
		_trash.unlock();
		_initer.unlock();
	}
}