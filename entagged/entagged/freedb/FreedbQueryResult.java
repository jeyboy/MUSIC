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
package entagged.freedb;

//$Id:
public class FreedbQueryResult {
    
    private String album,artist,id,category;
    private boolean exactMatch;
    
    public FreedbQueryResult(String queryResult) {
        this(queryResult, false);
    }
    public FreedbQueryResult(String freedbQueryResult, boolean exactMatch) {
        this.exactMatch = exactMatch;
        
		String[] fields = freedbQueryResult.split( " ", 3 );

		this.category = fields[0];
		this.id = fields[1];

		String[] infos = fields[2].split(" / ", 2);
		this.artist = infos[0];
		this.album = (infos.length > 1) ? infos[1] : "";
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public String getDiscId() {
        return this.id;
    }
    
    public String getAlbum() {
        return this.album;
    }
    
    public String getArtist() {
        return this.artist;
    }
    
    public boolean isExactMatch() {
        return this.exactMatch;
    }
    
    public String toString() {
		String output = "---Free DB Query Result-----------------------\n";

		output += "Genre: " + getCategory() + "\tCD ID: " + getDiscId() + "\n";
		output += "Album: " + getAlbum() + "\tArtist: " + getArtist();
		output += "\n----------------------------------------";
		return output;
	}
}
