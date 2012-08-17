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
package entagged.tageditor.tools;

import java.io.File;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.ModifyVetoException;
import entagged.audioformats.generic.AudioFileModificationAdapter;
import entagged.audioformats.generic.AudioFileModificationListener;
import entagged.tageditor.resources.PreferencesManager;

/**
 * This class is meant for preserving the file modification time.<br>
 * 
 * @author Christian Laireiter (liree@web.de)
 */
public class FileTimePreserver extends AudioFileModificationAdapter {

	/**
	 * This field is meant to store a static instance for the static instance of
	 * {@link entagged.audioformats.AudioFileIO} (which can be retrieved by
	 * {@link entagged.audioformats.AudioFileIO#getDefaultAudioFileIO()}).
	 */
	private static FileTimePreserver defaultPreserver = null;

	/**
	 * Returns <code>true</code> if the file time preservation is activated in
	 * entagged.
	 * 
	 * @return <code>true</code> if this class should be used.
	 */
	public static boolean isOptionActive() {
		return PreferencesManager.getBoolean("entagged.preservefiletime");
	}

	/**
	 * Registers or deregisters a static instance of this class to the default
	 * isntance of {@link entagged.audioformats.AudioFileIO}, if
	 * {@link #isOptionActive()} returns <code>true</code>.<br>
	 * If <code>active</code> is <code>true</code> but the option is
	 * deactivated, the instance will be deregistered as well.
	 * 
	 * @param active
	 *            <code>true</code> to register, false to deregister.
	 */
	public static void register(boolean active) {
		if (active && isOptionActive()) {
			if (defaultPreserver == null) {
				defaultPreserver = new FileTimePreserver();
			}
			AudioFileIO.getDefaultAudioFileIO()
					.addAudioFileModificationListener(defaultPreserver);
		} else {
			if (defaultPreserver != null) {
				AudioFileIO.getDefaultAudioFileIO()
						.removeAudioFileModificationListener(defaultPreserver);
			}
		}
	}

	/**
	 * This method sets file time preservation for entagged.
	 * 
	 * @param active
	 *            <code>true</code> if filetime sould be preserved.
	 */
	public static void setOptionActive(boolean active) {
		PreferencesManager.putBoolean("entagged.preservefiletime", active);
	}

	/**
	 * Stores the file modification time whent
	 * {@link #fileWillBeModified(AudioFile, boolean)} is called.<br>
	 * Will be set to &quote;-1&quote; if a veto occures, so that nothing
	 * happens if process is finished.<br>
	 */
	private long lastFileTime = -1;

	/**
	 * (overridden)
	 * 
	 * @see entagged.audioformats.generic.AudioFileModificationAdapter#fileOperationFinished(java.io.File)
	 */
	public void fileOperationFinished(File result) {
		if (this.lastFileTime != -1) {
			result.setLastModified(this.lastFileTime);
			this.lastFileTime = -1;
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.audioformats.generic.AudioFileModificationAdapter#fileWillBeModified(entagged.audioformats.AudioFile,
	 *      boolean)
	 */
	public void fileWillBeModified(AudioFile file, boolean delete)
			throws ModifyVetoException {
		lastFileTime = file.lastModified();
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.audioformats.generic.AudioFileModificationAdapter#vetoThrown(entagged.audioformats.generic.AudioFileModificationListener,
	 *      entagged.audioformats.AudioFile,
	 *      entagged.audioformats.exceptions.ModifyVetoException)
	 */
	public void vetoThrown(AudioFileModificationListener cause,
			AudioFile original, ModifyVetoException veto) {
		lastFileTime = -1;
	}
}
