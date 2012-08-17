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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;
import entagged.tageditor.tools.renaming.FileRenamer;
import entagged.tageditor.tools.stringtransform.TransformSet;

/**
 * This class will prepare a given directory-pattern-string for use with a huge
 * amount of audiofiles. <br>
 * This will reduce the cost of string replacements and index searches of
 * placeholders. <br>
 * <b>Format: </b> <br>
 * The format is a simple directory specification. Additionally following
 * placeholders can be supplied: <br>
 * <ol>
 * <li>Artist</li>
 * <li>Album</li>
 * <li>Title</li>
 * <li>Track</li>
 * <li>Genre</li>
 * <li>Year</li>
 * <li>Comment</li>
 * <li>Bitrate</li>
 * </ol>
 * 
 * @author Christian Laireiter (liree)
 */
public final class DirectoryPattern {

	/**
	 * Represents a directory section /&lt;section&gt;/.
	 * 
	 * @author Christian Laireiter (liree)
	 */
	private final class Section {

		/**
		 * If <code>true</code>, the section should be replaced by the
		 * directory where the audiofile resides.
		 */
		boolean fileDirectory = false;

		/**
		 * If this is <code>true</code>, either {@link #fileDirectory} is set
		 * or at least one objects in {@link #parts} is of class {@link Integer},
		 * which will be replaced by a tag value.<br>
		 * In other words, if this is <code>true</code>, the current section
		 * object represents a value that will be taken by a tag.
		 */
		final boolean isVariable;

		/**
		 * If not <code>null</code> this field contains either Strings or
		 * Integers. Strings will be taken directly. Integers represent a tag
		 * value of an audiofile. <br>
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
		 * Creates an instance, which represents the directory, where the audio
		 * file resides.
		 */
		Section() {
			fileDirectory = true;
			isVariable = false;
		}

		/**
		 * Creates an instance, which represents a directory name definition.
		 * <br>
		 * A name consists of constant strings and placeholders. This array
		 * should containt {@link String}objects for the constants and
		 * {@link Integer}objects for placeholders (values defined at
		 * {@link #parts}.<br>
		 * 
		 * @param nameDefinition
		 *            The splitted definition of a directory name.
		 */
		Section(Object[] nameDefinition) {
			assert nameDefinition != null && nameDefinition.length > 0;
			boolean isVariablePre = false;
			for (int i = 0; i < nameDefinition.length && !isVariablePre; i++) {
				if (nameDefinition[i] instanceof Integer) {
					isVariablePre = true;
				}
			}
			this.isVariable = isVariablePre;
			this.parts = nameDefinition;
		}

		/**
		 * Creates an instance of a section which will not change. <br>
		 * Audiofiles won't affect the suggested directory.
		 * 
		 * @param directoryName
		 *            Name of the section (directory).
		 */
		Section(String directoryName) {
			this.parts = new Object[] { directoryName };
			this.isVariable = false;
		}

		/**
		 * (overridden)
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			StringBuffer result = new StringBuffer();
			result.append("isFileDirectory: " + this.fileDirectory + "\n");
			result.append("objects:");
			for (int i = 0; parts != null && i < parts.length; i++) {
				if (i > 0) {
					result.append(",");
				}
				result.append("\"" + parts[i].toString() + "\"");
			}
			return result.toString();
		}

	}

	/**
	 * The bitrate placeholder in the given directory pattern will be replaced
	 * by this constant.<br>
	 */
	public final static String INTERNAL_BITRATE_PATTERN = "---bitrate----";

	/**
	 * Pools the patterns and defines their id by position in this array. <br>
	 * Look at {@link Section#parts}for the ids (numbers).
	 */
	public final static String[] PLACEHOLDER = { FileRenamer.ARTIST_MASK,
			FileRenamer.ALBUM_MASK, FileRenamer.TITLE_MASK,
			FileRenamer.TRACK_MASK, FileRenamer.GENRE_MASK,
			FileRenamer.YEAR_MASK, FileRenamer.COMMENT_MASK,
			FileRenamer.BITRATE_MASK };

