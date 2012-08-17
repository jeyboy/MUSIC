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
package entagged.listing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileFilter;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.listing.gui.ListingProgressListener;
import entagged.listing.separated.SeparatedListingCreator;
import entagged.listing.xml.XmlCreator;

/**
 * This class performs a recursive processing of audio files. <br>
 * However a further implementation for reports is present.
 * 
 * @author Christian Laireiter
 */
public class ListingProcessor implements Runnable {

	/**
	 * File filter for accepting only supported file extensions.
	 */
	private static final AudioFileFilter aff = new AudioFileFilter();

	/**
	 * Indicates that a transformed report should be generated.
	 */
	public final static int REPORT_OTHER = 4;

	/**
	 * Indicates that a tsv (tab seperated values) report should be generated.
	 */
	public final static int REPORT_TSV = 1;

	/**
	 * Indicates that a xml report should be generated
	 */
	public final static int REPORT_XML = 2;

	/**
	 * if the {@link Lister#addFile(AudioFile, String)} or
	 * {@link Lister#addFile(String)} method throwed an exception, it is stored
	 * here.<br>
	 * If not <code>null</code>, it will stop processing.
	 */
	private Exception lastSeriouseException = null;

	/**
	 * This field stores the lister used to create the listing. <br>
	 */
	private Lister lister;

	/**
	 * This field stores an optional listener.
	 */
	private ListingProgressListener lpl = null;

	/**
	 * Indicates whether {@link #selectedFiles}should be processed recursively.
	 */
	private boolean processRecursive;

	/**
	 * Directory which will be processed (read).
	 */
	private File[] selectedFiles;

	/**
	 * File which will be written to.
	 */
	private File targetFile;

	/**
	 * This field indicates whether to print the processing steps to Standard
	 * out or not.
	 */
	private boolean verbose = false;

	/**
	 * Creates an instance.
	 * 
	 * @param source
	 *            Directory of which to list files
	 * @param recursive
	 *            handle source recursive
	 * @param target
	 *            where to write the report. May be <code>null</code>.
	 * @param targetType
	 *            one of the REPORT_ constants. Note if you choose REPORT_OTHER
	 *            there will be no writing to the specified target.
	 */
	public ListingProcessor(File[] source, boolean recursive, File target,
			int targetType) {
		if (source == null || target == null) {
			throw new IllegalArgumentException("Arguments must not be null.");
		}
		if ((targetType & REPORT_TSV) != 0) {
			this.lister = new SeparatedListingCreator();
		} else if ((targetType & REPORT_XML) != 0) {
			this.lister = new XmlCreator();
		} else if ((targetType & REPORT_OTHER) != 0) {
			this.lister = new XmlCreator();
			// NO writing.
			this.targetFile = null;
		} else {
			throw new IllegalArgumentException(
					"targettype has incorrect value.");
		}
		this.selectedFiles = source;
		this.targetFile = target;
		this.processRecursive = recursive;
	}

	/**
	 * Creates an instance which uses the given lister to create the listing.
	 * <br>
	 * Recursive processing will be done.
	 * 
	 * @param listingCreator
	 *            used listing creator.
	 * @param source
	 *            The directory to list.
	 */
	public ListingProcessor(Lister listingCreator, File source[]) {
		this.lister = listingCreator;
		this.selectedFiles = source;
		this.processRecursive = true;
		this.targetFile = null;
	}

	/**
	 * @return Returns the lastSeriouseException.
	 */
	public Exception getLastSeriouseException() {
		return this.lastSeriouseException;
	}

	/**
	 * @return Returns the lister.
	 */
	public Lister getLister() {
		return lister;
	}

	/**
	 * @return Returns the lpl.
	 */
	public ListingProgressListener getListingProgressListener() {
		return lpl;
	}

