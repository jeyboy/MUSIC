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

package entagged.tageditor.tools.renaming.gui;

import entagged.tageditor.tools.gui.MultiIcon;
import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.DirectoryDescriptor;
import entagged.tageditor.tools.renaming.data.FileDescriptor;
import entagged.tageditor.tools.renaming.data.stat.properties.CanNotWriteProperty;
import entagged.tageditor.tools.renaming.data.stat.properties.ContainsWarningsProperty;

/**
 * This is a specialized renderer to draw the source tree.<br>
 * For example only in the source tree the flag for undeletable files should
 * occur.<br>
 * 
 * @author Christian Laireiter
 */
public final class SourceTreeCellRenderer extends RenameTreeCellRenderer {

	/**
	 * Is used for deep determination whether a directory contains a file with
	 * errors.<br>
	 */
	private final ContainsWarningsProperty warningProperty = new ContainsWarningsProperty();

	/**
	 * Creates an instance.
	 */
	public SourceTreeCellRenderer() {
		super(true);
	}

	/**
	 * This mehtod modifies the label for common errors.<br>
	 * For example if the source cannot be modified.<br>
	 * 
	 * @param file
	 *            The file whose statistics should be applied.
	 */
	private void handleCommon(AbstractFile file) {
		if (file.getStatistic().getProperty(CanNotWriteProperty.PROPERTY_NAME) > 0) {
			multiIcon.addIcon(getIcon("cannotwrite.png"),
					MultiIcon.BOTTOM_RIGHT);
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.gui.RenameTreeCellRenderer#processDirectoryDescriptor(entagged.tageditor.tools.renaming.data.DirectoryDescriptor)
	 */
	protected void processDirectoryDescriptor(DirectoryDescriptor descriptor) {
		super.processDirectoryDescriptor();
		handleCommon(descriptor);
		if (!descriptor.getStatistic().hasProperty(warningProperty.getName())) {
			descriptor.getStatistic().processProperty(descriptor,
					warningProperty);
		}
		if (descriptor.getStatistic().getProperty(warningProperty.getName()) > 0) {
			multiIcon.addIcon(getIcon("warn-icon.png"), MultiIcon.TOP_LEFT);
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.gui.RenameTreeCellRenderer#processFileDescritpor(entagged.tageditor.tools.renaming.data.FileDescriptor)
	 */
	protected void processFileDescritpor(FileDescriptor descriptor) {
		super.processFileDescritpor(descriptor);
		handleCommon(descriptor);
		if (descriptor.getMissingFields() != null) {
			multiIcon.addIcon(getIcon("bite.png"), MultiIcon.BOTTOM_LEFT);
		}
	}
}
