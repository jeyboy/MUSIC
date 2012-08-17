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
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class stores all information which was detected during processing the
 * selection.<br>
 * 
 * @author Christian Laireiter
 */
public final class ProcessingResult {

	/**
	 * This fild contains the {@link DirectoryDescriptor} objects, which
	 * represent the roots of the file system.<br>
	 */
	private final ArrayList fileSystemRoots;

	/**
	 * Creates a new instance of the result object.
	 */
	public ProcessingResult() {
		fileSystemRoots = new ArrayList();
	}

	/**
	 * This method will search for a {@link DirectoryDescriptor} which
	 * represents the given path.
	 * 
	 * @param path
	 *            The requested path.
	 * @return <code>null</code> if no matchin directory was processed.
	 */
	public DirectoryDescriptor getDirectoryByPath(String path) {
		DirectoryDescriptor[] result = getDirectoryPath(path);
		if (result != null) {
			return result[result.length - 1];
		}
		return null;
	}

	/**
	 * This method will return every directory descriptor object, which will
	 * make up the given path.<br>
	 * 
	 * @param path
	 *            The requested path.
	 * @return <code>null</code> if the path cannot be found. Else each path
	 *         beginning from the top.
	 */
	public DirectoryDescriptor[] getDirectoryPath(String path) {
		assert path != null;
		ArrayList result = new ArrayList();
		/*
		 * First use the systems file structure to generate an array of path
		 * components leading to the given path.
		 */
		File file = new File(path);
		ArrayList pathNames = new ArrayList();
		while (file != null) {
			pathNames.add(0, file.getName());
			file = file.getParentFile();
		}
		DirectoryDescriptor currentDescriptor = null;
		/*
		 * First find the right root
		 */
		Iterator it = fileSystemRoots.iterator();
		while (it.hasNext() && currentDescriptor == null) {
			DirectoryDescriptor curr = (DirectoryDescriptor) it.next();
			if (curr.getName().equals(pathNames.get(0).toString())) {
				currentDescriptor = curr;
				result.add(curr);
				pathNames.remove(0);
			}
		}
		// Now we have found the root and traverse the structure downwards.
		it = pathNames.iterator();
		while (it.hasNext() && currentDescriptor != null) {
			String currentName = (String) it.next();
			AbstractFile[] children = currentDescriptor.getChildren();
			for (int i = 0; i < children.length; i++) {
				if (children[i].isDirectory()) {
					if (children[i].getName().equals(currentName)) {
						currentDescriptor = (DirectoryDescriptor) children[i];
						result.add(currentDescriptor);
						// Break the child search.
						break;
					}
				}
			}
		}
		return (DirectoryDescriptor[]) result
				.toArray(new DirectoryDescriptor[result.size()]);
	}

	/**
	 * This method returns the DirectoryDescriptor objects, which represent the
	 * root of the filesystem. Only those directories are contained which have
	 * something to do with the renaming process. The origin of each scanned
	 * file contained, as well as their destinations.<br>
	 * 
	 * @return A list of top level directories.
	 */
	public DirectoryDescriptor[] getFileSystemRoots() {
		return (DirectoryDescriptor[]) fileSystemRoots
				.toArray(new DirectoryDescriptor[fileSystemRoots.size()]);
	}

	/**
	 * This method sets the filesystem-root objects of the result.<br>
	 * 
	 * @param directories
	 *            The file system root representations.
	 */
	public void setFileSystemRoots(DirectoryDescriptor[] directories) {
		Arrays.sort(directories);
		this.fileSystemRoots.clear();
		this.fileSystemRoots.addAll(Arrays.asList(directories));
	}

}