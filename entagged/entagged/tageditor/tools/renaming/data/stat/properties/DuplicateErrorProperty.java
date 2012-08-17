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
import entagged.tageditor.tools.renaming.data.TargetFileComparator;
import entagged.tageditor.tools.renaming.data.stat.Category;
import entagged.tageditor.tools.renaming.data.stat.Prop;

/**
 * This property detemines whether a file or directory can't be modified.<br>
 * 
 * @author Christian Laireiter
 */
public final class DuplicateErrorProperty extends Prop {

	/**
	 * This constant contains all categories, this property is assigned to.
	 */
	private final static List CATEGORY_LIST = Arrays.asList(new Category[] {
			Category.ERROR_CATEGORY, Category.INFORMATIVE_CATEGORY });

	/**
	 * Stores the message for {@link #getDescriptionFor(AbstractFile)}.
	 */
	private final static String MESSAGE = LangageManager
			.getProperty("tagrename.property.duplicateerror.msg");

	/**
	 * Constant giving the class a name.
	 */
	public static String PROPERTY_NAME = "DUPLICATE";

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.stat.Prop#getCategories()
	 */
	public List getCategories() {
		return CATEGORY_LIST;
	}

	/**
	 * This method returns the first found conflict partner.
	 * 
	 * @param file
	 *            The file whose conflict partner should be found.
	 * @return <code>null</code>, if no conflict exists, else the first found
	 *         file, which would be placed at the same location with the same
	 *         name.
	 */
	private AbstractFile getConflict(AbstractFile file) {
		AbstractFile result = null;
		if (file instanceof FileDescriptor) {
			FileDescriptor fd = (FileDescriptor) file;
			if (fd.getTargetDirectory() != null) {
				// DirectoryDescriptor returns them sorted.
				AbstractFile[] children = fd.getTargetDirectory()
						.getTargetChildren();
				int index = search(children, file);
				// Is index not the first entry, then compare the left one.
				if (index > 0) {
					result = TargetFileComparator.DEFAULT.equals(file,
							children[index - 1]) ? children[index - 1] : null;
				}
				// is the not the last one then compare the right.
				if (result == null && index + 1 < children.length) {
					result = TargetFileComparator.DEFAULT.equals(file,
							children[index + 1]) ? children[index + 1] : null;
				}
			}
		}
		return result;
	}

	/**
	 * (overridden)<br>
	 * <b>Consider:</b> If your file instance does not come from the
	 * {@link entagged.tageditor.tools.renaming.processing.FileProcessor} and is
	 * meant as a hack to gather information, you will need to change
	 * {@link #search(AbstractFile[], AbstractFile)}.
	 * 
	 * @see entagged.tageditor.tools.renaming.data.stat.Prop#getDescriptionFor(entagged.tageditor.tools.renaming.data.AbstractFile)
	 */
	public String getDescriptionFor(AbstractFile file) {
		if (file.getStatistic().getProperty(PROPERTY_NAME) > 0) {
			return MESSAGE + "\n" + getConflict(file).getPath();
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
	 * (overridden)<br>
	 * <b>Consider:</b> If your file instance does not come from the
	 * {@link entagged.tageditor.tools.renaming.processing.FileProcessor} and is
	 * meant as a hack to gather information, you will need to change
	 * {@link #search(AbstractFile[], AbstractFile)}.
	 * 
	 * @see entagged.tageditor.tools.renaming.data.stat.Prop#operate(entagged.tageditor.tools.renaming.data.AbstractFile)
	 */
	public int operate(AbstractFile file) {
		return getConflict(file) != null ? 1 : 0;
	}

	/**
	 * This method searches for the given file instance within the given array.<br>
	 * 
	 * @param children
	 *            The Array to search for the file.
	 * @param file
	 *            The file to be searched.
	 * @return Index of the file. <code>-1</code> if not contained.
	 */
	private int search(AbstractFile[] children, AbstractFile file) {
		/*
		 * The Arrays.binarySearch() finds the position of a file, whose target
		 * is the same as the one of the given file. But that's it, the index
		 * may be the one of an conflicting file, since the TargetFileComparator
		 * just compares these target names.
		 */
		int result = Arrays.binarySearch(children, file,
				TargetFileComparator.DEFAULT);
		/*
		 * Now we need to search for the file instance itself. Normally to be
		 * compilant with java equals methods, I should compare the files with
		 * their target names and their paths (origin). But to make it faster,
		 * and since there is a warning in the javadoc, I'll use the "=="
		 * operator for now.
		 */
		if (result >= 0) {
			boolean match = false;
			// Search to the left (+1 because of do while)
			int pointer = result + 1;
			do {
				pointer--;
				// uncomment this to be java std. safe
				// match = children[pointer].getPath().equals(file.getPath());
				match = children[pointer] == file;
			} while (pointer > 1
					&& !match
					&& TargetFileComparator.DEFAULT.equals(children[pointer],
							file));
			if (!match) {
				// (-1) because fo do while
				pointer = result - 1;
				do {
					pointer++;
					// uncomment this to be java std. safe
					// match =
					// children[pointer].getPath().equals(file.getPath());
					match = children[pointer] == file;
				} while ((pointer + 1) < children.length
						&& !match
						&& TargetFileComparator.DEFAULT.equals(
								children[pointer], file));
			}
			if (match) {
				return pointer;
			}
		}
		return 0;
	}
}
