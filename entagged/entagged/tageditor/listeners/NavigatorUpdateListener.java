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

import java.util.Collection;

/**
 * Implementors will be notified on changes of the current folder watched by the
 * {@link entagged.tageditor.models.Navigator} .<br>
 * 
 * @author Christian Laireiter
 */
public interface NavigatorUpdateListener {

	/**
	 * This method will be called upon each change.<br>
	 * 
	 * @param newFiles
	 *            {@link java.io.File} objects of new files.
	 * @param modifiedFiles
	 *            {@link java.io.File} objects of modified files.
	 * @param removedFiles
	 *            {@link String} Absolute path of previous existing files.
	 */
	public void update(Collection newFiles, Collection modifiedFiles,
			Collection removedFiles);

}
