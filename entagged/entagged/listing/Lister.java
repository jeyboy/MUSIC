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
package entagged.listing;

import entagged.audioformats.AudioFile;

/**
 * Implementors of this interface are meant handle multiple
 * {@link entagged.audioformats.AudioFile}in any matter. <br>
 * Examples: <br>
 * Create a listing of tag information <br>
 * Create a statistic. <br>
 * 
 * @author Christian Laireiter
 */
public interface Lister {

	/**
	 * This method handles the given audio file. <br>
	 * 
	 * @param audioFile
	 *            The audio file which should be regarded.
	 * @param relativePath
	 *            A listing will be done on a specific directory. This field
	 *            shows the relative path from the starting point. <br>
	 * @throws Exception
	 *             If an error occured, which will not be solved on next
	 *             invocation.
	 */
	void addFile(AudioFile audioFile, String relativePath) throws Exception;

	/**
	 * This method adds just the filename to the report, indicating that the tag
	 * could not be read due to errors. <br>
	 * <b>Note: </b> the implementation of this method may result in no
	 * operation. Its implementation specific if no tag information can be
	 * listed.
	 * 
	 * @param fileName
	 *            the name of the file.
	 * @throws Exception
	 *             If an error occured, which will not be solved on next
	 *             invocation.
	 */
	void addFile(String fileName) throws Exception;

	/**
	 * For some imaginable types of listings a close must be performed to write
	 * an end. <br>
	 * Example is a xml listing.
	 */
	void close();

	/**
	 * This method returns the generated listing.
	 * 
	 * @return String representation of the generated listing.
	 */
	String getContent();
}