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

import entagged.tageditor.resources.*;
import java.awt.*;
import entagged.tageditor.listeners.*;
import javax.swing.*;



public class WaitDialog extends JDialog {
	
	public WaitDialog( Frame parent ) {
		super( parent, "Please Wait...", false );
		this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		this.addWindowListener( new DialogWindowListener() );
		this.setSize( 400, 100 );
		
		this.setLocation( ( PreferencesManager.getInt("entagged.screen.width")/2 - 200 ), ( PreferencesManager.getInt("entagged.screen.height")/2 - 50 ) );
		
		JLabel l = new JLabel("This can take several seconds, especially when writing new tags on non-padded files...");
		
		JProgressBar jpb = new JProgressBar();
		jpb.setIndeterminate(true);
		jpb.setString("Processing File");
        jpb.setStringPainted(true); 
		
		this.getContentPane().add(l, "North");
		this.getContentPane().add(jpb, "Center");
		this.setVisible(true);
	}
}
