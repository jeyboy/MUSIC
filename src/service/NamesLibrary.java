package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import components.MainWnd;

import filelist.ListItem;

public class NamesLibrary {
	Map<String, LibraryCatalog> library = new HashMap<String, LibraryCatalog>();
	
	public NamesLibrary() {}
	
	char ProceedLetter(char l) {
        if (l >= (int)'a' && l <= (int)'z') return l;
        if (l == (int)'ú' || l == (int)'¸' || l == (int)'ü' || l == (int)'û') return '_';
        if (l >= (int)'à' && l <= (int)'ÿ') return l;
        return '_';
	}
	
	LibraryCatalog GetCatalog(char letter) {
		String name = "" + ProceedLetter(letter);
		LibraryCatalog res = (LibraryCatalog)library.get(name);
		if (res == null)
			res = Load(name);
		return res;
	}
	
	public LibraryCatalog Load(String letter) {
		LibraryCatalog res = null;
		try {
			res = new LibraryCatalog(new HashMap<String, Integer>());
			String strLine;
			BufferedReader reader = IOOperations.GetReader(service.Settings.librarypath + letter);
	  		while ((strLine = reader.readLine()) != null) {
	  			if (strLine.length() == 0) continue;
	  			res.catalog.put(strLine.substring(1), Integer.parseInt(strLine.charAt(0) + ""));
	  		}
	  		reader.close();			
		}
		catch (FileNotFoundException e) {
			res = new LibraryCatalog(new HashMap<String, Integer>());
		}
		catch (Exception e) {
			res = null;
			Errorist.printLog(e); 
		}
		
		if (res != null) 
			library.put(letter, res);
		
		return res;
	}
	
	public int Save() {
		Errorist.printMessage("Library::Save", "Start");
	    PrintWriter pw = null;
	    int counter, total = 0;
	    
	    File f = new File(service.Settings.libraryroot);
	    if (!f.exists())
	    	f.mkdirs();
	    
	    for (Map.Entry<String, LibraryCatalog> catalog : library.entrySet()) {
	    	Errorist.printMessage("Library::Save", catalog.getKey() + " - " + catalog.getValue().updated);
	    	if (catalog.getValue().updated) {
			    try {
			    	counter = 0;
			        pw = IOOperations.GetWriter(service.Settings.librarypath + catalog.getKey(), false);
			        	        
					for (Map.Entry<String, Integer> entry : catalog.getValue().catalog.entrySet()) {
						pw.println(("" + entry.getValue()) + entry.getKey());
						total++;
						if(counter++ >= 100) {
							counter=0;
							pw.flush();
						}
			        }
			        
			        pw.flush();
			        catalog.getValue().updated = false;
			    }
			    catch (Exception e) { Errorist.printLog(e); }
			    finally { if (pw != null) pw.close(); }
	    	}
	    }
	    
	    Errorist.printMessage("Library::Save", "End save");
	    return total;
	}
	
	void Put(String title, Integer down) {
		GetCatalog(title.charAt(0)).put(title, down);
	}
	
	public void Set(String title, Boolean down) {
		Errorist.printMessage("Library::Set", title);
		if (title.length() == 0) return;
		Put(title, down ? 1 : 0);
	}
	
	public Boolean Contains(String title) {
		if (title.length() == 0) return false;
		return GetCatalog(title.charAt(0)).containsKey(title);
	}	
	
	public Boolean Get(String title) {
		return GetCatalog(title.charAt(0)).get(title);
	}
	
//	public int Count () { return library.size();}
	
	public boolean ProceedFile(File file) {
		MediaInfo info = new MediaInfo(file);
		
		for(String title : info.Titles)
			if (Contains(title)) {
				Errorist.printMessage("Library::ProceedFile", file.getAbsolutePath() + " - Find");
				return true;
			}
			else {
				Set(title, false);
				Errorist.printMessage("Library::ProceedFile", file.getAbsolutePath() + " - Not find");
			}
		return false;
	}
	
	public void ProceedItem(ListItem item) {
		item.media_info = new MediaInfo(item.file);
		
//		if (item.state == STATUS.NONE)
			for(String title : item.media_info.Titles)
				if (Contains(title)) {
					if (Get(title)) {
						item.SetStatusLiked();
						Errorist.printMessage("Library::ProceedItem", item.title + " - Liked");
					}
					else {
						item.SetStatusListened();
						Errorist.printMessage("Library::ProceedItem", item.title + " - Listened");
					}
					MainWnd.wnd.repaint();
					break;
				} 
	//			else Set(title, false);
				else {
					Errorist.printMessage("Library::ProceedItem", item.title + " - Not Find");
				}
	}
	
//	public void SetListItemState(ListItem item) {
//    	if (Common.library.Contains(item.title)) {
//    		if (Common.library.Get(item.title))
//    			item.state = ListItem.STATUS.LIKED;
//    		else item.state = ListItem.STATUS.LISTENED;
//    	}
//	}
	
	class LibraryCatalog {
		public boolean updated = false;
		
		Map<String, Integer> catalog;
		
		public LibraryCatalog(Map<String, Integer> cat) {
			catalog = cat;
		}
		
		public void put(String key, Integer val) {
			val = (val == 1 ? val : get(key) ? 1 : val); 
			catalog.put(key, val);
			updated = true;
		}
		
		public Boolean containsKey(String title) {
			if (title.length() == 0) return false;
			return catalog.containsKey(title);
		}
		
		public Boolean get(String title) {
			Object o = catalog.get(title); 
			return o == null ? false : (int)o == 1;
		}		
	}
}
