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
package entagged.listing.gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

/**
 * Some functions to exonerate the gui.
 * 
 * @author Christian Laireiter
 */
public class Utils {

    /**
     * This method calls the {@link JFileChooser}and configures it to just
     * select directories. <br>
     * 
     * @param parent
     *                  Optional the component which should be parent of the
     *                  filchooser. Maybe <code>null</code>.
     * @param startDirectory
     *                  The directory the filechooser should start at.
     * 
     * @return File instance of choosen directory. <code>null</code> if
     *              aborted.
     */
    public static File chooseDirectory(Component parent, String startDirectory) {
        File result = null;
        JFileChooser fileChooser = startDirectory == null ? new JFileChooser()
                : new JFileChooser(startDirectory);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        // TODO Language
        fileChooser.setDialogTitle("Choose directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int selection = fileChooser.showOpenDialog(parent);
        if (selection == JFileChooser.APPROVE_OPTION) {
            result = fileChooser.getSelectedFile();
        }
        return result;
    }

    /**
     * This method calls the {@link JFileChooser}configured for saving to a
     * file. <br>
     * 
     * @param parent
     *                  Optional the component which should be parent of the
     *                  filchooser. Maybe <code>null</code>.
     * @param startDirectory
     *                  The directory where the file chooser should start.
     * @return the selected File. <code>null</code> if cancelled.
     */
    public static File chooseTargetFile(Component parent, String startDirectory) {
        File result = null;
        JFileChooser fileChooser = startDirectory == null ? new JFileChooser()
                : new JFileChooser(startDirectory);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        // TODO Language
        fileChooser.setDialogTitle("Choose file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int selection = fileChooser.showSaveDialog(parent);
        if (selection == JFileChooser.APPROVE_OPTION) {
            result = fileChooser.getSelectedFile();
        }
        return result;
    }
}