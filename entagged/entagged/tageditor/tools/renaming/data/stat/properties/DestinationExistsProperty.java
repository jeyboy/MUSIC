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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.FileDescriptor;
import entagged.tageditor.tools.renaming.data.stat.Category;
import entagged.tageditor.tools.renaming.data.stat.Prop;

/**
 * <b>Be Advised:</b> This class is meant to look for errors.<br>
 * However, for directories it is more informative. A positive on files should
 * be aknowledged as an error. <br>
 * <br>
 * This property detemines whether the destination of the file or directory
 * already exists.<br>
 * Before the result will be counted (file exists) it is tested, that the
 * {@link entagged.tageditor.tools.renaming.data.stat.properties.NoChangeProperty}
 * will not match. Only then, the existing target differs from the tested file.
 * <br>
 * 
 * @author Christian Laireiter
 */
public final class DestinationExistsProperty extends Prop {

	/**
	 * This constant contains all categories, this property is assigned to. If
	 * it is created to process Directories.<br>
	 * If processing a directory, the existence won't be an error. So its just a
	 * {@link Category#PROCESSING_INFO_CATEGORY}
	 */
	private final static List CATEGORY_LIST_DIRECTORY = new ArrayList(Arrays
			.asList(new Category[] { Category.PROCESSING_INFO_CATEGORY }));

	/**
	 * This constant contains all category, this property is assigned to if it
	 * is created for processing audio files.<br>
	 */
	private final static List CATEGORY_LIST_FILE = new ArrayList(Arrays
			.asList(new Category[] { Category.PROCESSING_INFO_CATEGORY,
					Category.INFORMATIVE_CATEGORY, Category.ERROR_CATEGORY }));

	/**
	 * Thie message is used for {@link #getDescriptionFor(AbstractFile)}.
	 */
	private final static String MESSAGE = LangageManager
			.getProperty("tagrename.property.destinationexists.msg");

	/**
	 * This field is used within the {@link #operate(AbstractFile)} method, to
	 * detemine, whether the existing file (at target location) is the tested
	 * file itself.
	 */
	private final static NoChangeProperty PROP_NC = new NoChangeProperty();

	/**
	 * Constant giving the class a name.
	 */
	public static String PROPERTY_NAME = "TARGET_EXISTS";

	/**
	 * This field will store either {@link #CATEGORY_LIST_DIRECTORY} or
	 * {@link #CATEGORY_LIST_FILE}, based on the arguments at creation.
	 */
	private final List categoryList;

	/**
	 * This creates a instance.<br>
	 * If <code>forFiles</code> is <code>true</code>, the instance is meant
	 * to represent an error. Since files shouldn't be replace existing ones.<br>
	 * If <code>false</code> it is meant for directories, which is naturally
	 * more informative.
	 * 
	 * @param forFiles
	 *            if it's meant for files give <code>true</code>.
	 */
	public DestinationExistsProperty(boolean forFiles) {
		if (forFiles) {
			this.categoryList = CATEGORY_LIST_FILE;
		} else {
			this.categoryList = CATEGORY_LIST_DIRECTORY;
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.stat.Prop#getCategories()
	 */
	public List getCategories() {
		return this.categoryList;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.stat.Prop#getDescriptionFor(entagged.tageditor.tools.renaming.data.AbstractFile)
	 */
	public String getDescriptionFor(AbstractFile file) {
		if (file.getStatistic().getProperty(PROPERTY_NAME) > 0) {
			return MESSAGE;
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
		if (file.isDirectory()) {
			return getFile(file.getPath()).exists() ? 1 : 0;
		}
		if (PROP_NC.operate(file) <= 0) {
			FileDescriptor fd = ((FileDescriptor) file);
			if (fd.getTargetDirectory() != null) {
				return getFile(
						fd.getTargetDirectory().getPath() + File.separator
								+ fd.getTargetName()).exists() ? 1 : 0;
			}
		}
		return 0;
	}

}
