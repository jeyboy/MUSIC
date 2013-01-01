package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
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
			BufferedReader reader = IOOperations.GetReader(Constants.librarypath + letter);
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
	    
	    File f = new File(Constants.libraryroot);
	    if (!f.exists())
	    	f.mkdirs();
	    
	    for (Map.Entry<String, LibraryCatalog> catalog : library.entrySet()) {
	    	if (catalog.getValue().updated) {
			    try {
			    	counter = 0;
			        pw = IOOperations.GetWriter(Constants.librarypath + catalog.getKey(), false, false);
			        	        
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
	    	} else {
		    	if (!catalog.getValue().added.isEmpty()) {
				    try {
				        pw = IOOperations.GetWriter(Constants.librarypath + catalog.getKey(), false, true);
				        	        
						for (Map.Entry<String, Integer> entry : catalog.getValue().added.entrySet()) {
							pw.println(("" + entry.getValue()) + entry.getKey());
							total++;
				        }
						pw.flush();
				        catalog.getValue().added.clear();
				    }
				    catch (Exception e) { Errorist.printLog(e); }
				    finally { if (pw != null) pw.close(); }
		    	}	    		
	    	}
	    }
	    
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
	
	///////////////////////////////////////////////////////////////////////////////////
	
	public void ParseBase(String path) {
		try {
			BufferedReader reader = IOOperations.GetReader(path);
	  		String strLine, ext;
	  		
	  		try {
				while ((strLine = reader.readLine()) != null) {
					if (strLine.length() == 0) continue;
					boolean flag = reader.readLine().charAt(0) == '1';
					
					strLine = strLine.toLowerCase();
					ext = IOOperations.extension(strLine);
					
					if (ext.length() != 0)
						strLine = strLine.substring(0, strLine.length() - (ext.length() + 1));				
					
					String temp = MediaInfo.SitesFilter(strLine);  
					temp = MediaInfo.SpacesFilter(MediaInfo.ForwardNumberPreFilter(temp));
					Set(temp, flag);
					temp = MediaInfo.ForwardNumberFilter(temp);
					Set(temp, flag);					
				}
				reader.close();
			}
	  		catch (IOException e) { Errorist.printLog(e); }
		} 
		catch (UnsupportedEncodingException | FileNotFoundException e) { Errorist.printLog(e); }
	}
	public void ParseLibrary(File path) {
		int proceed_count = 0;
		Collection<File> files = IOOperations.ScanDirectoriesF(new File [] {path});
		for(File f : files) {
			try {
				BufferedReader reader = IOOperations.GetReader(f.getAbsolutePath());
		  		String strLine;
		  		
		  		try {
					while ((strLine = reader.readLine()) != null) {
						if (strLine.length() == 0) continue;
						
						Set(strLine.substring(1), strLine.charAt(0) == '1');
						proceed_count++;
					}
					reader.close();
				}
		  		catch (IOException e) { Errorist.printLog(e); }
			} 
			catch (UnsupportedEncodingException | FileNotFoundException e) { Errorist.printLog(e); }
		}
		
		Errorist.printMessage("Library Parse", "Proceed " + proceed_count + " items");
		MainWnd.SetTitle("Proceed " + proceed_count + " items");
	}	
	
	class LibraryCatalog {
		boolean updated = false;
		Map<String, Integer> added = new HashMap<String, Integer>();
		Map<String, Integer> catalog;
		
		public LibraryCatalog(Map<String, Integer> cat) { catalog = cat; }
		
		public void put(String key, Integer val) {
			if (containsKey(key)) {
				boolean old_val = get(key);
				val = (val == 1 ? val : old_val ? 1 : val); 
				catalog.put(key, val);
				
				if (updated == false)
					updated = old_val != (val == 1);				
			} else {
				added.put(key, val);
				catalog.put(key, val);
			}
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