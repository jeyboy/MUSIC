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
package entagged.tageditor.models;

import java.io.*;

import java.util.Arrays;

import javax.swing.event.*;
import javax.swing.tree.*;


/**
 *  Represents the filesystem to be shown in the Jtree that has this model. The methods in this class allow the JTree component to traverse the file system tree and display the directories. $Id: FileTreeModel.java,v 1.1 2007/03/23 14:17:45 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    v0.03
 */
public class FileTreeModel implements TreeModel {

	/**  Creates and contains the FileFilter that keep only the Directories. */
	private final static FileFilter DIRS_ONLY_FILTER = new DirectoryOnlyFilter();

	/**  Contains the root File object from wich every Tree branch depart */
	private File root;


	/**
	 *  Creates the Model with the given root File Object
	 *
	 * @param  root  The root File
	 */
	public FileTreeModel( File root ) {
		this.root = root;
	}
	
	public Object getChild( Object parent, int index ) {
		File[] children = ( (File) parent ).listFiles( DIRS_ONLY_FILTER );

		if ( ( children == null ) || ( index >= children.length ) )
			return null;
		
		Arrays.sort( children );
		
		return new FileTreeModelFile( children[index] );
	}


	public int getChildCount( Object parent ) {
		File[] children = ( (File) parent ).listFiles( DIRS_ONLY_FILTER );
		
		if ( children == null )
			return 0;
		
		return children.length;
	}


	public int getIndexOfChild( Object parent, Object child ) {
		File[] children = ( (File) parent ).listFiles( DIRS_ONLY_FILTER );
		Arrays.sort( children );
		int i = Arrays.binarySearch(children, child);
		return i;
	}


	public Object getRoot() {
		return root;
	}


	public boolean isLeaf( Object node ) {
		return getChildCount(node) == 0;
	}


	public void addTreeModelListener( TreeModelListener l ) { }


	public void removeTreeModelListener( TreeModelListener l ) { }


	public void valueForPathChanged( TreePath path, Object newvalue ) { }


	private static class DirectoryOnlyFilter implements FileFilter {
		public boolean accept( File pathname ) {
			return pathname.isDirectory() && !pathname.isHidden();// && pathname.canRead();
		}
	}


	/**
	 *  Represent a File in the FileTreeModel. The only purpose of this class is to change to toString() behavior so the names in the JTree are the last directory and not the whole string.
	 *
	 * @author     Raphael Slinckx (KiKiDonK)
	 * @version    v0.03
	 */
	private static class FileTreeModelFile extends File {
		public FileTreeModelFile( File f ) {
			super( f.getAbsolutePath() );
		}
		
		public String toString() {
			return this.getName();
		}
	}

}


