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
import entagged.tageditor.tools.renaming.data.stat.Category;
import entagged.tageditor.tools.renaming.data.stat.Prop;

/**
 * This propertiy determines whether a child of a file contains property values,
 * which are assigned to be in the category
 * {@link entagged.tageditor.tools.renaming.data.stat.Category#ERROR_CATEGORY}.<br>
 * <br>
 * <b>Warning:</b> Neither add this Property into
 * {@link entagged.tageditor.tools.renaming.data.stat.Statistic#DEFAULT_DIR_STAT}
 * nor
 * {@link entagged.tageditor.tools.renaming.data.stat.Statistic#DEFAULT_FILE_STAT},
 * since this would lead into a infinite recursion.<br>
 * The way to use this is via
 * {@link entagged.tageditor.tools.renaming.data.stat.Statistic#processProperty(AbstractFile, Prop)}
 * on an existing statistic object.
 * 
 * @author Christian Laireiter
 */
public final class ContainsWarningsProperty extends Prop {

	/**
	 * This constant contains all categories, this property is assigned to.
	 */
	private final static List CATEGORY_LIST = Arrays
			.asList(new Category[] { Category.INFORMATIVE_CATEGORY });

	/**
	 * Stores the message for {@link #getDescriptionFor(AbstractFile)}.
	 */
	private final static String MESSAGE = LangageManager
			.getProperty("tagrename.property.containswarnings.msg");

	/**
	 * Constant giving the class a name.
	 */
	public static String PROPERTY_NAME = "CONTAINS_WARNING";

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
		if (file.getStatistic().containsCategory(Category.ERROR_CATEGORY)) {
			result = 1;
		} else {
			// else, if the children have.
			AbstractFile[] children = file.getChildren();
			for (int i = 0; i < children.length && result == 0; i++) {
				// If child has the property we don't need to recurse.
				if (children[i].getStatistic().hasProperty(PROPERTY_NAME)) {
					if (children[i].getStatistic().getProperty(PROPERTY_NAME) > 0) {
						result = 1;
					}
				} else {
					// Now we need to apply this property and will count down i,
					// to check its value once more. (saves some code ;) )
					children[i].getStatistic().processProperty(children[i],
							this);
					i--;
				}
			}
		}
		return result;
	}

}