	/**
	 * If <code>true</code> the pattern contains at least one time the
	 * "&lt;bitrate&gt;" pattern.
	 */
	private boolean containsBitrateDef = false;

	/**
	 * This field stores the {@link Section}objects.
	 */
	private ArrayList sections = new ArrayList();

	/**
	 * Each value of a tag will be transformed using this set. <br>
	 * If <code>null</code>, nothing will be done.
	 */
	private TransformSet transformSet = null;

	/**
	 * Creates an instance.
	 * 
	 * @param pattern
	 *            Pattern. If <code>null</code> or empty the path of the audio
	 *            file will be taken.
	 */
	public DirectoryPattern(String pattern) {
		initialize(pattern);
	}

	/**
	 * This method determines wheter the "&lt;bitrate&gt;" pattern has been
	 * used. <br>
	 * 
	 * @return <code>true</code> if pattern contains
	 *         {@link TagFromFilename#BITRATE_MASK}.<br>
	 */
	public boolean containsBitrate() {
		return this.containsBitrateDef;
	}

	/**
	 * Creates a absolute path for the given audiofile and if specified replaces
	 * the bitrate. <br>
	 * The path will be given by an array where each entry specifies a directory
	 * name, where subsequent entries represent subdirectories. <br>
	 * ["C:\","temp","MyMusic"] will represent the directory tree: <br>
	 * C:\temp\MyMusic
	 * 
	 * @param audioFile
	 *            Audiofile from which the tags will be taken.
	 * @param bitrate
	 *            if not <code>null</code>, the bitrate pattern will be
	 *            inserted.
	 * @return A absolute path definition for the current audio file.
	 * @throws MissingValueException
	 *             If the audiofile has empty values on specified placeholders.
	 */
	public String[] createFrom(AudioFile audioFile, Integer bitrate)
			throws MissingValueException {
		ArrayList result = new ArrayList();
		StringBuffer buffer = new StringBuffer();
		ArrayList missingTags = new ArrayList();
		Tag tag = audioFile.getTag();
		Iterator sectionIt = sections.iterator();
		while (sectionIt.hasNext()) {
			buffer.delete(0, buffer.length());
			Section current = (Section) sectionIt.next();
			if (current.fileDirectory) {
				String absParentPath = audioFile.getParentFile()
						.getAbsolutePath();
				String[] splitted = saveSplit(absParentPath, File.separator);
				for (int i = 0; i < splitted.length; i++) {
					result.add(splitted[i]);
				}
			} else {
				for (int i = 0; i < current.parts.length; i++) {
					if (current.parts[i] instanceof String) {
						buffer.append(current.parts[i].toString());
					} else {
						int index = ((Integer) current.parts[i]).intValue();
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
							if (bitrate != null) {
								value = bitrate.toString();
							} else {
								value = INTERNAL_BITRATE_PATTERN;
							}
							break;
						}
						if (value == null || value.trim().length() == 0) {
							missingTags.add(PLACEHOLDER[index]);
						} else {
							if (transformSet != null && index != 7) {
								value = transformSet.transform(value);
							}
							buffer
									.append(stripReservedDirectoryCharacters(value
											.trim()));
						}
					}
				}
				result.add(buffer.toString());
			}
		}
		if (!missingTags.isEmpty()) {
			throw new MissingValueException(audioFile, (String[]) missingTags
					.toArray(new String[missingTags.size()]));
		}
		return (String[]) result.toArray(new String[result.size()]);
	}

	/**
	 * Returns the number of sections identified by the pattern.<br>
	 * The {@link #createFrom(AudioFile, Integer)} method returns an array of
	 * strings. If you cat those strings by the {@link File#separatorChar}, you
	 * will get a system dependent path specification. Each array element is
	 * constructed by a section element, which was extracted from the given
	 * pattern string.<br>
	 * 
	 * @return The number of sections, of the pattern.
	 */
	public int getSectionCount() {
		return this.sections.size();
	}

