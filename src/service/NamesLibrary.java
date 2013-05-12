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

import media.MediaInfo;


import components.MainWnd;

import filelist.ListItem;

public class NamesLibrary {
	Map<String, LibraryCatalog> library = new HashMap<String, LibraryCatalog>();
	
	public NamesLibrary() {}
	
	char proceedLetter(char l) {
        if (l >= (int)'a' && l <= (int)'z') return l;
        if (l == (int)'ú' || l == (int)'¸' || l == (int)'ü' || l == (int)'û') return '_';
        if (l >= (int)'à' && l <= (int)'ÿ') return l;
        return '_';
	}
	
	LibraryCatalog GetCatalog(char letter) {
		String name = "" + proceedLetter(letter);
		LibraryCatalog res = (LibraryCatalog)library.get(name);
		if (res == null)
			res = load(name);
		return res;
	}
	
	public LibraryCatalog load(String letter) {
		LibraryCatalog res = null;
		try {
			res = new LibraryCatalog(new HashMap<String, Integer>());
			String strLine;
			BufferedReader reader = IOOperations.getReader(Constants.librarypath + letter);
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
	
	public int save() {
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
			        pw = IOOperations.getWriter(Constants.librarypath + catalog.getKey(), false, false);
			        	        
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
				        pw = IOOperations.getWriter(Constants.librarypath + catalog.getKey(), false, true);
				        	        
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
	
	void put(String title, Integer down) {
		GetCatalog(title.charAt(0)).put(title, down);
	}
	
	public void set(String title, Boolean down) {
		Errorist.printMessage("Library::Set", title);
		if (title.length() == 0) return;
		put(title, down ? 1 : 0);
	}
	
	public Boolean contains(String title) {
		if (title.length() == 0) return false;
		return GetCatalog(title.charAt(0)).containsKey(title);
	}	
	
	public Boolean get(String title) {
		return GetCatalog(title.charAt(0)).get(title);
	}
	
//	public int Count () { return library.size();}
	
	public boolean proceedFile(File file) {
		MediaInfo info = new MediaInfo(file);
		
		for(String title : info.Titles)
			if (contains(title)) {
				Errorist.printMessage("Library::ProceedFile", file.getAbsolutePath() + " - Find");
				return true;
			}
			else {
				set(title, false);
				Errorist.printMessage("Library::ProceedFile", file.getAbsolutePath() + " - Not find");
			}
		return false;
	}
	
	public void ProceedItem(ListItem item) {
		item.media_info = new MediaInfo(item.file());
		
//		if (item.state == STATUS.NONE)
			for(String title : item.media_info.Titles)
				if (contains(title)) {
					if (get(title)) {
						item.setStatusLiked();
						Errorist.printMessage("Library::ProceedItem", item.title + " - Liked");
					}
					else {
						item.setStatusListened();
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
			BufferedReader reader = IOOperations.getReader(path);
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
					set(temp, flag);
					temp = MediaInfo.ForwardNumberFilter(temp);
					set(temp, flag);					
				}
				reader.close();
			}
	  		catch (IOException e) { Errorist.printLog(e); }
		} 
		catch (UnsupportedEncodingException | FileNotFoundException e) { Errorist.printLog(e); }
	}
	public void ParseLibrary(File path) {
		int proceed_count = 0;
		Collection<File> files = IOOperations.scanDirectories(new File [] {path});
		for(File f : files) {
			try {
				BufferedReader reader = IOOperations.getReader(f.getAbsolutePath());
		  		String strLine;
		  		
		  		try {
					while ((strLine = reader.readLine()) != null) {
						if (strLine.length() == 0) continue;
						
						set(strLine.substring(1), strLine.charAt(0) == '1');
						proceed_count++;
					}
					reader.close();
				}
		  		catch (IOException e) { Errorist.printLog(e); }
			} 
			catch (UnsupportedEncodingException | FileNotFoundException e) { Errorist.printLog(e); }
		}
		
		Errorist.printMessage("Library Parse", "Proceed " + proceed_count + " items");
		MainWnd.setTitle("Proceed " + proceed_count + " items");
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

//public static String compress(String str) throws IOException {
//    if (str == null || str.length() == 0) {
//        return str;
//    }
//    System.out.println("String length : " + str.length());
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    GZIPOutputStream gzip = new GZIPOutputStream(out);
//    gzip.write(str.getBytes());
//    gzip.close();
//    String outStr = out.toString("ISO-8859-1");
//    System.out.println("Output String lenght : " + outStr.length());
//    return outStr;
// }
//
//public static String decompress(String str) throws IOException {
//    if (str == null || str.length() == 0) {
//        return str;
//    }
//    System.out.println("Input String length : " + str.length());
//    GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
//    BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
//    String outStr = "";
//    String line;
//    while ((line=bf.readLine())!=null) {
//      outStr += line;
//    }
//    System.out.println("Output String lenght : " + outStr.length());
//    return outStr;
// }