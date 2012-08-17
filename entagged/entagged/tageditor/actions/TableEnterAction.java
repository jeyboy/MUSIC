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
package entagged.tageditor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;

import entagged.tageditor.ControlPanel;
import entagged.tageditor.models.Navigator;

/**
 * This action will look at the current Selection of the table and perform
 * following:
 * <ul>
 * <li>If a directory is selected, perform navigator.browseInto()</li>
 * <li>If multiple rows are selected or just one file, the taginfopanel is made
 * visible and its artist field gets the focus. <br>
 * </ul>
 * 
 * @author Christian Laireiter
 */
public class TableEnterAction extends AbstractAction {

    /**
     * Stores the navigator which performs the directory changes.
     */
    private final Navigator control;

    /**
     * The panel for focussing the artist field.
     */
    private ControlPanel controlPanel;

    /**
     * Creates an instance.
     * 
     * @param ctrl
     *                  The navigator which will perform the directory changes.
     * @param ctrlPanel
     *                  The panel which stores the
     *                  {@link entagged.tageditor.TagInfoPanel}and holds the
     *                  selection.
     */
    public TableEnterAction(Navigator ctrl, ControlPanel ctrlPanel) {
        this.control = ctrl;
        this.controlPanel = ctrlPanel;
    }

    /**
     * (overridden)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        List list = this.controlPanel.getAudioFiles().getAudioFiles();
        if (list.size() > 0) {
            File current = (File)list.get(0);
            if (current.isDirectory() && list.size() == 1) {
                control.browseInto(current);
            } else {
                controlPanel.focusArtistOnTagInfoPanel();    
            }
        }
    }

}