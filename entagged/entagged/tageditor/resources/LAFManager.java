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

import javax.swing.*;


/**
 *  Manages the Look And Feel using Static Methods $Id: LAFManager.java,v 1.1 2007/03/23 14:17:20 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    v0.05
 */
public class LAFManager {
	public static void setupLookAndFeel( String laf ) {
		try {
			UIManager.setLookAndFeel( laf );
		} catch ( Exception e ) {
			System.out.println( "Error loading the Look&Feel -"+laf+"-: " + e +"\nUsing Metal Java Look And Feel");
			e.printStackTrace();
		}
	}
	
	public static void updateLookAndFeel(JFrame f, String classname) throws Exception{
		UIManager.setLookAndFeel( classname );
		SwingUtilities.updateComponentTreeUI( f);
	}
}

