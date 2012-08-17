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

import java.awt.*;
import entagged.tageditor.listeners.*;
import entagged.tageditor.resources.*;
import javax.swing.*;
import java.net.*;

public class AboutDialog extends JDialog {
	
	public AboutDialog( Frame parent ) {
		super( parent, "About", true );
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addWindowListener(new DialogWindowListener());
		this.setSize(500, 500);
		this.setLocationRelativeTo(this.getParent());
		
		JEditorPane jep;
		try{
			URL aboutURL = ResourcesRepository.getAboutFile();
			jep = new JEditorPane(aboutURL);
		}catch(Exception e) {
			e.printStackTrace();
			jep = new JEditorPane();
			jep.setText("Error while loading the about html page..please visit the webpage at http://entagged.sourceforge.net/ instead !");
		}
		jep.setEditable(false);
		
		JScrollPane jsp = new JScrollPane(jep);
		
		this.getContentPane().add(jsp,"Center");
		this.setVisible(true);
	}
}