	/**
	 * This method will try to read a {@link entagged.audioformats.Tag}out of
	 * the given file and appends it to {@link #lister}.<br>
	 * 
	 * @param file
	 *            File to list.
	 * @param pathPrefix
	 *            If given (not <code>null</code>), the length of this string
	 *            will be cur from the beginning of the path of the given file.
	 */
	private void handleFile(File file, String pathPrefix) {
		String relativePath = file.getAbsolutePath();
		if (pathPrefix != null) {
			relativePath = "." + relativePath.substring(pathPrefix.length());
		}
		try {
			if (verbose) {
				System.out.print("Reading file \"" + file + "\"...");
			}

			AudioFile audioFile = AudioFileIO.read(file);

			lister.addFile(audioFile, pathPrefix);
			if (verbose)
				System.out.println("OK");
		} catch (CannotReadException cre) {
			try {
				lister.addFile(file.getAbsolutePath());
				if (verbose) {
					System.out.println("ERROR (" + cre.getMessage() + ")");
				} else {
					System.out.println("Error reading file \"" + file
							+ "\". Reason: " + cre.getMessage());
				}
				if (lpl != null) {
					lpl.errorOccured("Error reading file \"" + file
							+ "\". Reason: " + cre.getMessage());
				}
			} catch (Exception e) {
				lastSeriouseException = e;
			}
		} catch (Exception e) {
			lastSeriouseException = e;
		}
	}

	/**
	 * @return Returns the verbose.
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * This method reads the contents of the given directory and appends the
	 * tags of audio files to {@link #lister}.<br>
	 * If {@link #processRecursive}is <code>true</code> this method calls
	 * itself on each subdirectory.
	 * 
	 * @param directory
	 *            Directory whose contents to list.
	 * @throws Exception
	 *             any
	 */
	private void processDirectory(File directory) throws Exception {
		if (verbose) {
			System.out.println("Entering \"" + directory + "\"");
		}
		File[] files = directory.listFiles(aff);
		if (lpl != null) {
			lpl.directoryChanged(files.length, directory.getAbsolutePath());
		}
		for (int i = 0; i < files.length && lastSeriouseException == null; i++) {
			if (lpl != null && lpl.abort()) {
				break;
			}
			if (files[i].isDirectory() && processRecursive) {
				processDirectory(files[i]);
			} else if (files[i].isFile()) {
				if (lpl != null) {
					lpl.fileChanged(i, files[i].getName());
				}
				handleFile(files[i], directory.getAbsolutePath());
			}
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			String report = null;
			for (int i = 0; i < selectedFiles.length
					&& lastSeriouseException == null; i++) {
				if (selectedFiles[i] != null) {
					if (!selectedFiles[i].exists()) {
						lastSeriouseException = new Exception(
								"The selected file "
										+ selectedFiles[i].getAbsolutePath()
										+ " doesn't exist.");
						continue;
					}
					if (selectedFiles[i].isDirectory())
						processDirectory(selectedFiles[i]);
					else {
						if (lpl != null) {
							lpl.directoryChanged(1, selectedFiles[i]
									.getParentFile().getAbsolutePath());
							lpl.fileChanged(0, selectedFiles[i].getName());
						}
						handleFile(selectedFiles[i], null);
					}
				}
			}
			if (lastSeriouseException == null) {
				lister.close();
				report = lister.getContent();
			}
			if (lpl != null) {
				lpl.processingFinished();
			}
			writeReport(report);
		} catch (Exception e) {
			e.printStackTrace();
			this.lastSeriouseException = e;
		}
	}

	/**
	 * @param list
	 *            The lister to set.
	 */
	public void setLister(Lister list) {
		this.lister = list;
	}

	/**
	 * @param listener
	 *            The lpl to set.
	 */
	public void setListingProgressListener(ListingProgressListener listener) {
		this.lpl = listener;
	}

	/**
	 * @param value
	 *            The verbose to set.
	 */
	public void setVerbose(boolean value) {
		this.verbose = value;
	}

	/**
	 * This method starts the report generation, and returns the thread its
	 * running in.
	 * 
	 * @return the thread the processing is done in.
	 */
	public Thread start() {
		Thread result = new Thread(this, "Report creation: ");
		result.start();
		return result;
	}

	/**
	 * This method will write the given string to {@link #targetFile}if its not
	 * <code>null</code>.<br>
	 * 
	 * @param report
	 *            String to write.
	 * @throws IOException
	 *             write errors.
	 */
	private void writeReport(String report) throws IOException {
		if (this.targetFile != null) {
			FileWriter fw = new FileWriter(this.targetFile);
			fw.write(report);
			fw.flush();
			fw.close();
		}
	}
}