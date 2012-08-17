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
package entagged.tageditor.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileFilter;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.audioformats.exceptions.CannotWriteException;
import entagged.tageditor.FileWarningDialog;
import entagged.tageditor.ProgressDialog;
import entagged.tageditor.TagEditorFrame;
import entagged.tageditor.exceptions.OperationException;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.util.MultipleFieldsMergingTable;

public abstract class ControlPanelButtonListener implements ActionListener,
		Runnable {

	private static AudioFileFilter aff = new AudioFileFilter();

	// Max number of files to process, if the number of files is greater, show a
	// confirmation dialog
	private static final int MAX_FILES = 30;

	/**
	 * This field indicates, that the user aborted a batch operation.
	 */
	private boolean abort = false;

	protected MultipleFieldsMergingTable audioFiles;

	/**
	 * This field will be set by {@link #addException(List, File, Exception)}.
	 * If <code>true</code>, all errors won't result in an error message.<br>
	 */
	private boolean ignoreAll = false;

	/**
	 * This field stores the interface to the user.
	 */
	private ProgressDialog progressDialog;

	protected TagEditorFrame tagEditorFrame;

	public ControlPanelButtonListener(TagEditorFrame tagEditorFrame,
			MultipleFieldsMergingTable audioFiles) {
		this.audioFiles = audioFiles;
		this.tagEditorFrame = tagEditorFrame;
	}

	public void actionPerformed(ActionEvent ev) {
		// Reset procesing state.
		this.abort = false;
		this.ignoreAll = false;
		getProgressDialog().setAborted(false);
		getProgressDialog().setAbortable(true);
		getProgressDialog().setFinished(false);
		/*
		 * Start thread to exit eventqueue.
		 */
		new Thread(this, "Perform batch.").start();

		getProgressDialog().setVisible(true);
	}

	protected void addException(List l, File f, Exception e) {
		l.add(f);
		l.add(e);
		if (!ignoreAll) {
			int selected = JOptionPane
					.showConfirmDialog(
							tagEditorFrame,
							e.getMessage()
									+ "\n Would you like to ignore all errors and continue ? \n"
									+ "(Press cancel to abort the operation.",
							"Error occured", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.ERROR_MESSAGE);
			ignoreAll = selected == JOptionPane.YES_OPTION;
			abort = selected == JOptionPane.CANCEL_OPTION;
		}
	}

	protected abstract void audioFileAction(AudioFile af)
			throws CannotReadException, CannotWriteException,
			OperationException;

	protected int countFiles(File f) {
		this.abort = getProgressDialog().isAborted();
		if (f.isDirectory()) {
			int count = 0;
			File[] files = f.listFiles(aff);
			for (int i = 0; i < files.length; i++)
				count += countFiles(files[i]);
			return count;
		} else
			return 1;
	}

	protected abstract void finalizeAction();

	/**
	 * This method creates and displays the progressdialog for reflecting
	 * current progress and allow the user to abort.
	 * 
	 * @return a progress dialog.
	 */
	protected ProgressDialog getProgressDialog() {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(tagEditorFrame,
					"Progress", "Starting");
			this.progressDialog.setAbortable(true);
			this.progressDialog.setModal(true);
		}
		return this.progressDialog;
	}

	protected String isDataValid() {
		return "";
	}

	protected abstract void prepareAction();

	protected List recursiveAction(File f) {
		this.abort = getProgressDialog().isAborted();
		List errorFiles = new LinkedList();
		if (f.isDirectory()) {
			File[] files = f.listFiles(aff);
			for (int i = 0; i < files.length && !this.abort; i++)
				errorFiles.addAll(recursiveAction(files[i]));
		} else if (!abort) {
			getProgressDialog().setDescription("Processing " + f.getName());
			if (f instanceof AudioFile) {
				try {
					audioFileAction((AudioFile) f);
				} catch (CannotReadException e) {
					addException(errorFiles, f, e);
				} catch (CannotWriteException e) {
					addException(errorFiles, f, e);
				} catch (OperationException e) {
					addException(errorFiles, f, e);
				}
			} else {
				try {
					errorFiles.addAll(recursiveAction(AudioFileIO.read(f)));
				} catch (CannotReadException e) {
					addException(errorFiles, f, e);
				}
			}
		}
		return errorFiles;
	}

	/**
	 * Executes what actionPerformed did.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// Count the number of files
		getProgressDialog().setDescription("Counting files");
		
		ArrayList fileCopy = new ArrayList(this.audioFiles.getAudioFiles());
		Iterator it = fileCopy.iterator();
		int fileNb = 0;
		while (it.hasNext() && !abort)
			fileNb += countFiles((File) it.next());
		
		System.out.println("Number of files to be processed: " + fileNb);
		if (fileNb >= MAX_FILES) {
			int res = JOptionPane.showConfirmDialog(
						tagEditorFrame,
						LangageManager.getProperty("tageditorframe.numberoffileswarning.text").replaceAll("%1", fileNb + ""),
						LangageManager.getProperty("tageditorframe.numberoffileswarning.title"),
						JOptionPane.YES_NO_OPTION);
			abort = res == JOptionPane.NO_OPTION;
		}

		tagEditorFrame.getEditorSettings().prepareAudioProcessing();

		// Process the files
		it = fileCopy.iterator();
		prepareAction();

		String error = isDataValid();
		if (!"".equals(error)) {
			JOptionPane.showMessageDialog(tagEditorFrame, error,
											LangageManager.getProperty("common.dialog.error"),
											JOptionPane.ERROR_MESSAGE);
			abort = true;
		}

		getProgressDialog().setTitle("Performing changes");
		List errorFiles = new LinkedList();
		while (it.hasNext() && !abort) {
			errorFiles.addAll(recursiveAction((File) it.next()));
		}

		finalizeAction();

		tagEditorFrame.refreshCurrentTableView();

		if (errorFiles.size() != 0) {
			new FileWarningDialog(tagEditorFrame, errorFiles);
			getProgressDialog().dispose();
		}
		else
			getProgressDialog().dispose();
	}
}
