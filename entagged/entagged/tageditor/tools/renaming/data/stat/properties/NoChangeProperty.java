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

package entagged.tageditor.tools.renaming.data.stat.properties;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.FileDescriptor;
import entagged.tageditor.tools.renaming.data.stat.Category;
import entagged.tageditor.tools.renaming.data.stat.Prop;

/**
 * This property detemines whether a file would be renamed to the same location,
 * as it already is.<br>
 * 
 * @author Christian Laireiter
 */
public final class NoChangeProperty extends Prop {

	/**
	 * This constant contains all categories, this property is assigned to.
	 */
	private final static List CATEGORY_LIST = Arrays.asList(new Category[] {
			Category.PROCESSING_INFO_CATEGORY, Category.INFORMATIVE_CATEGORY });

	/**
	 * Stores the message for {@link #getDescriptionFor(AbstractFile)}.
	 */
	private final static String MESSAGE = LangageManager
			.getProperty("tagrename.property.nochange.msg");

	/**
	 * Constant giving the class a name.
	 */
	public static String PROPERTY_NAME = "NO_CHANGE";

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.stat.Prop#getCategories()
	 */
	public List getCategories() {
		return CATEGORY_LIST;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.stat.Prop#getDescriptionFor(entagged.tageditor.tools.renaming.data.AbstractFile)
	 */
	public String getDescriptionFor(AbstractFile file) {
		if (file.getStatistic().getProperty(PROPERTY_NAME) > 0)
			return MESSAGE;
		return null;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.stat.Prop#getName()
	 */
	public String getName() {
		return PROPERTY_NAME;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.stat.Prop#operate(entagged.tageditor.tools.renaming.data.AbstractFile)
	 */
	public int operate(AbstractFile file) {
		int result = 0;
		if (file instanceof FileDescriptor) {
			FileDescriptor fd = (FileDescriptor) file;
			if (fd.getTargetDirectory() != null
					&& fd.getPath().equals(
							fd.getTargetDirectory().getPath() + File.separator
									+ fd.getTargetName()))
				result = 1;
		}
		return result;
	}

}
