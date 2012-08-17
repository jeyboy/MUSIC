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
import java.awt.event.*;
import java.awt.*;
import entagged.tageditor.listeners.*;
import javax.swing.*;



/**
 *  $Id: WarningDialog.java,v 1.1 2007/03/23 14:17:11 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    22/01/2004
 */
public class WarningDialog extends JDialog {
	
	public WarningDialog( Frame parent, String message ) {
		super( parent,LangageManager.getProperty("common.dialog.warning"), true );
		this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		this.addWindowListener( new DialogWindowListener() );
		this.setSize( 600, 400 );
		this.setLocation( ( PreferencesManager.getInt("entagged.screen.width")/2 - 300 ), ( PreferencesManager.getInt("entagged.screen.height")/2 - 200 ) );
		
		JTextArea messageArea = new JTextArea(message);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		JButton ok = new JButton(LangageManager.getProperty("common.dialog.ok"));
		ok.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				WarningDialog.this.setVisible( false );
			}
		});
		
		this.getContentPane().add(messageArea, "Center");
		this.getContentPane().add(ok, "South");
		this.setVisible(true);
	}
}
