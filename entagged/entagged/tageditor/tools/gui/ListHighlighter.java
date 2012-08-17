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
package entagged.tageditor.tools.gui;

import java.awt.Color;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import entagged.tageditor.tools.renaming.FileRenamer;

/**
 * This class will be created with a list of strings which should be
 * highlighted. <br>
 * Then this class maybe installed on a document and will highlight the words of
 * the List. <br>
 * 
 * @author Christian Laireiter
 */
public class ListHighlighter extends DefaultHighlighter implements
		DocumentListener {

	/**
	 * This method will create a highlighter for the rename from tag panel.
	 * 
	 * @return Configured highlighter.
	 */
	public static ListHighlighter createRenameFromTagHighlighter() {
		String[] list = { FileRenamer.ALBUM_MASK, FileRenamer.ARTIST_MASK,
				FileRenamer.BITRATE_MASK, FileRenamer.COMMENT_MASK,
				FileRenamer.GENRE_MASK, FileRenamer.TITLE_MASK,
				FileRenamer.YEAR_MASK, FileRenamer.TRACK_MASK };
		return new ListHighlighter(list);
	}

	/**
	 * This method will create a highlighter for the tag from filename panel.
	 * 
	 * @return Configured highlighter.
	 */
	public static ListHighlighter createTagFromFilenameHighlighter() {
		String[] list = { FileRenamer.ALBUM_MASK, FileRenamer.ARTIST_MASK,
				FileRenamer.COMMENT_MASK,
				entagged.tageditor.tools.TagFromFilename.IGNORE_MASK,
				FileRenamer.GENRE_MASK, FileRenamer.TITLE_MASK,
				FileRenamer.YEAR_MASK, FileRenamer.TRACK_MASK };
		return new ListHighlighter(list);
	}

	/**
	 * Stores the textcomponent, this highlighter is assigned to.
	 */
	protected JTextComponent component;

	/**
	 * The list of words that should be highlighted if found.
	 */
	private String[] highlights;

	/**
	 * This one is used for painting highlights.
	 */
	private DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlightPainter(
			Color.LIGHT_GRAY) {
		
		/**
		 * Returns {@link Color#GREEN}, if the installed compontent as the
		 * focus. else {@link Color#LIGHT_GRAY}.
		 * 
		 * @see DefaultHighlightPainter#getColor()
		 */
		public Color getColor() {
			if (component != null) {
				if (component.hasFocus()) {
					return Color.GREEN;
				}
			}			
			return Color.LIGHT_GRAY;
		}

	};

	/**
	 * Creates an instance.
	 * 
	 * @param list
	 *            List of words which should be highlighted.
	 */
	public ListHighlighter(String[] list) {
		this.highlights = list;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent e) {
		// Not of interest.
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.text.DefaultHighlighter#deinstall(javax.swing.text.JTextComponent)
	 */
	public void deinstall(JTextComponent c) {
		super.deinstall(c);
		c.getDocument().removeDocumentListener(this);
		this.component = null;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent e) {
		process(e);
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.text.DefaultHighlighter#install(javax.swing.text.JTextComponent)
	 */
	public void install(JTextComponent c) {
		super.install(c);
		c.getDocument().addDocumentListener(this);
		this.component = c;
	}

	/**
	 * This method will look at the document and change the highlighting
	 * 
	 * @param e
	 *            The Event on which this class is working.
	 */
	private void process(DocumentEvent e) {
		this.removeAllHighlights();
		Document document = e.getDocument();
		try {
			String text = document.getText(0, document.getLength());
			for (int i = 0; i < highlights.length; i++) {
				String current = highlights[i];
				int index = text.indexOf(current);
				while (index >= 0) {
					this.addHighlight(index, index + current.length(), painter);
					index = text.indexOf(current, index + 1);
				}
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent e) {
		process(e);
	}

}