	/**
	 * Parses the pattern.
	 * 
	 * @param pattern
	 *            Pattern
	 */
	private void initialize(String pattern) {
		if (pattern == null || pattern.trim().length() == 0) {
			sections.add(new Section());
		} else {
			pattern = pattern.trim().replaceAll("\\\\", "/");
			Pattern p = Pattern.compile("\\A(([a-zA-Z]:)?/)");
			Matcher matcher = p.matcher(pattern);
			// Look if we've got a absolute or a relativ path
			boolean startsWithSysRoot = matcher.lookingAt();
			if (startsWithSysRoot) {
				sections.add(new Section(pattern
						.substring(0, matcher.end() - 1)));
				pattern = pattern.substring(matcher.end());
			} else {
				sections.add(new Section());
			}
			while (pattern.length() > 0) {
				int separator = pattern.indexOf('/');
				String current = null;
				if (separator != -1) {
					current = pattern.substring(0, separator);
				} else {
					// Last section
					current = pattern;
				}
				// Remove current section
				pattern = pattern.substring(current.length());
				if (pattern.startsWith("/")) {
					pattern = pattern.substring(1);
				}
				/*
				 * Now check the section
				 */
				ArrayList sectionElements = new ArrayList();
				while (current.length() > 0) {
					int smallestIndex = Integer.MAX_VALUE;
					int index = -1;
					for (int i = 0; i < PLACEHOLDER.length; i++) {
						int currentIndex = current.indexOf(PLACEHOLDER[i]);
						if (currentIndex != -1 && currentIndex < smallestIndex) {
							smallestIndex = currentIndex;
							index = i;
						}
					}
					if (index != -1) {
						if (smallestIndex > 0) {
							sectionElements.add(current.substring(0,
									smallestIndex));
							current = current.substring(smallestIndex);
						}
						sectionElements.add(new Integer(index));
						if (index == 7) {
							containsBitrateDef = true;
						}
						current = current
								.substring(PLACEHOLDER[index].length());
					} else {
						sectionElements.add(current);
						current = "";
					}
				}
				sections.add(new Section(sectionElements.toArray()));
			}
		}
	}

	/**
	 * This method returns the specific nature of the sepecified section.<br>
	 * If the section at given index is a constant string, this method returns
	 * <code>true</code>. The other case are sections, which are made up by
	 * placeholders. They are variable.<br>
	 * 
	 * @param index
	 *            The index of the section.
	 * @return <code>true</code>, if specified section is a constant string.
	 */
	public boolean isConstantSection(int index) {
		Section current = (Section) this.sections.get(index);
		return current.isVariable && !current.fileDirectory;
	}

	/**
	 * Java-Bug or whatsoever-fix... Replaces
	 * {@link String#split(java.lang.String)}
	 * 
	 * @param string
	 *            The string, that should be splitted.
	 * @param divider
	 *            The string, by which <code>string</code> should be splitted.
	 * @return An array of Strings, which contain the values located between the
	 *         <code>divider</code>.
	 */
	private String[] saveSplit(String string, String divider) {
		ArrayList result = new ArrayList();
		StringBuffer path = new StringBuffer(string);
		int index = path.indexOf(divider);
		while (index > 0) {
			result.add(path.substring(0, index));
			path.delete(0, index + divider.length());
			index = path.indexOf(divider);
		}
		if (path.length() > 0) {
			result.add(path.toString());
		}
		return (String[]) result.toArray(new String[result.size()]);
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
	 * This method will replace all reserved and invalid characters for path
	 * names by "-". <br>
	 * 
	 * @param txt
	 *            A String which should be converted to a valid directory name.
	 * @return A filesystem conforming string, base on <code>txt</code>.
	 */
	private String stripReservedDirectoryCharacters(String txt) {
		if (txt.length() > 2 && txt.substring(0, 3).matches("[a-zA-Z]:\\\\")) {
			if (txt.length() > 3)
				txt = txt.substring(0, 3)
						+ FileRenamer.PATTERN_INVALID_DIRECTORY_CHARS.matcher(
								txt.substring(3)).replaceAll("-");
		} else
			txt = FileRenamer.PATTERN_INVALID_DIRECTORY_CHARS.matcher(txt)
					.replaceAll("-");
		return txt;
	}
}