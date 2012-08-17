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

import java.util.Comparator;

import entagged.tageditor.tools.renaming.util.FileSystemUtil;

/**
 * This comparator is used to sort an array of
 * {@link entagged.tageditor.tools.renaming.data.AbstractFile}instances in
 * respect to their target location. <br>
 * Like always, directories will be placed in front of files. <br>
 * If we have got two
 * {@link entagged.tageditor.tools.renaming.data.FileDescriptor}objects their
 * target names will be used for comparison.
 * 
 * @author Christian Laireiter
 */
public class TargetFileComparator implements Comparator {

    /**
     * This instance is the default instance. <br>
     * To not flood the memory with instances of this comparator, this field
     * should be used. <br>
     */
    public static final TargetFileComparator DEFAULT = new TargetFileComparator();

    /**
     * This field will contain the value of
     * {@link FileSystemUtil#isFilesystemCaseSensitive()}.
     */
    private static final boolean FILESYSTEM_CASESENSITIVE;

    static {
        boolean value = false;
        try {
            value = FileSystemUtil.isFilesystemCaseSensitive();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FILESYSTEM_CASESENSITIVE = value;
    }

    /**
     * (overridden)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        if (o1.getClass() != o2.getClass()) {
            // Directories before all others
            if (o1 instanceof DirectoryDescriptor)
                return -1;
            return 1;
        }
        if (o1 instanceof FileDescriptor) {
            FileDescriptor fd1 = (FileDescriptor) o1;
            FileDescriptor fd2 = (FileDescriptor) o2;
            // If both will be relocated, compare their target names
            if (fd1.getTargetName() != null && fd2.getTargetName() != null) {
                if (FILESYSTEM_CASESENSITIVE)
                    return fd1.getTargetName().compareTo(fd2.getTargetName());
                return fd1.getTargetName().compareToIgnoreCase(
                        fd2.getTargetName());
            }
            // Now at least one of the files won't be moved, this means, the one
            // will be put to the end.
            if (fd2.getTargetName() == null) {
                return -1;
            }
            return 1;
        }
        if (o1 instanceof DirectoryDescriptor) {
            return ((DirectoryDescriptor) o1).compareTo(o2);
        }
        return 0;
    }

    /**
     * This method compares the target names of the given files, to determine
     * equality. <br>
     * 
     * @param f1
     *            File to be compared.
     * @param f2
     *            File to be compared.
     * @return <code>true</code> if the target names of the two files are
     *         identical.
     */
    public boolean equals(AbstractFile f1, AbstractFile f2) {
        return compare(f1, f2) == 0;
    }

}