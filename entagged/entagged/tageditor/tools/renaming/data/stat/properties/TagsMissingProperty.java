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

import java.util.Arrays;
import java.util.List;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.FileDescriptor;
import entagged.tageditor.tools.renaming.data.stat.Category;
import entagged.tageditor.tools.renaming.data.stat.Prop;

/**
 * This property detemines whether a file lacks of tag values, which where
 * defined in the patterns.<br>
 * 
 * @author Christian Laireiter
 */
public final class TagsMissingProperty extends Prop {

	/**
	 * This constant contains all categories, this property is assigned to.
	 */
	private final static List CATEGORY_LIST = Arrays.asList(new Category[] {
			Category.ERROR_CATEGORY, Category.INFORMATIVE_CATEGORY });

	/**
	 * Stores the message for {@link #getDescriptionFor(AbstractFile)}.
	 */
	private final static String MESSAGE = LangageManager
			.getProperty("tagrename.property.tagsmissing.msg");

	/**
	 * Constant giving the class a name.
	 */
	public static String PROPERTY_NAME = "MISSING_TAG";

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
		if (file instanceof FileDescriptor
				&& file.getStatistic().getProperty(PROPERTY_NAME) > 0) {
			FileDescriptor fd = (FileDescriptor) file;
			StringBuffer result = new StringBuffer(MESSAGE);
			String[] missingFields = fd.getMissingFields();
			for (int i = 0; i < missingFields.length; i++) {
				if (i > 0) {
					result.append(',');
				}
				result.append(missingFields[i]);
			}
			return result.toString();
		}
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
			if (((FileDescriptor) file).getMissingFields() != null)
				result = 1;
		}
		return result;
	}

}
