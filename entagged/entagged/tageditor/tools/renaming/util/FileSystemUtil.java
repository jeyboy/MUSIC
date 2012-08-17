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

package entagged.tageditor.tools.renaming.util;

import java.io.File;
import java.util.HashSet;

import entagged.tageditor.resources.PreferencesManager;

/**
 * This class provides some tools for filesystem related issues. <br>
 * 
 * @author Christian Laireiter
 */
public final class FileSystemUtil {

    /**
     * shortcut, for {@link #isFilesystemCaseSensitive()}.
     */
    private static Boolean fileSystemCaseSensitive = null;

    /**
     * This method counts the number of files of the given directory
     * recursiveley.
     * 
     * @param file
     *            The directory to be scanned.
     * @return The number of real files within the given directory.
     */
    public static int countFiles(File file) {
        int result = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                if (files[i].isDirectory()) {
                    result += countFiles(files[i]);
                } else {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * This method tries to determine whether the current file system is case
     * sensitive or not.
     * 
     * @return <code>true</code>, if the filesystem is case sensitive like
     *         linux.
     * @throws Exception
     *             If the implemented way of determination doesn't work.
     */
    public static boolean isFilesystemCaseSensitive() throws Exception {
        if (fileSystemCaseSensitive == null) {
            String tmp = PreferencesManager.get("filesystem.casesensitive");
            if (tmp != null) {
                fileSystemCaseSensitive = Boolean.valueOf(tmp);
            }
        }
        if (fileSystemCaseSensitive == null) {
            File[] roots = File.listRoots();
            int result = -1;
            for (int i = 0; i < roots.length && result == -1; i++) {
                File currentRoot = roots[i];
                /*
                 * Step one buffer all filenames
                 */
                File[] files = currentRoot.listFiles();
                HashSet fileNames = new HashSet();
                for (int j = 0; files != null && j < files.length; j++) {
                    fileNames.add(files[j].getName());
                }
                /*
                 * Now pick one after another for check.
                 */
                for (int j = 0; files != null && j < files.length
                        && result == -1; j++) {
                    File currentFile = files[j];
                    String path = currentFile.getAbsolutePath();
                    String otherPath = path.toUpperCase();
                    if (otherPath.equals(path)) {
                        otherPath = path.toLowerCase();
                        // If the path doesn't have alpha chars, continue with
                        // next
                        // candidate.
                        if (otherPath.equals(path))
                            continue;
                    }
                    // If now the otherPath candidate was read before (see
                    // fileNames)
                    // Its not good so... continue
                    if (fileNames.contains(otherPath)) {
                        continue;
                    }
                    // Now we can get the new file instance
                    File otherFile = new File(otherPath);
                    if (!otherFile.exists())
                        result = 1;
                    else
                        result = 0;
                }
            }
            if (result == -1) {
                throw new IllegalStateException(
                        "Cannot determine filesystem sensitivity");
            }
            fileSystemCaseSensitive = Boolean.valueOf(result == 1);
            PreferencesManager.put("filesystem.casesensitive",
                    fileSystemCaseSensitive.toString());
        }
        return fileSystemCaseSensitive.booleanValue();
    }

}