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

package entagged.tageditor.tools.renaming.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

/**
 * TODO: Comment
 * 
 * @author Christian Laireiter
 */
public final class DirectoryPruneStructure {

	/**
	 * This class sorts strings in two steps.<br>
	 * 1. The directories with longest absolute paths first.<br>
	 * 2. String comparision.<br>
	 * <br>
	 * After an array of directories is sorted with that, the deepest
	 * directories are first and directories with same parent are next to each
	 * other.
	 * 
	 * @author Christian Laireiter
	 */
	private final class DeepLevelSorter implements Comparator {

		/**
		 * (overridden)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			int result = o2.toString().length() - o1.toString().length();
			if (result == 0) {
				result = o1.toString().compareTo(o2.toString());
			}
			return result;
		}

	}

	/**
	 * This hashtable stores the absolute paths of source directories , which
	 * are about to be deleted, if they are empty.<br>
	 */
	private HashSet directories2prune;

	/**
	 * This hashtable stores directories, which are to be excluded from
	 * deletion.<br>
	 * During renaming it is easy to determine thos directories, where some
	 * files are for example copied, instead of moving. So these directories
	 * still contain files.<br>
	 * Directories which are added here are:<br>
	 * <ul>
	 * <li> where a file was copied from </li>
	 * <li> where a file was moved or copied to </li>
	 * </ul>
	 */
	private HashSet excludedFromPrune;

	/**
	 * Creates an instance.
	 */
	public DirectoryPruneStructure() {
		excludedFromPrune = new HashSet();
		directories2prune = new HashSet();
	}

	/**
	 * This method adds the given directory , to the list of directories, which
	 * will be considered for pruning empty directories.
	 * 
	 * @param directory
	 *            The aboslute path of the directory to be deleted.
	 */
	public void addDirectory(String directory) {
		if (directory != null && !excludedFromPrune.contains(directory)
				&& !directories2prune.contains(directory)) {
			directories2prune.add(directory);
			File dirFile = new File(directory);
			if (dirFile.getParent() != null) {
				addDirectory(dirFile.getParent());
			}
		}
	}

	/**
	 * This method notifies the strucure, that a directory and all of its
	 * parents are not to be deleted.<br>
	 * 
	 * @param directory
	 *            The absolute path of the directory, wich doesn't be deleted.
	 */
	public void excludeDirectory(String directory) {
		if (directory != null && !excludedFromPrune.contains(directory)) {
			directories2prune.remove(directory);
			excludedFromPrune.add(directory);
			File dirFile = new File(directory);
			if (dirFile.getParent() != null) {
				excludeDirectory(dirFile.getParent());
			}
		}
	}

	/**
	 * This method starts the deletion of all directories, which were added and
	 * not excluded.<br>
	 * If an added directory contains any other file or directory, it won't be
	 * deleted. The process will start with the deepest directories and moves on
	 * to the top level directories. The movement will stop on two conditions,
	 * first the case, that a directory has been excluded, the other is that a
	 * directory wich is about to be deleted still contains something.<br>
	 * 
	 * @return A list of directories, which should be deleted, but where the
	 *         operation failed.
	 */

	public String[] prune() {
		ArrayList result = new ArrayList();
		ArrayList directoryList = new ArrayList(directories2prune);
		Collections.sort(directoryList, new DeepLevelSorter());
		Iterator it = directoryList.iterator();
		// No operate from the deepest directories up to the top level
		// directories.
		while (it.hasNext()) {
			String currentDirectoryPath = (String) it.next();
			if (!excludedFromPrune.contains(currentDirectoryPath)) {
				File instance = new File(currentDirectoryPath);
				// If it has already been deleted, its OK
				if (instance.exists()) {
					if (!instance.canWrite() || instance.list().length > 0) {
						excludeDirectory(currentDirectoryPath);
					} else {
						if (!instance.delete()) {
							result.add(currentDirectoryPath);
						}
					}
				}
			}
		}
		return (String[]) result.toArray(new String[result.size()]);
	}
}
