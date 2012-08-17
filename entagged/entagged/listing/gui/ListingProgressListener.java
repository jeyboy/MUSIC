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

/**
 * Listener for recieving the progress of
 * {@link entagged.listing.ListingProcessor}
 * 
 * @author Christian Laireiter
 */
public interface ListingProgressListener {

    /**
     * This method returns <code>true</code> if the processing should be
     * aborted. <br>
     * It's not a real listener method, but a quick way for finishing the
     * prototype
     * 
     * @return <code>true</code> if listing should be aborted.
     */
    boolean abort();
    /**
     * Will be called if another directory is processed.
     * 
     * @param fileCount
     *                  Number of files and directories in it.
     * @param name
     *                  Name of the directory.
     */
    void directoryChanged(int fileCount, String name);

    /**
     * Will be called if an error occured.
     * 
     * @param errorDescription
     *                  description for the error.
     */
    void errorOccured(String errorDescription);

    /**
     * Will be called if another file is processed.
     * 
     * @param fileNum
     *                  the number of the current file.
     * @param name
     *                  name of the file.
     */
    void fileChanged(int fileNum, String name);

    /**
     * Will be called if the listing has been finished.
     *  
     */
    void processingFinished();
}