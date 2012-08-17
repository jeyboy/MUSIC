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
package entagged.tageditor.util.swing;

import javax.swing.JComboBox;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.renaming.FileRenamer;

/**
 * This class easily extends
 * {@link entagged.tageditor.util.swing.QuickHelpComboBoxExtender}and registers
 * the completion values for use with the {@link entagged.tageditor.RenamePanel}
 * s pattern boxes.
 * 
 * @author Christian Laireiter
 */
public class FilenameFromTagExtender extends QuickHelpComboBoxExtender {

    /**
     * Creates an instance, that will work with the given combobox.
     * 
     * @param listenTo
     *                  The combobox which will be used .
     * @param withBitrate
     *                  if <code>true</code>, the "&lt;bitrate&gt;" will be used,
     *                  too.
     */
    public FilenameFromTagExtender(JComboBox listenTo, boolean withBitrate) {
        super(listenTo);
        initialize(withBitrate);
    }

    /**
     * Initializes this implementation.
     * 
     * @param withBitrate
     *                  if <code>true</code>, the "&lt;bitrate&gt;" will be used,
     *                  too.
     */
    private void initialize(boolean withBitrate) {
        this.addCompletionValue(FileRenamer.ALBUM_MASK, LangageManager
                .getProperty("taghelpfieldextender.valuedescription")
                + " \""
                + LangageManager.getProperty("common.tag.album")
                + "\",");
        this.addCompletionValue(FileRenamer.ARTIST_MASK, LangageManager
                .getProperty("taghelpfieldextender.valuedescription")
                + " \""
                + LangageManager.getProperty("common.tag.artist")
                + "\".");
        if (withBitrate) {
            this.addCompletionValue(FileRenamer.BITRATE_MASK, LangageManager
                    .getProperty("taghelpfieldextender.valuedescription")
                    + " \""
                    + LangageManager.getProperty("common.encoding.bitrate")
                    + "\".");
        }
        this.addCompletionValue(FileRenamer.COMMENT_MASK, LangageManager
                .getProperty("taghelpfieldextender.valuedescription")
                + " \""
                + LangageManager.getProperty("common.tag.comment")
                + "\".");
        this.addCompletionValue(FileRenamer.GENRE_MASK, LangageManager
                .getProperty("taghelpfieldextender.valuedescription")
                + " \""
                + LangageManager.getProperty("common.tag.genre")
                + "\".");
        this.addCompletionValue(FileRenamer.TITLE_MASK, LangageManager
                .getProperty("taghelpfieldextender.valuedescription")
                + " \""
                + LangageManager.getProperty("common.tag.title")
                + "\".");
        this.addCompletionValue(FileRenamer.TRACK_MASK, LangageManager
                .getProperty("taghelpfieldextender.valuedescription")
                + " \""
                + LangageManager.getProperty("common.tag.track")
                + "\".");
        this
                .addCompletionValue(FileRenamer.YEAR_MASK, LangageManager
                        .getProperty("taghelpfieldextender.valuedescription")
                        + " \""
                        + LangageManager.getProperty("common.tag.year")
                        + "\".");
    }

}