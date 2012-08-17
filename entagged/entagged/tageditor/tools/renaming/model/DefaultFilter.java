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

package entagged.tageditor.tools.renaming.model;

import entagged.tageditor.tools.renaming.data.DirectoryDescriptor;
import entagged.tageditor.tools.renaming.data.FileDescriptor;

/**
 * This class is meant to filter
 * {@link entagged.tageditor.tools.renaming.data.DirectoryDescriptor} objects
 * for display in a tree.<br>
 * A use case is to filter the renaming data structure for just displaying those
 * directories, which are marked as target.So you get a tree which displays only
 * the result.
 * 
 * @author Christian Laireiter
 */
public class DefaultFilter {

	/**
	 * If <code>true</code>, the filter only accepts thoso
	 * DirectoryDescriptor objects which are marked as source. Else only tose
	 * marked as target.<br>
	 * 
	 * @see entagged.tageditor.tools.renaming.data.DirectoryDescriptor#isSourceDirectory()
	 * @see entagged.tageditor.tools.renaming.data.DirectoryDescriptor#isTargetDirectory()
	 */
	protected final boolean source;

	/**
	 * Creates an instance of the filter.
	 * 
	 * @param src
	 *            if <code>true</code>, only directories are allowed which
	 *            represent a scanned directory. Else only those who are the
	 *            target of a renaming operation.
	 */
	public DefaultFilter(boolean src) {
		this.source = src;
	}

	/**
	 * This method does the filtering and needs to be overriden on subclasses.
	 * 
	 * @param descriptor
	 *            The {@link DirectoryDescriptor} which should be held against
	 *            the filtering rules.
	 * @return <code>true</code>, if the directory is accepted.
	 */
	public boolean accept(DirectoryDescriptor descriptor) {
		return source && descriptor.isSourceDirectory() || !source
				&& descriptor.isTargetDirectory();
	}

	/**
	 * This method does the filtering on files an needs to be overriden on
	 * subclasses.
	 * 
	 * @param parent
	 *            This parameter is needed to decide, whether the file object
	 *            should be displayed. If the file is originally in the given
	 *            directory, however just the target tree should be accepted, it
	 *            must be detemined if the file resides there.
	 * @param file
	 *            The {@link FileDescriptor} of the file which sould be held
	 *            against the filtering rules.
	 * @return <code>true</code>, if file is accepted.
	 */
	public boolean accept(DirectoryDescriptor parent, FileDescriptor file) {
		boolean result = false;
		if (this.source) {
			if (file.getSourceDirectory() == parent) {
				result = true;
			}
		} else {
			if (file.getTargetDirectory() == parent) {
				result = true;
			}
		}
		return result;
	}

}
