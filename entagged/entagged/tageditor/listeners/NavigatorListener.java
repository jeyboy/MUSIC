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

import java.io.File;

/**
 * This interface is meant for recieving changes from
 * {@link entagged.tageditor.models.Navigator}.<br>
 * 
 * @author Christian Laireiter
 */
public interface NavigatorListener {
    /**
     * Tells, that the navigator stepped one directory back in the history.
     */
    int EVENT_BACKWARD = 0x01;

    /**
     * Tells, that the navigator switched to another directory.
     */
    int EVENT_JUMPED = 0x02;

    /**
     * Tells, that the navigator switched to the parent directory.
     */
    int EVENT_PARENT = 0x04;

    /**
     * This type of event is just for notification on all listeners, that a
     * reload should occur.
     */
    int EVENT_RELOAD = 0x08;

    /**
     * This method will be called if the directory has changed.
     * 
     * @param newDirectory
     *                  The new directory.
     * @param contents
     *                  The contents of the directory filtered by
     *                  {@link entagged.audioformats.AudioFileFilter}.
     * @param how
     *                  The type of the change. <br>
     * @see #EVENT_BACKWARD <br>
     * @see #EVENT_JUMPED
     */
    void directoryChanged(File newDirectory, File[] contents, int how);
}