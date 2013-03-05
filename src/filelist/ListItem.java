package filelist;

import java.io.File;

import folders.FolderNode;

import media.MediaInfo;


import service.Common;
import service.Errorist;
import service.IOOperations;
import service.Utils;

public class ListItem {
	final static byte default_status = (byte)128;

	byte status = default_status;
	FolderNode node;
	
	public void SetStatusNone() 		{
		SetStatusUnListened();
		SetStatusUnLiked();
	}
	public boolean StatusIsNone()		{	return (status & 1) == 0; }
	
	public void SetStatusListened() 	{
		status |= 1 << 0;
		for(String name : media_info.Titles)
			Common.library.Set(name, false);
	}
	public void SetStatusUnListened() 	{	status &= ~(1 << 0); }
	public boolean StatusIsListened()	{	return (status & 1) == 1; }
	
	public void SetStatusLiked() 		{	
		status |= 1 << 1;
		for(String name : media_info.Titles)
			Common.library.Set(name, true);		
	}
	public void SetStatusUnLiked() 		{	status &= ~(1 << 1); }
	public boolean StatusIsLiked()		{	return (status >> 1 & 1) == 1; }
	
	public void SetStatusPlayed() 		{	status |= 1 << 2; }
	public void SetStatusUnPlayed() 	{	status &= ~(1 << 2); }	
	public boolean StatusIsPlayed()		{	return (status >> 2 & 1) == 1; }	
	
	public static ListItem Load(FolderNode lib_node, String prefix, String info) 	{	return new ListItem(lib_node, Utils.JoinPaths(prefix, info.substring(2)), (byte)info.charAt(1));	}
	public String SaveInfo() 					{	return "f" + ((char)status) + "" + file.getName();	} 
	
	public String title;
	public String ext;
	public File file;
	public MediaInfo media_info = null;

	public ListItem(FolderNode lib_node, String path) { this(lib_node, new File(path)); }
	public ListItem(FolderNode lib_node, String path, byte state) { this(lib_node, new File(path), state); }

	public ListItem(FolderNode lib_node, File file) { this(lib_node, file, default_status); }
	public ListItem(FolderNode lib_node, File file, byte state) { this(lib_node, file, IOOperations.extension(file.getName()), state); }
	public ListItem(FolderNode lib_node, File file, String ext) { this(lib_node, file, ext, default_status); }	
	public ListItem(FolderNode lib_node, File file, String ext, byte state) {
		this.file = file;
		this.ext = ext;
		this.title = file.getName();
		this.status = state;
		this.node = lib_node;
	}	
	
	@Override
	public String toString() { return title; }
	
	public void Exec() {
		if (Common.raw_flag()) InnerExec();
		else {
			if (IOOperations.open(file)) {
				SetStatusListened();
				Common.drop_manager.player_panel.setVisible(false);
			}
			else
				InnerExec(); 
		}
	}
	
	void InnerExec() {
		try {
			Common.player.play(file);
			SetStatusListened();
			Common.drop_manager.player_panel.setVisible(true);
		}
		
		catch (Exception e2) { 
			Errorist.printLog(e2);
			Common.tabber.MoveSelectAndInit(true);
		}
	}
	
	public void OpenFolder() { IOOperations.open(file.getParentFile());	}
	public void InitMedia() { Common.library.ProceedItem(this); }
}