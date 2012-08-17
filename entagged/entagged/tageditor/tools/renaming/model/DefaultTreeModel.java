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

package entagged.tageditor.tools.renaming.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.ConfigurationChangeListener;
import entagged.tageditor.tools.renaming.data.DirectoryDescriptor;
import entagged.tageditor.tools.renaming.data.FileDescriptor;

/**
 * This class is a treemodel for
 * {@link entagged.tageditor.tools.renaming.data.DirectoryDescriptor} objects.<br>
 * This class will apply a given filter to all its results.
 * 
 * @author Christian Laireiter
 */
public class DefaultTreeModel implements TreeModel, ConfigurationChangeListener {

	/**
	 * This field holds the filter which reduces the tree represented by this
	 * model.
	 */
	private final DefaultFilter filter;

	/**
	 * This field contains the {@link TreeModelListener} objects, added by
	 * {@link #addTreeModelListener(TreeModelListener)}
	 */
	private final ArrayList listener = new ArrayList();

	/**
	 * This field contains those directories, which are given during
	 * construction and applied to the given filter.
	 */
	private final ArrayList topDirectories;

	/**
	 * This string will represent the root of the represented tree.
	 */
	private final String treeRoot;

	/**
	 * This field will contain all paths of target directories, which have been
	 * rejected for display in the target tree.<br>
	 * This is used for directories which would remain empty after processing.<br>
	 * Those directories won't be created, however they would be displayed.<br>
	 * This field will be cleared when {@link #optionHasChanged()} is called.
	 */
	private final HashSet rejectedPaths;

	/**
	 * Similiar to {@link #rejectedPaths}. But here the accepted are stored.
	 */
	private final HashSet acceptedPaths;

	/**
	 * Creates an instance which will process the given directories (<code>treeRoots</code>)
	 * and use <code>treeFilter</code> to reduce the strucutre.<br>
	 * 
	 * @param treeRoots
	 *            The directories which represent the top level of the tree.
	 *            <b>Caution:</b> They are held against the given filter.
	 * @param treeFilter
	 *            The filter which reduces the tree structure represented by the
	 *            {@link DirectoryDescriptor} objects themselves.
	 * @param treeRootTitle
	 *            A String which will be used to name the root node of the
	 *            represented tree.
	 */
	public DefaultTreeModel(DirectoryDescriptor[] treeRoots,
			DefaultFilter treeFilter, String treeRootTitle) {
		assert treeFilter != null;
		assert treeRoots != null;
		this.filter = treeFilter;
		topDirectories = new ArrayList();
		this.rejectedPaths = new HashSet();
		this.acceptedPaths = new HashSet();
		for (int i = 0; i < treeRoots.length; i++) {
			if (filter.accept(treeRoots[i])) {
				topDirectories.add(treeRoots[i]);
			}
		}
		/*
		 * Now create a root object for the tree
		 */
		// create a new one for unique use.
		this.treeRoot = new String(treeRootTitle);
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public synchronized void addTreeModelListener(TreeModelListener l) {
		if (!this.listener.contains(l)) {
			this.listener.add(l);
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index) {
		Object result = null;
		if (parent == this.treeRoot) {
			result = topDirectories.get(index);
		} else {
			AbstractFile[] children = getFilteredChildren((AbstractFile) parent);
			result = children[index];
		}
		return result;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {
		if (parent == this.treeRoot) {
			return topDirectories.size();
		}
		return getFilteredChildren((AbstractFile) parent).length;
	}

	/**
	 * This method returns the subdirectories or files of the given node.<br>
	 * 
	 * @param file
	 *            The file object, whose children should be returned.
	 * @return Subnodes of given file, held against the filter.
	 */
	private AbstractFile[] getFilteredChildren(AbstractFile file) {
		ArrayList result = new ArrayList();

		if (file.isDirectory()) {
			AbstractFile[] children = file.getChildren();
			for (int i = 0; i < children.length; i++) {
				if (children[i] instanceof DirectoryDescriptor) {
					DirectoryDescriptor dir = (DirectoryDescriptor) children[i];
					if (this.filter.accept(dir)) {
						String path = dir.getPath();
						// has this directory already been accepted after 
						// last change of options
						if (acceptedPaths.contains(path)) {
							result.add(dir);
						} else if (!rejectedPaths.contains(path)) {
							// If it hast not been rejected, it must
							// be determined, whether it contains files.
							if (getFilteredChildren(children[i]).length > 0) {
								// If it hast children, accept it.
								result.add(children[i]);
								acceptedPaths.add(path);
							} else {
								// mark it as rejected (no contents)
								rejectedPaths.add(path);
							}
						}

					}
				} else {
					if (this.filter.accept((DirectoryDescriptor) file,
							((FileDescriptor) children[i]))) {
						result.add(children[i]);
					}
				}
			}
		}
		Collections.sort(result);
		AbstractFile[] files = (AbstractFile[]) result
				.toArray(new AbstractFile[result.size()]);
		return files;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
	 *      java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {
		AbstractFile[] children = null;
		if (parent == this.treeRoot) {
			children = (AbstractFile[]) this.topDirectories
					.toArray(new AbstractFile[topDirectories.size()]);
		} else {
			children = getFilteredChildren((AbstractFile) parent);
		}
		int result = Arrays.binarySearch(children, child);
		if (result < 0) {
			result = -1;
		}
		return result;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot() {
		return this.treeRoot;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		if (node == treeRoot)
			return false;
		return getFilteredChildren((AbstractFile) node).length == 0;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.renaming.data.ConfigurationChangeListener#optionHasChanged()
	 */
	public void optionHasChanged() {
		rejectedPaths.clear();
		acceptedPaths.clear();
		final Iterator it = this.listener.iterator();
		while (it.hasNext()) {
			TreeModelListener current = (TreeModelListener) it.next();
			current.treeStructureChanged(new TreeModelEvent(this, new TreePath(
					getRoot())));
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public synchronized void removeTreeModelListener(TreeModelListener l) {
		this.listener.remove(l);
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
	 *      java.lang.Object)
	 */
	public synchronized void valueForPathChanged(TreePath path, Object newValue) {
		// Nothing to do
	}

}
