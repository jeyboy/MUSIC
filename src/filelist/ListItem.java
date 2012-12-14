package filelist;

import java.io.File;
import java.io.IOException;

import service.Common;
import service.Errorist;
import service.IOOperations;
import service.MediaInfo;
import service.ServiceAgent;

public class ListItem {
	final static byte default_status = (byte)128;

	byte status = default_status;
	
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
	
	public static ListItem Load(String info) 	{	return new ListItem(info.substring(2), (byte)info.charAt(1));	}
	public String SaveInfo() 					{	return "f" + ((char)status) + "" + file.getAbsolutePath();	} 
	
	public String title;
	public String ext;
	public File file;
	public MediaInfo media_info = null;

	public ListItem(String path) { this(new File(path)); }
	public ListItem(String path, byte state) { this(new File(path), state); }

	public ListItem(File file) { this(file, default_status); }
	public ListItem(File file, byte state) { this(file, IOOperations.extension(file.getName()), state); }
	public ListItem(File file, String ext) { this(file, ext, default_status); }	
	public ListItem(File file, String ext, byte state) {
		this.file = file;
		this.ext = ext;
		this.title = file.getName();
		this.status = state;
	}	
	
	@Override
	public String toString() { return title; }
	
	public long MemorySize() { return ServiceAgent.getObjectSize(this); }
	
	public void Exec() {
		if (Common.raw_flag) InnerExec();
		else {
	        try {
	        	IOOperations.open(file);
	        	SetStatusListened();
	        }
	        catch (Exception e) { InnerExec(); }
		}
	}
	
	void InnerExec() {
    	if (ext.equals("mp3")) {
    		try {
    			Common.mp3.play(file);
    			SetStatusListened();
    		}
    		
    		catch (Exception e2) { Errorist.printLog(e2); }
    	} 
    	else Common.tabber.MoveSelectAndInit(true); 
	}
	
	public void OpenFolder() {
		try { IOOperations.open(file.getParentFile()); }
		catch (IOException e) { Errorist.printLog(e); }
	}

	public void InitMedia() { Common.library.ProceedItem(this); }
}
