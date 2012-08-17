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

package entagged.tageditor.tools.renaming.data;

import java.util.HashMap;

/**
 * This class represents a file, which was processed by the renaming.<br>
 * 
 * @author Christian Laireiter
 */
public class FileDescriptor extends AbstractFile {

	/**
	 * Since the file extension is very redundant, every different occurence is
	 * stored here.<br>
	 * The {@link #getExtension()} extracts the extension and stores it. If it
	 * finds and entry in this set, it will use it. So not too much string
	 * objects with same text is used.
	 */
	private final static HashMap EXTENSION_SET = new HashMap();

	/**
	 * This field ist meant for {@link #getChildren()}, since a file won't have
	 * children.
	 */
	private final static AbstractFile[] FILE_CHILDREN = new AbstractFile[0];

	/**
	 * This field stores the bitrate of an audio file.
	 */
	private final int bitrate;

	/**
	 * Used to store the file extension.<br>
	 * Will be filled on first call of {@link #getExtension()}.
	 */
	private String extension;

	/**
	 * If not <code>null</code>, this field contains the tag
	 */
	private String[] missingFields = null;

	/**
	 * The new destination of the renamed file.
	 */
	private DirectoryDescriptor targetDirectory = null;

	/**
	 * The filename after the processing (the calculated one).
	 */
	private String targetName = null;

	/**
	 * If <code>true</code>, the audio file can't be read.
	 */
	private boolean unreadable = false;

	/**
	 * Creates an instance. <code>name</code> and <code>parentPath</code>
	 * refert to the source (origin) of the current object.
	 * 
	 * @param name
	 *            The name of the original file.
	 * @param origin
	 *            The directory where the file resides.
	 * @param rate
	 *            The bitrate of the represented file in kbps
	 */
	public FileDescriptor(String name, DirectoryDescriptor origin, int rate) {
		super(false, name, origin);
		assert origin != null;
		this.bitrate = rate;
	}

	/**
	 * @return Returns the bitrate.
	 */
	public int getBitrate() {
		return this.bitrate;
	}

	/**
	 * (overridden) Will always return an empty Array, since files don't have
	 * children.
	 * 
	 * @see entagged.tageditor.tools.renaming.data.AbstractFile#getChildren()
	 */
	public AbstractFile[] getChildren() {
		return FileDescriptor.FILE_CHILDREN;
	}

	/**
	 * This method returns the file extension of the current file.<br>
	 * 
	 * @return The file extension (identifies the format for entagged).
	 */
	public String getExtension() {
		if (this.extension == null) {
			int lastIndex = getName().lastIndexOf('.');
			if (lastIndex != -1) {
				this.extension = getName().substring(lastIndex + 1);
				this.extension = extension.toLowerCase();
				if (EXTENSION_SET.containsKey(extension)) {
					extension = (String) EXTENSION_SET.get(extension);
				} else {
					EXTENSION_SET.put(extension, extension);
				}
			} else {
				extension = "";
			}
		}
		return this.extension;
	}

	/**
	 * @return Returns the missingFields.
	 */
	public String[] getMissingFields() {
		return this.missingFields;
	}

	/**
	 * @return Returns the sourceDirectory.
	 */
	public DirectoryDescriptor getSourceDirectory() {
		return this.getParent();
	}

	/**
	 * @return Returns the targetDirectory.
	 */
	public DirectoryDescriptor getTargetDirectory() {
		return this.targetDirectory;
	}

	/**
	 * @return Returns the targetName.
	 */
	public String getTargetName() {
		return this.targetName;
	}

	/**
	 * @return Returns the unreadable.
	 */
	public boolean isUnreadable() {
		return this.unreadable;
	}

	/**
	 * @param fields
	 *            The missingFields to set.
	 */
	public void setMissingFields(String[] fields) {
		this.missingFields = fields;
	}

	/**
	 * @param dir
	 *            The targetDirectory to set.
	 */
	public void setTargetDirectory(DirectoryDescriptor dir) {
		this.targetDirectory = dir;
	}

	/**
	 * @param name
	 *            The targetName to set.
	 */
	public void setTargetName(String name) {
		this.targetName = name;
	}

	/**
	 * @param unread
	 *            The unreadable to set.
	 */
	public void setUnreadable(boolean unread) {
		this.unreadable = unread;
	}

}
