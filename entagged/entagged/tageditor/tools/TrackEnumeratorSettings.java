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
import java.util.Collection;

import entagged.listing.gui.ListingProgressListener;
import entagged.tageditor.resources.PreferencesManager;

/**
 * This class is meant to store all processing settings for
 * {@link entagged.tageditor.tools.TrackEnumerator}
 * 
 * @author Christian Laireiter
 */
public final class TrackEnumeratorSettings {

    /**
     * This constant stores the property name for the {@link PreferencesManager},
     * for the attribute {@link #digitCount}.<br>
     */
    public final static String PREF_DIGIT_COUNT = TrackEnumeratorSettings.class
            .getName()
            + "_DIGIT_COUNT";

    /**
     * This constant stores the property name for the {@link PreferencesManager},
     * for the attribute {@link #directoryRestart}.<br>
     */
    public final static String PREF_DIRECTORY_RESTART = TrackEnumeratorSettings.class
            .getName()
            + "_DIRECTORY_RESTART";

    /**
     * This constant stores the property name for the {@link PreferencesManager},
     * for the attribute {@link #dynamicDigitExtend}.<br>
     */
    public final static String PREF_DYNAMIC_EXTEND = TrackEnumeratorSettings.class
            .getName()
            + "_DYNAMIC_EXTEND";

    /**
     * This constant stores the property name for the {@link PreferencesManager},
     * for the attribute {@link #filesFirst}.<br>
     */
    public final static String PREF_FILES_FIRST = TrackEnumeratorSettings.class
            .getName()
            + "_FILES_FIRST";

    /**
     * This constant stores the property name for the {@link PreferencesManager},
     * for the attribute {@link #numberExtension}.<br>
     */
    public final static String PREF_NUMBER_EXTENSION = TrackEnumeratorSettings.class
            .getName()
            + "_NUMBER_EXTENSION";

    /**
     * This constant stores the property name for the {@link PreferencesManager},
     * for the attribute {@link #ofString}.<br>
     */
    public final static String PREF_OF_STRING = TrackEnumeratorSettings.class
            .getName()
            + "_OF_STRING";

    /**
     * This constant stores the property name for the {@link PreferencesManager},
     * for the attribute {@link #saveRun}.<br>
     */
    public final static String PREF_SAVE_RUN = TrackEnumeratorSettings.class
            .getName()
            + "_OF_STRING";

    /**
     * This method will set the number of digits to be used when enumerating.
     * This will affect the leading zeros.
     */
    private int digitCount = 2;

    /**
     * This field affects the numbering for directories. <br>
     * When this option is set the processing will restart with the numbering
     * for each directory.
     */
    private boolean directoryRestart = false;

    /**
     * If {@link #digitCount}doesn't suffice the amount of music files, with
     * this option the extension could be activated. <br>
     */
    private boolean dynamicDigitExtend = false;

    /**
     * if {@link #directoryRestart}is false and all files will be enumerated in
     * order of the selection and recursive it might be wanted to first
     * enumerate the files of a directory before a subdirectory is processed.
     */
    private boolean filesFirst = true;

    /**
     * For each directory its content is sorted, so that the directories comes
     * first. The files are sorted by their names. If this field is
     * <code>true</code> Each number in the filename is extended by leading
     * zeros to match a length of 50 digits. With this there should be no
     * lexicalic problems. (Think of filesnames like 1.mp3, 10.mp3 2.mp3)
     */
    private boolean numberExtension = false;

    /**
     * This field stores the "of" String. <br>
     * Its default value will cause each track string to look like "02 / 20".
     * <br>
     * For the total coutn "&lt;n&gt;" is used. <br>
     * If the field is <code>null</code> this functionality is disabled.
     */
    private String ofString = " / <n>";

    /**
     * This field is used to reflect the state of the processing. <br>
     * If this field is <code>null</code> it simply won't be used.
     */
    private ListingProgressListener progressListener = null;

    /**
     * If true, the processor will first test if he can do all requested
     * operation except writing. <br>
     * Writing errors cannot be determinded.
     */
    private boolean saveRun = false;

    /**
     * This field stores the selection. Those files and directories will be
     * enumerated.
     */
    private ArrayList selection = new ArrayList();

    /**
     * Creates an instance and triggers loading the defaults from
     * {@link entagged.tageditor.resources.PreferencesManager}<br>
     */
    public TrackEnumeratorSettings() {
        loadDefaults();
    }

    /**
     * Adds the given files to the processing list.
     * 
     * @param files
     *                  Files to add.
     */
    public void addFiles(File[] files) {
        this.selection.addAll(Arrays.asList(files));
    }

    /**
     * @return Returns the digitCount.
     */
    public int getDigitCount() {
        return digitCount;
    }

    /**
     * @return Returns the ofString.
     */
    public String getOfString() {
        return ofString;
    }

    /**
     * @return Returns the progressListener.
     */
    public ListingProgressListener getProgressListener() {
        return progressListener;
    }

    /**
     * Returns the selection. <br>
     * 
     * @return Collection with {@link File}objects.
     */
    public Collection getSelection() {
        return this.selection;
    }

    /**
     * @return Returns the directoryRestart.
     */
    public boolean isDirectoryRestart() {
        return directoryRestart;
    }

