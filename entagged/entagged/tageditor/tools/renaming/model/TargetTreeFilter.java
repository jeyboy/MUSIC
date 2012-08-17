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
import entagged.tageditor.tools.renaming.data.RenameConfiguration;
import entagged.tageditor.tools.renaming.data.stat.Statistic;
import entagged.tageditor.tools.renaming.data.stat.properties.CanNotWriteProperty;
import entagged.tageditor.tools.renaming.data.stat.properties.DestinationExistsProperty;
import entagged.tageditor.tools.renaming.data.stat.properties.DuplicateErrorProperty;

/**
 * This filter is meant to filter all files which contain errors.<br>
 * 
 * @author Christian Laireiter
 */
public final class TargetTreeFilter extends DefaultFilter {

	/**
	 * This field is used to gather information about various user decisions.<br>
	 * For example, if an audio file cannot be modified it would normally be
	 * excluded by this filter. However, if
	 * {@link RenameConfiguration#isCopyUnmodifiableFiles()} is
	 * <code>true</code>, it will be take despite that fact, because the
	 * renaming will copy the source file.
	 */
	private RenameConfiguration renameConfig;

	/**
	 * Creates an instance.
	 * 
	 * @param config
	 *            The configuration of the renaming
	 */
	public TargetTreeFilter(RenameConfiguration config) {
		super(false);
		assert config != null;
		this.renameConfig = config;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.model.DefaultFilter#accept(entagged.tageditor.tools.renaming.data.DirectoryDescriptor,
	 *      entagged.tageditor.tools.renaming.data.FileDescriptor)
	 */
	public boolean accept(DirectoryDescriptor parent, FileDescriptor file) {
		if (super.accept(parent, file)) {
			// Now filter more.
			final Statistic stat = file.getStatistic();
			int errorCount = 0;
			// If the source would be copied on that cases the there is no
			// error.
			if (!renameConfig.isCopyUnmodifiableFiles()) {
				errorCount = stat
						.getProperty(CanNotWriteProperty.PROPERTY_NAME);
			}
			errorCount += stat
					.getProperty(DestinationExistsProperty.PROPERTY_NAME);
			errorCount += stat
					.getProperty(DuplicateErrorProperty.PROPERTY_NAME);
			return errorCount == 0;
		}
		return false;
	}

}
