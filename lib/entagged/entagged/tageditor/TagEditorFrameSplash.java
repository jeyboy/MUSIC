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
package entagged.tageditor;

import entagged.tageditor.resources.SplashWindow;
import java.net.URL;

/**
 *  $Id: TagEditorFrameSplash.java,v 1.1 2007/03/23 14:17:10 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK) ; Nicolas Velin
 * @version    4 janvier 2004
 */
public class TagEditorFrameSplash {
	public static void main( String[] args ) {
		URL splashImage = TagEditorFrameSplash.class.getClassLoader().getResource("entagged/tageditor/resources/icons/splash.png");
		SplashWindow splash = new SplashWindow( splashImage );
		splash.setVisible( true );
		splash.setBeginning("Beginning");
		
		miage.sgbd.SqlProvider.createDataBase();

		try {
			Class.forName( "entagged.tageditor.resources.Initialization" ).getMethod( "init", new Class[]{Class.forName("entagged.tageditor.resources.InitializationMonitor")} ).invoke( null, new Object[]{splash} );
			Class.forName( "entagged.tageditor.TagEditorFrame" ).getMethod( "main", new Class[]{String[].class} ).invoke( null, new Object[]{args} );
		} catch ( Throwable e ) {
			System.out.println("Fatal Error occured !");
			e.printStackTrace();
			System.exit( -1 );
		}
		splash.setFinishing("Finished");

		splash.dispose();
	}
}

