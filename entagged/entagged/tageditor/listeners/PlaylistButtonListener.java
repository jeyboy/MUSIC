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
package entagged.tageditor.listeners;

import entagged.tageditor.TagEditorFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


/**
 *  Listener for the Playlist generation button $Id: PlaylistButtonListener.java,v 1.1 2007/03/23 14:16:43 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    v0.07
 */
public class PlaylistButtonListener implements ActionListener {
	/**  Parent Tag Editor */
	private TagEditorFrame tagEditorFrame;


	/**
	 *  Constructor for the PlaylistButtonListener object
	 *
	 * @param  tagEditorFrame  Parent Tag Editor Frame
	 */
	public PlaylistButtonListener( TagEditorFrame tagEditorFrame ) {
		this.tagEditorFrame = tagEditorFrame;
	}


	/**
	 *  Generates the playlists
	 *
	 * @param  e  Description of the Parameter
	 */
	public void actionPerformed( ActionEvent e ) {
		/*
		PlaylistGenerator.generatePLS( tagEditorFrame.getAlbumTableModel().getAlbum(), tagEditorFrame.getAlbumTableModel().getAlbum().getTrack( 0 ).getParentFile().getPath() );
		PlaylistGenerator.generateM3U( tagEditorFrame.getAlbumTableModel().getAlbum(), tagEditorFrame.getAlbumTableModel().getAlbum().getTrack( 0 ).getParentFile().getPath() );
		*/

		//JOptionPane.showMessageDialog( tagEditorFrame, LangageManager.getProperty( "playlistbuttonlistener.playlistgenerated" ) );
		JOptionPane.showMessageDialog( tagEditorFrame, "Not implemented yet" );

	}
}

