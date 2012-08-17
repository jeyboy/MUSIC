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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileFilter;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.mp3.Id3v2Tag;
import entagged.listing.gui.ListingProgressListener;
import entagged.tageditor.exceptions.OperationException;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;

/**
 * This class allows enumerating track number of multiply directories and audio
 * files. <br>
 * The order of the given selection will be used to determine the track number
 * given to each audio file. <br>
 * If an error occurs while tagging, the number will be skipped, so the user may
 * fix the problem and assigns the tracknumber himself later.
 * 
 * @author Christian Laireiter
 */
public class TrackEnumerator {

    /**
     * This class should sort an array of {@link File}objects. <br>
     * First criteria wheter it is a directory. <br>
     * Second the name. where each found number is extended to match the length
     * of 20 digits. Hope this is enough.
     */
    private final class FileSorter implements Comparator {
        /**
         * (overridden)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
            int result = 0;
            if (o1 instanceof File && o2 instanceof File) {
                File f1 = (File) o1;
                File f2 = (File) o2;
                if (f1.isDirectory() != f2.isDirectory()) {
                    if (f1.isDirectory())
                        result = -1;
                    else
                        result = 1;
                }
                if (result == 0) {
                    if (getSettings().isNumberExtension()) {
                        result = extendNumbers(f1.getName()).compareTo(
                                extendNumbers(f2.getName()));
                    } else {
                        result = f1.getName().compareTo(f2.getName());
                    }
                }
            }
            return result;
        }

        /**
         * This method searches for numbers in the string an extends them with
         * leading zeros.
         * 
         * @param original
         *                  String whose number should be extended.
         * @return result
         */
        public String extendNumbers(String original) {
            StringBuffer result = new StringBuffer();
            int pos = 0;
            StringBuffer tmp = new StringBuffer();
            while (pos < original.length()) {
                char curr = original.charAt(pos);
                if (!Character.isDigit(curr)) {
                    if (tmp.length() > 0) {
                        result.append(fillLeadingZero(tmp.toString(), 50));
                        tmp.delete(0, tmp.length());
                    }
                    result.append(curr);
                } else {
                    tmp.append(curr);
                }
                pos++;
            }
            if (tmp.length() > 0) {
                result.append(fillLeadingZero(tmp.toString(), 50));
            }
            return result.toString();
        }
    }

    /**
     * This class will forward each request to the
     * {@link ListingProgressListener}in the local
     * {@link TrackEnumeratorSettings}if its not <code>null</code>.<br>
     */
    private final class ProgressListener implements ListingProgressListener {

        /**
         * (overridden)
         * 
         * @see entagged.listing.gui.ListingProgressListener#abort()
         */
        public boolean abort() {
            if (getSettings().getProgressListener() != null)
                return getSettings().getProgressListener().abort();
            // Else run to end.
            return false;
        }

        /**
         * (overridden)
         * 
         * @see entagged.listing.gui.ListingProgressListener#directoryChanged(int,
         *           java.lang.String)
         */
        public void directoryChanged(int fileCount, String name) {
            if (getSettings().getProgressListener() != null) {
                getSettings().getProgressListener().directoryChanged(fileCount,
                        name);
            } else {
                System.out.println("Processing directory \"" + name + "\"");
            }
        }

        /**
         * (overridden)
         * 
         * @see entagged.listing.gui.ListingProgressListener#errorOccured(java.lang.String)
         */
        public void errorOccured(String errorDescription) {
            if (getSettings().getProgressListener() != null) {
                getSettings().getProgressListener().errorOccured(
                        errorDescription);
            } else {
                System.err.println(errorDescription);
            }
            errors = true;
        }

        /**
         * (overridden)
         * 
         * @see entagged.listing.gui.ListingProgressListener#fileChanged(int,
         *           java.lang.String)
         */
        public void fileChanged(int fileNum, String name) {
            if (getSettings().getProgressListener() != null) {
                getSettings().getProgressListener().fileChanged(fileNum, name);
            } else {
                System.out.println("File changed \"" + name + "\"");
            }
        }

