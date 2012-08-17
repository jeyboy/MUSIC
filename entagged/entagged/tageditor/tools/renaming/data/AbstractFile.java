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

import java.io.File;
import java.util.ArrayList;

import entagged.tageditor.tools.renaming.data.stat.Statistic;

/**
 * This class is the base for the data model of the renaming facility.<br>
 * To easy build up trees containing the structure of processed files and the
 * strucuture of the result, this is the base for both directories and files.<br>
 * A common property is for example whether the object represents a directory or
 * not (in that case a file).<br>
 * 
 * @author Christian Laireiter
 */
public abstract class AbstractFile implements Comparable {

	/**
	 * This field identifies the current object as a directory.<br>
	 */
	private final boolean directory;

	/**
	 * Stores the name of the file.
	 */
	private final String filename;

	/**
	 * If this field is set by {@link #overrideName} the name which should be
	 * used for renaming is this.<br>
	 * A second variable is needed, since the bitrate-Pattern needs to be
	 * replaced if some options of the users change.<br>
	 * For example a directory would contain some files that cannot be
	 * processed, because they cannot be moved.<br>
	 * If the user enables the copy option, the target directory will then
	 * contain them and the bitrate must be replaced once again.
	 */
	private String overrideName = null;

	/**
	 * This field stores the parent file of the current one.<br>
	 * If <code>null</code>, the current file represents a filesystem root.
	 */
	private final DirectoryDescriptor parentFile;

	/**
	 * This field will contain the statistic on the current file (or directory).<br>
	 * It will be set on first call of {@link #getStatistic()}.<br>
	 */
	private Statistic statistic;

	/**
	 * Creates an instance.
	 * 
	 * @param dir
	 *            <code>true</code>, if the object should represent a
	 *            directory.
	 * @param name
	 *            The name of the file or directory.
	 * @param parent
	 *            The file instance, which is the parent.
	 */
	public AbstractFile(boolean dir, String name, DirectoryDescriptor parent) {
		assert name != null;
		this.directory = dir;
		this.filename = name;
		this.parentFile = parent;
	}

	/**
	 * This method compares two AbstractFiles.
	 * 
	 * @param other
	 *            The oghet object.
	 * @return see {@link Comparable#compareTo(Object)}
	 */
	public int compareTo(Object other) {
		int result = 0;
		if (other instanceof AbstractFile) {
			AbstractFile abFile = (AbstractFile) other;
			if (this.directory != abFile.directory) {
				if (this.directory)
					result = -1;
				else
					result = 1;
			} else
				result = this.getName().compareTo(abFile.getName());
		}
		return result;
	}

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof AbstractFile) {
			AbstractFile abFile = (AbstractFile) other;
			return abFile.directory == this.directory
					&& abFile.filename.equals(filename);
		}
		return super.equals(other);
	}

	/**
	 * This method returns the bitrate.<br>
	 * If its a directory, it will be the average.
	 * 
	 * @return The average bitrate of a directory, or the absolute one of files.
	 */
	public abstract int getBitrate();

	/**
	 * This method returns the children of the current file.<br>
	 * 
	 * @return The children of the current object.
	 */
	public abstract AbstractFile[] getChildren();

	/**
	 * This method returns the name of the file or directory.<br>
	 * If {@link #overrideName} has been used withou giving <code>null</code>,
	 * that will be returned here.<br>
	 * 
	 * @return the name of the file or directory. For filedescriptor its always
	 *         the source.
	 */
	public String getName() {
		if (this.overrideName != null)
			return this.overrideName;
		return this.filename;
	}

	/**
	 * Returns the name, which has been used at construction of the object.<br>
	 * 
	 * @return The name at construction.
	 */
	public String getOriginalName() {
		return this.filename;
	}

	/**
	 * This method returns the parent of the current file.<br>
	 * 
	 * @return <code>null</code> if no parent assigned.
	 */
	public DirectoryDescriptor getParent() {
		return this.parentFile;
	}

	/**
	 * This method returns the path of the current file.
	 * 
	 * @return The path as string.
	 */
	public String getPath() {
		if (parentFile != null)
			return parentFile.getPath() + File.separator + getName();
		return getName();
	}

	/**
	 * Returns the statistics for the current file.<br>
	 * 
	 * @return Statistic of current file.
	 */
	public Statistic getStatistic() {
		if (this.statistic == null) {
			this.statistic = Statistic.processFile(this);
		}
		return this.statistic;
	}

	/**
	 * This method will return the directory structure of the current file, like
	 * a treepath.
	 * 
	 * @return The treepath of the current object as an array of AbstractFile.
	 */
	public AbstractFile[] getTreePath() {
		ArrayList result = new ArrayList();
		AbstractFile current = this;
		do {
			result.add(0, current);
			current = current.getParent();
		} while (current != null);
		return (AbstractFile[]) result.toArray(new AbstractFile[result.size()]);
	}

	/**
	 * This method returns <code>true</code>, if the current object
	 * represents a directory.<br>
	 * 
	 * @return <code>true</code> if directory.
	 */
	public boolean isDirectory() {
		return this.directory;
	}

	/**
	 * This method should be used with care.<br>
	 * It is meant to be used to replace the directories name in order to handle
	 * the bitrate pattern.<br>
	 * 
	 * @param name
	 *            the new directory name.
	 */
	public void overrideName(String name) {
		this.overrideName = name;
	}

	/**
	 * This method will update the statistic property given by name.
	 * 
	 * @param propertyName
	 *            Name of the property to updated.
	 */
	public void refreshStat(String propertyName) {
		getStatistic().updateProperty(propertyName, this);
	}

	/**
	 * (overridden) Returns the name.<br>
	 * 
	 * @see #getName()
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}
}
