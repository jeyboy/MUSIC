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

package entagged.tageditor.tools.renaming.data.stat;

/**
 * This class is meant to classify
 * {@link entagged.tageditor.tools.renaming.data.stat.Prop}"erties" into
 * categories. Each implementation of
 * {@link entagged.tageditor.tools.renaming.data.stat.Prop} must return a
 * categroy. With this additional assinment like to {@link #ERROR_CATEGORY} it
 * is easily possible to identifiy a value as an error. Further it is the
 * possible to ask for a description.
 * 
 * @author Christian Laireiter
 */
public final class Category {

	/**
	 * This category is meant to represent serious errors.<br>
	 */
	public final static Category ERROR_CATEGORY = new Category("ERROR");

	/**
	 * A category which Identifies properties, that are not only useful to the
	 * renaming facility but also interesting for the user.<br>
	 * Think of the case that a file would be renamed to the exact location it
	 * already resides.<br>
	 */
	public final static Category INFORMATIVE_CATEGORY = new Category(
			"INFORMATIVE");

	/**
	 * Just a informative category.
	 */
	public final static Category PROCESSING_INFO_CATEGORY = new Category(
			"PROCESSING_INFO");

	/**
	 * This field stores the name of the current category.<br>
	 */
	private final String categoryName;

	/**
	 * Creates a Category.
	 * 
	 * @param name
	 *            Name of the category.
	 */
	public Category(String name) {
		assert name != null;
		this.categoryName = name;
	}

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Category) {
			return this.categoryName.equals(((Category) obj).categoryName);
		}
		return false;
	}

}
