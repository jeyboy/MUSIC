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
import java.util.Iterator;

import entagged.tageditor.tools.renaming.pattern.DirectoryPattern;
import entagged.tageditor.tools.renaming.pattern.FilePattern;
import entagged.tageditor.tools.stringtransform.TransformSet;

/**
 * This class stores the configuration of the renaming operation.
 * 
 * @author Christian Laireiter
 */
public final class RenameConfiguration {

	/**
	 * If this field is <code>true</code>, all audiofiles which can't be
	 * modified, should be copied instead of being renamed.<br>
	 */
	private boolean copyUnmodifiableFiles = false;

	/**
	 * Stores the pattern instance for determining the directories.
	 */
	private final DirectoryPattern directoryPattern;

	/**
	 * If this field is <code>true</code>, then the track value, if it
	 * contains a number, will extended to two digits if necessary.<br>
	 * &quot;1&quot; will become &quot;01&quot;.<br>
	 */
	private boolean extendTrackNumbers = false;

	/**
	 * Stores the pattern instance for determining the filenames.
	 */
	private final FilePattern filePattern;

	/**
	 * This field contains the registered {@link ConfigurationChangeListener}.<br>
	 */
	private final ArrayList listener;

	/**
	 * If this is <code>true</code>, the renaming should search for similiar
	 * directories, when determining the target. And since audio tag values may
	 * differ in their case, the first determined one is taken for future files.<br>
	 * <b>Use:</b> On filesystems which are case sensitive, it may happen, that
	 * there already exists a directory "SCOOTER". The file which is about to be
	 * renamed would be placed into a directory "scooter" (lowercase). Now with
	 * this option set, the parent directory of the resulting "scooter" will be
	 * scanned in respect to find the "SCOOTER"-directory. Then the file will be
	 * placed there insead. Further, if there already exist two directories like
	 * "SCOOTER" and "Scooter" and the file should be placed at "scooter", the
	 * one will be taken with the most files in it. If it cannot be determined,
	 * the first one of the file listing.<br>
	 * <br>
	 * <b>Consider:</b> For now it is activated by default, to disburden the
	 * user.
	 */
	private boolean searchSimiliarDirectories = true;

	/**
	 * This field stores the selected files, which should be processed.
	 */
	private final File[] selectedFiles;

	/**
	 * The set of transformations which will be applied on tag values.
	 */
	private final TransformSet transformationSet;

	/**
	 * Creates an instance.
	 * 
	 * @param selection
	 *            the selected files or directories which should be processed.
	 * @param dirPattern
	 *            Pattern instance for determining directories, may be null.
	 * @param pattern
	 *            pattern for determining the filename, may be null.
	 * @param set
	 *            The set of transformations which should be applied to all tag
	 *            values. It may be <code>null</code>.
	 */
	public RenameConfiguration(File[] selection, DirectoryPattern dirPattern,
			FilePattern pattern, TransformSet set) {
		assert selection != null;
		this.listener = new ArrayList();
		this.directoryPattern = dirPattern;
		this.filePattern = pattern;
		this.selectedFiles = selection;
		this.transformationSet = set;
	}

	/**
	 * Adds a listener which will recieve notifications on changed settings.
	 * 
	 * @param changeListener
	 *            Listener to add.
	 */
	public synchronized void addConfigurationChangeListener(
			ConfigurationChangeListener changeListener) {
		if (!this.listener.contains(changeListener)) {
			this.listener.add(changeListener);
		}
	}

	/**
	 * This method notifies all registered listeners about a configuration
	 * change.<br>
	 * Call this method after you've changed some options.
	 */
	public synchronized void fireConfigChange() {
		Iterator it = this.listener.iterator();
		while (it.hasNext()) {
			((ConfigurationChangeListener) it.next()).optionHasChanged();
		}
	}

	/**
	 * @return Returns the directoryPattern.
	 */
	public DirectoryPattern getDirectoryPattern() {
		return directoryPattern;
	}

	/**
	 * @return Returns the filePattern.
	 */
	public FilePattern getFilePattern() {
		return filePattern;
	}

	/**
	 * Returns the selection of the current configuration.<br>
	 * 
	 * @return The selected files and directories.
	 */
	public File[] getSelection() {
		return this.selectedFiles;
	}

	/**
	 * @return Returns the transformationSet.
	 */
	public TransformSet getTransformationSet() {
		return this.transformationSet;
	}

	/**
	 * @return Returns the copyUnmodifiableFiles.
	 */
	public boolean isCopyUnmodifiableFiles() {
		return this.copyUnmodifiableFiles;
	}

	/**
	 * @return Returns the extendTrackNumbers.
	 */
	public boolean isExtendTrackNumbers() {
		return this.extendTrackNumbers;
	}

	/**
	 * For more information on how this result whould be interpreted see
	 * {@link #searchSimiliarDirectories}.
	 * 
	 * @return Returns the searchSimiliarDirectories.
	 */
	public boolean isSearchSimiliarDirectoriesEnabled() {
		return this.searchSimiliarDirectories;
	}

	/**
	 * This method removes the given listener.
	 * 
	 * @param changeListener
	 *            Listener to remove.
	 */
	public synchronized void removeConfigurationChangeListener(
			ConfigurationChangeListener changeListener) {
		this.listener.remove(changeListener);
	}

	/**
	 * @param value
	 *            The copyUnmodifiableFiles to set.
	 */
	public synchronized void setCopyUnmodifiableFiles(boolean value) {
		this.copyUnmodifiableFiles = value;
	}

	/**
	 * @param extendTrackNumbers
	 *            The extendTrackNumbers to set.
	 */
	public synchronized void setExtendTrackNumbers(boolean extendTrackNumbers) {
		this.extendTrackNumbers = extendTrackNumbers;
	}
}