        /**
         * (overridden)
         * 
         * @see entagged.listing.gui.ListingProgressListener#processingFinished()
         */
        public void processingFinished() {
            if (getSettings().getProgressListener() != null) {
                getSettings().getProgressListener().processingFinished();
            } else {
                System.out.println("Finished!");
            }
        }

    }

    /**
     * This field indicates wheter the process method is counting.
     */
    private boolean counting = false;

    /**
     * This field indicates that there has been at least one error.
     */
    protected boolean errors = false;

    /**
     * Will print progress to system out or if settings contains a valid
     * {@link ListingProgressListener}it will forward the calls.
     */
    private ProgressListener progressListener;

    /**
     * Stores the current TrackEnumeratorSettings.
     */
    private TrackEnumeratorSettings settings;

    /**
     * Stores the sorter.
     */
    private final FileSorter sorter;

    /**
     * Creates an instance.
     */
    public TrackEnumerator() {
        this.settings = new TrackEnumeratorSettings();
        this.progressListener = new ProgressListener();
        this.sorter = new FileSorter();
    }

    /**
     * This method will insert zeros to the <code>trackValue</code> until its
     * character count matches <code>digitCount</code>.
     * 
     * @param trackValue
     *                  String which should be adjusted.
     * @param digitCount
     *                  Number of digits required.
     * @return trackValue with leading zeros.
     */
    protected String fillLeadingZero(String trackValue, int digitCount) {
        if (trackValue.length() >= digitCount) {
            return trackValue;
        }
        StringBuffer tmp = new StringBuffer(trackValue);
        while (tmp.length() < digitCount) {
            tmp.insert(0, "0");
        }
        return tmp.toString();
    }

    /**
     * Returns the settings object. <br>
     * 
     * @return TrackEnumeratorSettings
     */
    public synchronized TrackEnumeratorSettings getSettings() {
        return this.settings;
    }

    /**
     * This method creates the track number string out of given parameters. <br>
     * If <code>trackNum</code> doesn't fill the number of
     * <code>digitCount</code>, there will be leading zeros applied. <br>
     * This will also happen if <code>total</code> is greater and
     * <code>dynamicExtend</code> is set.
     * 
     * @param trackNum
     *                  The number of which a string should be generated.
     * @param total
     *                  The total amount of files (Valid value is needed when enabling
     *                  <code>dynamicExtend</code>.
     * @param digitCount
     *                  The amount on digits which should be used.
     * @param dynamicExtend
     *                  <code>true</code> if the <code>digitCount</code> should be
     *                  adjusted when needed.
     * @return The string for the tag track.
     */
    private String getTrackValue(int trackNum, int total, int digitCount,
            boolean dynamicExtend) {
        String trackValue = String.valueOf(trackNum);
        String totalValue = String.valueOf(total);
        if (dynamicExtend) {
            if (totalValue.length() > digitCount) {
                digitCount = totalValue.length();
            }
            if (trackValue.length() > digitCount) {
                digitCount = trackValue.length();
            }
        }
        StringBuffer result = new StringBuffer(fillLeadingZero(trackValue,
                digitCount));
        String ofValue = getSettings().getOfString();
        if (ofValue != null) {
            if (ofValue.indexOf("<n>") != -1) {
                ofValue = ofValue.replaceAll("<n>", String.valueOf(total));
            } else {
                ofValue += String.valueOf(total);
            }
            result.append(ofValue);
        }
        return result.toString();
    }

