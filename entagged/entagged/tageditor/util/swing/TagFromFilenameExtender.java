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
import entagged.tageditor.tools.TagFromFilename;

/**
 * This class extends the
 * {@link entagged.tageditor.util.swing.FilenameFromTagExtender}.<br>
 * It adds "&lt;ignore&gt;" and removes "&lt;bitrate&gt;".
 * 
 * @author Christian Laireiter
 */
public class TagFromFilenameExtender extends FilenameFromTagExtender {

    /**
     * @param listenTo
     */
    public TagFromFilenameExtender(JComboBox listenTo) {
        super(listenTo, false);
        initialize();
    }

    /**
     * Adds "ignore" and removes "bitrate"
     */
    private void initialize() {
        this.addCompletionValue(TagFromFilename.IGNORE_MASK, LangageManager
                .getProperty("taghelpfieldextender.ignoredescription"));
    }

}