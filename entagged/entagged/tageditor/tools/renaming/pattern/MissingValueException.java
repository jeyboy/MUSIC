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
package entagged.tageditor.tools.renaming.pattern;

import entagged.audioformats.AudioFile;

/**
 * This exception is thrown if either
 * {@link entagged.tageditor.tools.renaming.pattern.DirectoryPattern}or
 * {@link entagged.tageditor.tools.renaming.pattern.FilePattern}handles an
 * audio file which lacks of a specified tag value. <br>
 * 
 * @author Christian Laireiter (liree)
 */
public class MissingValueException extends Exception {

	/**
	 * The audio file which has missing mandatory values.
	 */
	private AudioFile file;

	/**
	 * The placeholder which couldn't be replaced.<br>
	 * 
	 * @see DirectoryPattern#PLACEHOLDER
	 */
	private String[] invalidTags;

	/**
	 * Creates an instance.
	 * 
	 * @param audioFile
	 *            The audio file which has missing mandatory values.
	 * @param tags
	 *            The placeholder which couldn't be replaced.<br>
	 */
	public MissingValueException(AudioFile audioFile, String[] tags) {
		assert audioFile != null && tags != null && tags.length > 0;
		this.file = audioFile;
		this.invalidTags = tags;
	}

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		StringBuffer msg = new StringBuffer("The file: "
				+ this.file.getAbsolutePath()
				+ " doesn't contain necessary values for: ");
		for (int i = 0; i < invalidTags.length; i++) {
			if (i > 0) {
				msg.append(',');
			}
			msg.append(invalidTags[i]);
		}
		return msg.toString();
	}

	/**
	 * Returns the pattern placeholder where no value could be found for the
	 * current audio file {@link #file}.
	 * 
	 * @return pattern placeholder.
	 */
	public String[] getMissingFields() {
		return this.invalidTags;
	}

}