    /**
     * @return Returns the dynamicDigitExtend.
     */
    public boolean isDynamicDigitExtend() {
        return dynamicDigitExtend;
    }

    /**
     * @return Returns the filesFirst.
     */
    public boolean isFilesFirst() {
        return filesFirst;
    }

    /**
     * @return Returns the numberExtension.
     */
    public boolean isNumberExtension() {
        return numberExtension;
    }

    /**
     * Returns <code>true</code> if each track field will be like "2 / 20" or
     * something set with {@link #setOfString(String)}.<br>
     * 
     * @return <code>true</code> if "of"-functionality is activated.
     */
    public boolean isOfEnabled() {
        return this.ofString != null;
    }

    /**
     * @return Returns the saveRun.
     */
    public boolean isSaveRun() {
        return saveRun;
    }

    /**
     * This method will load some settings from the
     * {@link entagged.tageditor.resources.PreferencesManager}.<br>
     */
    public void loadDefaults() {
        String tmp = PreferencesManager.get(PREF_DIGIT_COUNT);
        if (tmp != null) {
            digitCount = Integer.parseInt(tmp);
        }
        tmp = PreferencesManager.get(PREF_DYNAMIC_EXTEND);
        if (tmp != null) {
            this.dynamicDigitExtend = PreferencesManager
                    .getBoolean(PREF_DYNAMIC_EXTEND);
        }
        tmp = PreferencesManager.get(PREF_DIRECTORY_RESTART);
        if (tmp != null) {
            this.directoryRestart = PreferencesManager
                    .getBoolean(PREF_DIRECTORY_RESTART);
        }
        tmp = PreferencesManager.get(PREF_FILES_FIRST);
        if (tmp != null) {
            this.filesFirst = PreferencesManager.getBoolean(PREF_FILES_FIRST);
        }
        tmp = PreferencesManager.get(PREF_NUMBER_EXTENSION);
        if (tmp != null) {
            this.numberExtension = PreferencesManager
                    .getBoolean(PREF_NUMBER_EXTENSION);
        }
        tmp = PreferencesManager.get(PREF_SAVE_RUN);
        if (tmp != null) {
            this.saveRun = PreferencesManager.getBoolean(PREF_SAVE_RUN);
        }
        this.ofString = PreferencesManager.get(PREF_OF_STRING);
        if (ofString != null && ofString.equals("null")) {
            this.ofString = null;
        }
    }

    /**
     * This method will save some settings using {@link PreferencesManager}.
     * <br>
     */
    public void saveToDefaults() {
        PreferencesManager.putInt(PREF_DIGIT_COUNT, this.digitCount);
        PreferencesManager.putBoolean(PREF_DYNAMIC_EXTEND,
                this.dynamicDigitExtend);
        PreferencesManager.putBoolean(PREF_FILES_FIRST, this.filesFirst);
        PreferencesManager.putBoolean(PREF_NUMBER_EXTENSION,
                this.numberExtension);
        PreferencesManager.putBoolean(PREF_SAVE_RUN, this.saveRun);
        PreferencesManager.putBoolean(PREF_DIRECTORY_RESTART,
                this.directoryRestart);
        if (ofString == null) {
            PreferencesManager.put(PREF_OF_STRING, "null");
        } else {
            PreferencesManager.put(PREF_OF_STRING, ofString);
        }
    }

    /**
     * @param num
     *                  The digitCount to set.
     */
    public void setDigitCount(int num) {
        this.digitCount = num;
    }

    /**
     * @param restart
     *                  The directoryRestart to set.
     */
    public void setDirectoryRestart(boolean restart) {
        this.directoryRestart = restart;
    }

    /**
     * @param value
     *                  The dynamicDigitExtend to set.
     */
    public void setDynamicDigitExtend(boolean value) {
        this.dynamicDigitExtend = value;
    }

    /**
     * This method sets the files to process by previously clearing old
     * selection.
     * 
     * @param toProcess
     *                  The files that should be altered.
     */
    public void setFiles(File[] toProcess) {
        this.selection.clear();
        this.addFiles(toProcess);
    }

    /**
     * @param value
     *                  The filesFirst to set.
     */
    public void setFilesFirst(boolean value) {
        this.filesFirst = value;
    }

    /**
     * @see #numberExtension
     * @param extension
     *                  The numberExtension to set.
     */
    public void setNumberExtension(boolean extension) {
        this.numberExtension = extension;
    }

    /**
     * This method will set the "Of"-Functionality to the given string. <br>
     * USE: <br>
     * Any string. But "&lt;n&gt;" will be replaced by the total amount. If
     * <code>newOfValue</code> doesn't contain it, the total amount will
     * simply be appended to the written track. <br>
     * If this field is set to <code>null</code>, the audio files will just
     * recieve the tracks number. <br>
     * 
     * @param newOfValue
     *                  new Of instruction.
     */
    public void setOfString(String newOfValue) {
        this.ofString = newOfValue;
    }

    /**
     * @param listener
     *                  The progressListener to set.
     */
    public void setProgressListener(ListingProgressListener listener) {
        this.progressListener = listener;
    }

    /**
     * @param value
     *                  The saveRun to set.
     */
    public void setSaveRun(boolean value) {
        this.saveRun = value;
    }
}