    /**
     * This method will start the processing of the selection. <br>
     * 
     * @param preview
     *                  <code>true</code> if no really change should be made. This
     *                  could help counting (determining file count) and due to
     *                  {@link ListingProgressListener}create a preview of the
     *                  processing.
     * @return the number of processed audio files
     * @throws OperationException
     *                   If Errors occur.
     */
    public synchronized int process(boolean preview) throws OperationException {
        Iterator iterator = getSettings().getSelection().iterator();
        errors = false;
        int processedCount = 0;
        int totalCount = 0;
        if ((getSettings().isOfEnabled()
                || getSettings().isDynamicDigitExtend() || getSettings()
                .isSaveRun())
                && !counting) {
            counting = true;
            totalCount = process(true);
            counting = false;
        }
        if (getSettings().isSaveRun() && errors) {
            return -1;
        }
        while (iterator.hasNext() && !progressListener.abort()) {
            File current = (File) iterator.next();
            if (!current.canRead()) {
                progressListener.errorOccured(LangageManager
                        .getProperty("trackenum.processor.readaccesserror")
                        + "\"" + current.getAbsolutePath() + "\"");
                continue;
            }
            if (current.isDirectory()) {
                processedCount += processDirectory(current, processedCount,
                        totalCount, preview);
            } else {
                if (processFile(current, processedCount + 1, totalCount,
                        preview)) {
                    processedCount++;
                }
            }
        }
        if (!counting) {
            progressListener.processingFinished();
        }
        return processedCount;
    }

    /**
     * This method will enumerate the contents of this directory.
     * 
     * @param current
     *                  Directory to process.
     * @param start
     *                  The tracknumber of the next processed file.
     * @param total
     *                  the amount of files that will be processed.
     * @param preview
     *                  if <code>true</code> no changes will be performed. <br>
     * @return The number of affected files.
     */
    private int processDirectory(File current, int start, int total,
            boolean preview) {
        File[] content = current.listFiles(new AudioFileFilter());
        Arrays.sort(content, this.sorter);
        if (getSettings().isDirectoryRestart()) {
            start = 0;
            total = 0;
            for (int i = 0; i < content.length && !progressListener.abort(); i++) {
                if (content[i].isFile()) {
                    total++;
                }
            }
        }
        progressListener.directoryChanged(content.length, current
                .getAbsolutePath());
        ArrayList directories = new ArrayList();
        int result = 0;
        int filesProcessed = 0;
        for (int i = 0; i < content.length && !progressListener.abort(); i++) {
            if (content[i].isDirectory()) {
                if (getSettings().isFilesFirst()) {
                    directories.add(content[i]);
                } else {
                    result += processDirectory(content[i], result + start,
                            total, preview);
                }
            } else {
                int toSet = getSettings().isDirectoryRestart() ? filesProcessed
                        : start + result;
                processFile(content[i], toSet + 1, total, preview);
                result++;
                filesProcessed++;
            }
        }
        Iterator it = directories.iterator();
        while (it.hasNext() && !progressListener.abort()) {
            result += processDirectory((File) it.next(), result + start, total,
                    preview);
        }
        return result;
    }

    /**
     * This method will set the track of the current file.
     * 
     * @param current
     *                  Directory to process.
     * @param trackNum
     *                  the track number to assign
     * @param total
     *                  The total amount of processing files. Valid values are only
     *                  needed if the {@link TrackEnumeratorSettings#isOfEnabled()}
     *                  method of {@link #settings}returns <code>true</code>
     * @param preview
     *                  if <code>true</code> no changes will be performed. <br>
     * @return The number of affected files.
     */
    private boolean processFile(File current, int trackNum, int total,
            boolean preview) {
        boolean result = false;
        try {
            AudioFile file = AudioFileIO.read(current);
            if (file != null && file.getTag() != null) {
                String trackValue = getTrackValue(trackNum, total,
                        getSettings().getDigitCount(), getSettings()
                                .isDynamicDigitExtend());
                file.getTag().setTrack(trackValue);
                if (!preview) {
                    if(file.getTag() instanceof Id3v2Tag)
                        file.getTag().setEncoding(PreferencesManager.get("entagged.tag.encoding"));
                    
                    AudioFileIO.write(file);
                }
                if (!counting) {
                    progressListener.fileChanged(trackNum, current.getName()
                            + " --> " + trackValue);
                }
                result = true;
            } else {
                progressListener
                        .errorOccured(LangageManager
                                .getProperty("trackenum.processor.fileerror")+" "
                                + current.getAbsolutePath());
            }
        } catch (Exception e) {
            progressListener.errorOccured(e.getMessage());
        }
        return result;
    }
}