/*
 *  ********************************************************************   **
 *  Copyright notice                                                       **
 *  **																	   **
 *  (c) 2003 Entagged Developpement Team				                   **
 *  http://www.sourceforge.net/projects/entagged                           **
 *  **																	   **
 *  All rights reserved                                                    **
 *  **																	   **
 *  This script is part of the Entagged project. The Entagged 			   **
 *  project is free software; you can redistribute it and/or modify        **
 *  it under the terms of the GNU General Public License as published by   **
 *  the Free Software Foundation; either version 2 of the License, or      **
 *  (at your option) any later version.                                    **
 *  **																	   **
 *  The GNU General Public License can be found at                         **
 *  http://www.gnu.org/copyleft/gpl.html.                                  **
 *  **																	   **
 *  This copyright notice MUST APPEAR in all copies of the file!           **
 *  ********************************************************************
 */
package entagged.tageditor.resources;
import java.util.*;
import java.io.*;

public class UserComboBoxStringsManager {
	private static Hashtable ht = new Hashtable();
	private final static File PREFERENCES_DIR = new File(new File(System.getProperty("user.home"),".entagged"), "comboboxes");
	
	public static void init() {
		if(!PREFERENCES_DIR.exists())
			PREFERENCES_DIR.mkdirs();
	}
	
	public static void exit() {
		Enumeration e = ht.keys();
		while( e.hasMoreElements() )
			saveFile((String)e.nextElement());
		
		System.out.println("Saving user comboboxes");
	}
	
	public static void readFile(String file) {
		String line = null;
		Vector lines = new Vector();
		try{
			BufferedReader br = new BufferedReader(new FileReader(PREFERENCES_DIR+file));
			while((line = br.readLine()) != null)
				if(!line.equals(""))
					lines.add(line);
			br.close();
		}catch(Exception e){
			//e.printStackTrace();
			lines.clear();
			System.out.println("Warning: Could not read from saved combobox entries");
		}
		//---------------------------------
		assert lines != null;
		ht.put(file,lines);
	}
	
	public static Vector getList(String id) {
		return (Vector) ht.get(id);
	}
	
	public static void addToList(String id, String s, boolean first) {
		Vector entry =(Vector) ht.get(id);
		
		s = s.trim();
		if(entry.contains(s))// || entry.isEmpty())
			return;
		
		if( first ) {
			entry.add(0, s);
		}
		else
			entry.add(s);
		
	}
	
	public static void addToList(String id, String s) {
		addToList(id, s, true);
	}
	
	public static void clearList(String id) {
	    Vector entry =(Vector) ht.get(id);
	    if(entry != null)
	        entry.removeAllElements();
	}
	
	private static void saveFile(String file) {
		Vector lines = (Vector) ht.get(file);
		try{
			File pref = new File(PREFERENCES_DIR+file);
			pref.createNewFile();
			PrintWriter pw = new PrintWriter(new FileWriter(pref));
			for(int i = 0; i<lines.size(); i++) {
				//System.out.println("Saving: "+(String)lines.elementAt(i));
				pw.println((String)lines.elementAt(i));
			}
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
			lines.clear();
			System.out.println("Warning: Could not save combobox entries");
		}
	}
}
