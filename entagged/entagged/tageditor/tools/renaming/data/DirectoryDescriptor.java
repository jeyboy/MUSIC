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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This class is for use of the renaming data model to identify a directory.<br>
 * 
 * @author Christian Laireiter
 */
public final class DirectoryDescriptor extends AbstractFile {

	/**
	 * A list of children. (Subdirectories of files).
	 */
	private ArrayList children;

	/**
	 * This field will be set to <code>true</code>, if the is called.<br>
	 */
	private boolean dirty = false;

	/**
	 * If <code>true</code>, the object represents an directory, which was
	 * processed during reading.<br>
	 */
	private boolean sourceDirectory;

	/**
	 * If <code>true</code>, the object represents a target directory of the
	 * renaming.<br>
	 * In opposition to {@link #sourceDirectory} this field may be changed,
	 * since a directory may contain files who would reside at the given
	 * location and just be renamed.
	 */
	private boolean targetDirectory;

	/**
	 * This field will be filled by {@link #getTargetChildren()} for performance
	 * reasons.<br>
	 * 
	 * @see TargetFileComparator
	 */
	private AbstractFile[] targetSortedChildren;

	/**
	 * Creates a directory instance.
	 * 
	 * @param name
	 *            The name of the represented directory.
	 * @param parent
	 *            The file of the parent directory. <code>null</code> if
	 *            current object represents the file system root.
	 * @param source
	 *            if <code>true</code>, the directory is an existing
	 *            directory whose contents are read.
	 * @param target
	 *            if <code>true</code>, the directory will also be a target
	 *            for the renaming movement.
	 */
	public DirectoryDescriptor(String name, DirectoryDescriptor parent,
			boolean source, boolean target) {
		super(true, name, parent);
		if (name == null) {
			throw new IllegalArgumentException("Name of directory is invalid.");
		}
		this.sourceDirectory = source;
		this.targetDirectory = target;
		this.children = new ArrayList();
	}

	/**
	 * This method will add the given file to the childlist of the current
	 * directory.<br>
	 * 
	 * @param file
	 *            The child to be added.
	 */
	public void addChild(AbstractFile file) {
		this.dirty = true;
		if (!children.contains(file)) {
			this.children.add(file);
			targetSortedChildren = null;
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.AbstractFile#getBitrate()
	 */
	public int getBitrate() {
		/*
		 * targetchildren, because we want just the valid files.
		 */
		AbstractFile[] sub = getTargetChildren();
		long sum = 0;
		for (int i = 0; i < sub.length; i++) {
			if (sub[i] instanceof FileDescriptor) {
				// Don't mention files, that won't be moved.
				if (((FileDescriptor) sub[i]).getTargetDirectory() == null)
					continue;
			}
			sum += sub[i].getBitrate();
		}
		if (sub.length > 0) {
			return (int) sum / sub.length;
		}
		return 0;
	}

	/**
	 * (overridden) Warning, sorting is performed if
	 * {@link #addChild(AbstractFile)} was used.
	 * 
	 * @see entagged.tageditor.tools.renaming.data.AbstractFile#getChildren()
	 */
	public AbstractFile[] getChildren() {
		AbstractFile[] result = null;
		if (this.dirty) {
			Collections.sort(this.children);
			result = (AbstractFile[]) children
					.toArray(new AbstractFile[children.size()]);
			this.dirty = false;
		} else
			result = (AbstractFile[]) this.children
					.toArray(new AbstractFile[children.size()]);
		return result;
	}

	/**
	 * This method will return the inserted children, sorted by another
	 * comparator, which sorts taking the target names of the files.<br>
	 * 
	 * @see TargetFileComparator
	 * @return The children sorted by their target names.
	 */
	public AbstractFile[] getTargetChildren() {
		if (targetSortedChildren == null) {
			targetSortedChildren = getChildren();
			Arrays.sort(targetSortedChildren, TargetFileComparator.DEFAULT);
		}
		return targetSortedChildren;
	}

	/**
	 * @return Returns the sourceDirectory.
	 */
	public boolean isSourceDirectory() {
		return sourceDirectory;
	}

	/**
	 * @return Returns the targetDirectory.
	 */
	public boolean isTargetDirectory() {
		return targetDirectory;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.AbstractFile#refreshStat(java.lang.String)
	 */
	public void refreshStat(String propertyName) {
		AbstractFile[] children2 = getChildren();
		for (int i = 0; i < children2.length; i++) {
			children2[i].refreshStat(propertyName);
		}
		super.refreshStat(propertyName);
	}

	/**
	 * @param source
	 *            The sourceDirectory to set.
	 */
	public void setSourceDirectory(boolean source) {
		this.sourceDirectory = source;
	}

	/**
	 * @param target
	 *            The targetDirectory to set.
	 */
	public void setTargetDirectory(boolean target) {
		this.targetDirectory = target;
	}

}
