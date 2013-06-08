package filelist;

import java.io.File;
import java.util.Date;

import folders.FolderNode;

import media.MediaInfo;


import service.Common;
import service.Errorist;
import service.IOOperations;
import service.Utils;

public class ListItem {
	public String title;
	public String ext;
	public String path;
	public File media_file = null;
	public MediaInfo _media_info = null;
	public FolderNode node;
	
	final static byte default_status = (byte)128;
	byte status = default_status;
	long last_update = new Date().getTime();
	
	public boolean updateNedded() {
		long curr_time = new Date().getTime();
		boolean res = (curr_time - last_update) > 30000; // half of minute 
		if (res == true)
			last_update = curr_time;
		return res;
	}
	
	public void setStatusNone() 		{
		setStatusUnListened();
		setStatusUnLiked();
	}
	public boolean statusIsNone()		{	return (status & 1) == 0; }
	
	public void setStatusListened() 	{
		status |= 1 << 0;
		for(String name : mediaInfo().Titles)
			Common.library.set(name, false);
	}
	public void setStatusUnListened() 	{	status &= ~(1 << 0); }
	public boolean statusIsListened()	{	return (status & 1) == 1; }
	
	public void setStatusLiked() 		{	
		status |= 1 << 1;
		for(String name : mediaInfo().Titles)
			Common.library.set(name, true);		
	}
	public void setStatusUnLiked() 		{	status &= ~(1 << 1); }
	public boolean statusIsLiked()		{	return (status >> 1 & 1) == 1; }
	
	public void setStatusPlayed() 		{	status |= 1 << 2; }
	public void setStatusUnPlayed() 	{	status &= ~(1 << 2); }	
	public boolean statusIsPlayed()		{	return (status >> 2 & 1) == 1; }	
	
	public static ListItem load(FolderNode lib_node, String prefix, String info) { return new ListItem(lib_node, Utils.joinPaths(prefix, info.substring(2)), (byte)info.charAt(1)); }
	public String saveInfo() { return "f" + ((char)status) + "" + title + "." + ext; } 
	
	public ListItem(FolderNode lib_node, String path) { this(lib_node, path, default_status); }
	public ListItem(FolderNode lib_node, String path, byte state) {
		this.path = path;
		this.title = IOOperations.filename(path);
		this.ext = IOOperations.extension(path);
		this.status = state;
		this.node = lib_node;
	}		
	
	public String toString() { return title; }
	
	public void exec() {
		if (Common.raw_flag()) innerExec();
		else {
			if (IOOperations.open(file())) {
				setStatusListened();
				Common.drop_manager.player_panel.setVisible(false);
			}
			else innerExec(); 
		}
	}
	
	void innerExec() {
		try {
			Common.player.play(file());
			setStatusListened();
			Common.drop_manager.player_panel.setVisible(true);
		}
		
		catch (Exception e2) { 
			Errorist.printLog(e2);
			Common.tabber.moveSelectAndInit(true);
		}
	}
	
	public void openFolder() { IOOperations.open(file().getParentFile());	}
	public void initMedia() { Common.library.ProceedItem(this); }
	public void delete() {
		if (node.tab.options.delete_files)
			Common._trash.AddElem(path, node.tab.options.delete_empty_folders);
		node.list.model.removeElement(this);
		node.freeMemory();
	}
	public FileList getList() { return node.list;}
	
	public File file() {
		if (media_file == null)
			media_file = new File(path);
		return media_file;
	}
	
	public MediaInfo mediaInfo() {
		if (_media_info == null)
			_media_info = new MediaInfo(file());
		return _media_info;
	}
}