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
import java.awt.Frame;
import entagged.tageditor.listeners.*;
import javax.swing.*;
import java.util.*;
import java.io.*;


public class FileWarningDialog extends JDialog {
	
	public FileWarningDialog( Frame parent, List files ) {
		super( parent, LangageManager.getProperty("common.dialog.warning"), true );
		this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		this.addWindowListener( new DialogWindowListener() );
		this.setSize( 600, 400 );
		this.setLocation( ( PreferencesManager.getInt("entagged.screen.width")/2 - 300 ), ( PreferencesManager.getInt("entagged.screen.height")/2 - 200 ) );
		
		StringBuffer sb = new StringBuffer();
		sb.append(LangageManager.getProperty("tageditorframe.errors")+"\n");
		
		Iterator it = files.iterator();
		while(it.hasNext())
			sb.append(((File)it.next()).getAbsolutePath()).append("\t"+((Exception)it.next()).getMessage()+"\n");
		
		JTextArea messageArea = new JTextArea(sb.toString());
		messageArea.setLineWrap(false);
		messageArea.setEnabled(false);
		
		JButton ok = new JButton(LangageManager.getProperty("common.dialog.ok"));
		ok.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				FileWarningDialog.this.setVisible( false );
			}
		});
		
		JScrollPane jsp = new JScrollPane(messageArea);
		this.getContentPane().add(jsp, "Center");
		this.getContentPane().add(ok, "South");
		this.setVisible(true);
	}
}
