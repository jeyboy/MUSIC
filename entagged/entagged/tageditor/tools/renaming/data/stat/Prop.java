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

import java.io.File;
import java.util.List;
import java.util.WeakHashMap;

import entagged.tageditor.tools.renaming.data.AbstractFile;

/**
 * This is the base for properties and their determine operations.<br>
 * They will be used to determine certain issues, like the source file could not
 * be written (so not moved). Because of the result type of
 * {@link #operate(AbstractFile)} a
 * {@link entagged.tageditor.tools.renaming.data.stat.Statistic} can easily
 * count the number of this issue up the directory structure.
 * 
 * @author Christian Laireiter
 */
public abstract class Prop {

	/**
	 * Stores absolute paths of file instances to themselves.
	 */
	private static WeakHashMap fileCache = new WeakHashMap();

	/**
	 * This method returns a {@link File} instance, for the given file. The
	 * result is cached in a {@link WeakHashMap}, so that subsequent accesses
	 * won't result in many instance creations.
	 * 
	 * @param absolutePath
	 *            The absolute Filepath of the requested file.
	 * @return A cached {@link File} instance.
	 */
	protected static File getFile(String absolutePath) {
		File result = (File) fileCache.get(absolutePath);
		if (result == null) {
			result = new File(absolutePath);
			fileCache.put(absolutePath, result);
		}
		return result;
	}

	/**
	 * This method must return all categories, the particular property is
	 * assigned to.<br>
	 * 
	 * @return A list of {@link Category} objects.
	 */
	public abstract List getCategories();

	/**
	 * This mehtod returns a description for a users, which describes the result
	 * of the current property in respect to the given file.<br>
	 * In the case of
	 * {@link entagged.tageditor.tools.renaming.data.stat.properties.CanNotWriteProperty}
	 * this method should return <code>null</code>, if the file could be
	 * written.<br>
	 * <b>behind the scenes:</b> This method was introduced when a seleciton of
	 * a file should present its errors.
	 * 
	 * @param file
	 *            The file which should be in respect to the current property.
	 * @return The description. May be <code>null</code>.
	 */
	public abstract String getDescriptionFor(AbstractFile file);

	/**
	 * This Method should return a unique identifier for the represented
	 * property
	 * 
	 * @return Name of the Property.
	 */
	public abstract String getName();

	/**
	 * This method gathers the information which represent the current Property.
	 * 
	 * @param file
	 *            The abstractfile.
	 * @return 1 if Property applies to the file, else zero. (int because its
	 *         used for counting).
	 */
	public abstract int operate(AbstractFile file);
}
