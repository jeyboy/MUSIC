package filelist;

import java.io.File;
import java.io.IOException;

import service.Common;
import service.Errorist;
import service.IOOperations;
import service.MediaInfo;
import service.ServiceAgent;

public class ListItem {
	byte status = (byte)128;
	
	public void SetStatusNone() 		{	status |= 0 << 0;	}
	public boolean StatusIsNone()		{	return (status & 1) == 0; }
	
	public void SetStatusListened() 	{	status |= 1 << 0;	}		
	public boolean StatusIsListened()	{	return (status & 1) == 1; }
	
	public void SetStatusLiked() 		{	status |= 1 << 1;	}
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

	public ListItem(File file) { this(file, (byte)128); }
	public ListItem(File file, byte state) { this(file, IOOperations.extension(file.getName()), state); }
	public ListItem(File file, String ext) { this(file, ext, (byte)128); }	
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
        try { 
        	IOOperations.open(file);
        	SetStatusListened();
        }
        catch (IOException e) { Errorist.printLog(e); }
        catch (UnsupportedOperationException e) 
        {
        	Errorist.printLog(e);
      	   //alt try open the file
        }
	}
	
	public void OpenFolder() {
		try { IOOperations.open(file.getParentFile()); }
		catch (IOException e) { Errorist.printLog(e); }
	}
	
//	public void SetState(STATUS newstate) {
//    	state = newstate;
//    	for(String tstr : media_info.Titles)
//    		Common.library.Set(tstr, newstate == STATUS.LIKED);		
//	} 

	public void InitMedia() { Common.library.ProceedItem(this); }
}
