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

import java.util.ArrayList;

import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;
import entagged.tageditor.tools.renaming.FileRenamer;
import entagged.tageditor.tools.stringtransform.TransformSet;

/**
 * Like {@link entagged.tageditor.tools.renaming.pattern.DirectoryPattern}this
 * class is used for compiling filename patterns. <br>
 * The difference to the DirectoryPattern is that this class doesn't allow path
 * separators. <br>
 * 
 * @author Christian Laireiter (liree)
 */
public final class FilePattern {

	/**
	 * If not <code>null</code> this field contains either Strings or
	 * Integers. Strings will be taken directly. Integers represent a tag value
	 * of an audiofile. <br>
	 * Integervalue-&gt;Tag-field: <br>
	 * <li>0: Artist</li>
	 * <li>1: Album</li>
	 * <li>2: Title</li>
	 * <li>3: Track</li>
	 * <li>4: Genre</li>
	 * <li>5: Year</li>
	 * <li>6: Comment</li>
	 * <li>7: Bitrate</li>
	 * <br>
	 */
	Object[] parts = null;

	/**
	 * Each value of a tag will be transformed using this set.<br>
	 * If <code>null</code>, nothing will be done.
	 */
	private TransformSet transformSet = null;

	/**
	 * Creates an instance and prepares the given pattern. <br>
	 * 
	 * @param filePattern
	 *            The filename pattern. If <code>null</code> or empty the name
	 *            of the audiofile is taken without modifications.
	 */
	public FilePattern(String filePattern) {
		initialize(filePattern);
	}

	/**
	 * Returns the new filename which <br>
	 * 
	 * @param audioFile
	 *            The audiofile from which the tags are taken.
	 * @return The new name of the audiofile. <br>
	 * @throws MissingValueException
	 *             If a specified tag value is empty.
	 */
	public String createFrom(AudioFile audioFile) throws MissingValueException {
		if (parts == null) {
			return audioFile.getName();
		}
		StringBuffer result = new StringBuffer();
		ArrayList missingTags = new ArrayList();
		Tag tag = audioFile.getTag();
		for (int i = 0; i < parts.length; i++) {
			if (parts[i] instanceof String) {
				result.append(parts[i].toString());
			} else {
				int index = ((Integer) parts[i]).intValue();
				String value = null;
				switch (index) {
				case 0:
					value = tag.getFirstArtist();
					break;
				case 1:
					value = tag.getFirstAlbum();
					break;
				case 2:
					value = tag.getFirstTitle();
					break;
				case 3:
					value = tag.getFirstTrack();
					break;
				case 4:
					value = tag.getFirstGenre();
					break;
				case 5:
					value = tag.getFirstYear();
					break;
				case 6:
					value = tag.getFirstComment();
					break;
				case 7:
					value = String.valueOf(audioFile.getBitrate());
					break;
				}
				if (value == null || value.trim().length() == 0) {
					missingTags.add(DirectoryPattern.PLACEHOLDER[index]);
				} else {
					if (this.transformSet != null) {
						value = this.transformSet.transform(value);
					}
					result.append(value.trim());
				}
			}
		}

		if (!missingTags.isEmpty()) {
			throw new MissingValueException(audioFile, (String[]) missingTags
					.toArray(new String[missingTags.size()]));
		}
		return stripReservedFileCharacters(result.toString());
	}

	/**
	 * Parses the filepattern.
	 * 
	 * @param filePattern
	 *            The filename pattern. If <code>null</code> or empty the name
	 *            of the audiofile is taken without modifications.
	 */
	private void initialize(String filePattern) {
		if (filePattern != null && filePattern.trim().length() > 0) {
			filePattern = filePattern.trim();
			ArrayList sectionElements = new ArrayList();
			while (filePattern.length() > 0) {
				int smallestIndex = Integer.MAX_VALUE;
				int index = -1;
				for (int i = 0; i < DirectoryPattern.PLACEHOLDER.length; i++) {
					int currentIndex = filePattern
							.indexOf(DirectoryPattern.PLACEHOLDER[i]);
					if (currentIndex != -1 && currentIndex < smallestIndex) {
						smallestIndex = currentIndex;
						index = i;
					}
				}
				if (index != -1) {
					if (smallestIndex > 0) {
						sectionElements.add(filePattern.substring(0,
								smallestIndex));
						filePattern = filePattern.substring(smallestIndex);
					}
					sectionElements.add(new Integer(index));
					filePattern = filePattern
							.substring(DirectoryPattern.PLACEHOLDER[index]
									.length());
				} else {
					sectionElements.add(filePattern);
					filePattern = "";
				}
			}
			parts = sectionElements.toArray();
		}
	}

	/**
	 * @param set
	 *            The transformSet to set. If <code>null</code> nothing will
	 *            happen to the values.
	 */
	public void setTransformSet(TransformSet set) {
		this.transformSet = set;
	}

	/**
	 * This method replaces all invalid characters by "-". <br>
	 * 
	 * @param text
	 *            Text which should be transformed.
	 * @return Transformed text.
	 */
	private String stripReservedFileCharacters(String text) {
		return FileRenamer.PATTERN_INVALID_FILE_CHARS.matcher(text).replaceAll(
				"-");
	}

}
