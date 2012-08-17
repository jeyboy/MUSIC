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
package entagged.covers;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class CoverDownloader {
	
	CoverSource[] sources;
	
	public CoverDownloader() {
		this.sources = new CoverSource[] {
				new AmazonCoverSource()
				};
	}
	
	public CoverDownloader(CoverSource[] sources) {
		this.sources = sources;
	}
	
	public List search(String artist, String album) {
		List list = new LinkedList();
		
		for(int i = 0; i<sources.length; i++) {
			try {
				List results = sources[i].search(artist, album);
				list.addAll(results);
			} catch(CoverException e) {
				//Do not add the results of the source since we got an exception
			}
		}
		
		Collections.sort(list);
		
		return list;
	}
	
	public static void main(String[] args) {
		CoverDownloader cd = new CoverDownloader();
		
		List covers = cd.search("yes","yessongs");
		
		System.out.println(covers.size()+" matches");
		
		Iterator it = covers.iterator();
		int i = 0;
		while(it.hasNext()) {
			Cover cover = (Cover) it.next();
			System.out.println(cover);
			try{
				cover.saveAs(new File("/home/kikidonk/test-"+i+".jpg"));
			} catch(CoverException e) {
				System.err.println("Could not save cover: \n"+cover);
			}
			i++;
		}
	}
}
