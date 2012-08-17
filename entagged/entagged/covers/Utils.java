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

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	private static final Pattern[] stripPatterns = {
			Pattern.compile(" ?\\(dis[ck] \\d+?\\)"),
			Pattern.compile("^(the|a|an|el|le|les|la|los|die|der|das|den) "),
			Pattern.compile(" (the|a|an|el|le|les|la|los|die|der|das|den) "),
			Pattern.compile("('|~|\\!|@|#|\\$|%|\\^|\\*|_|\\[|\\{|\\]|\\}|\\||\\\\|;|:|`|\"|<|,|>|\\.|\\?|/|&)")
			};
	
	public static String normalize(String name) {
		name = name.toLowerCase();

		for (int i = 0; i < stripPatterns.length; i++) {
			Matcher ma = stripPatterns[i].matcher(name);
			name = ma.replaceAll("");
		}

		return name;
	}
	
	public static String getExtension(URL f) {
		String name = f.toExternalForm().toLowerCase();
		int i = name.lastIndexOf( "." );
		if(i == -1)
			return "";
		
		return name.substring( i + 1 );
	}
}
