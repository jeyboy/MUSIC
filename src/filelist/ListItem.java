package filelist;

import java.io.File;
import java.io.IOException;

import service.Common;
import service.Errorist;
import service.IOOperations;
import service.MediaInfo;
import service.ServiceAgent;

public class ListItem {
	public enum STATUS { LISTENED, LIKED, PLAYED, NONE }
	
	public int status_to_str() {
		switch(state) {
			case LISTENED 	: return 0 ;
			case LIKED 	 	: return 1 ;
			case NONE 	 	: return 2 ;
//			case PLAYED 	: return 3 ;
			default			: return 2 ;
		}
	}
	
	static STATUS str_to_status(char r) {
		switch(r) {
			case '0' 		: return ListItem.STATUS.LISTENED ;
			case '1' 	 	: return ListItem.STATUS.LIKED ;
			case '2' 	 	: return ListItem.STATUS.NONE ;
	//		case '3' 		: return ListItem.STATUS.PLAYED ;
			default			: return ListItem.STATUS.NONE ;
		}			
	}
	
	public static ListItem Load(String info) {
		return new ListItem(info.substring(2), str_to_status(info.charAt(1)));
	}
	public String SaveInfo() {
		return "f" + status_to_str() + file.getAbsolutePath();
	} 
	
	public String title;
	public String ext;
	public File file;
	public STATUS state = STATUS.NONE;
	public MediaInfo media_info = null;

	public ListItem(String path) { this(new File(path)); }
	public ListItem(String path, STATUS state) { this(new File(path), state); }

	public ListItem(File file) { this(file, STATUS.NONE); }
	public ListItem(File file, STATUS state) { this(file, IOOperations.extension(file.getName()), state); }
	public ListItem(File file, String ext) { this(file, ext, STATUS.NONE); }	
	public ListItem(File file, String ext, STATUS state) {
		this.file = file;
		this.ext = ext;
		this.title = file.getName();
		this.state = state;
	}	
	
	@Override
	public String toString() { return title; }
	
	public long MemorySize() { return ServiceAgent.getObjectSize(this); }
	
	public void Exec() {
        try { 
        	IOOperations.open(file);
        	SetState(STATUS.LISTENED);
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
	
	public void SetState(STATUS newstate) {
    	state = newstate;
    	for(String tstr : media_info.Titles)
    		Common.library.Set(tstr, newstate == STATUS.LIKED);		
	} 

	public void InitMedia() { Common.library.ProceedItem(this); }
}
