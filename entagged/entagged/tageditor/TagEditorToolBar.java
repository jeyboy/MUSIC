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
import javax.swing.JButton;
import javax.swing.JToolBar;
import java.awt.*;


/**
 *  $Id: TagEditorToolBar.java,v 1.1 2007/03/23 14:17:10 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    16 d?cembre 2003
 */
public class TagEditorToolBar extends JToolBar {

	public TagEditorToolBar( ) {
		super(JToolBar.HORIZONTAL);

		JButton playlist = new JButton( LangageManager.getProperty( "tageditortoolbar.generateplaylist" ) );
		//playlist.addActionListener( new PlaylistButtonListener( this.tagEditorFrame ) );
		
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets( 0, 0, 0, 0 );
		this.setLayout( gbl );

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbl.setConstraints( playlist, gbc );
		this.add( playlist  );
				
		this.addSeparator();
		
		this.setFloatable( false );
	}
}

