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
package entagged.listing.gui.tasks;

import java.util.Collection;
import java.util.Iterator;

import entagged.cli.XslTransformer;
import entagged.listing.ListingProcessor;
import entagged.listing.xml.TransformTarget;
import entagged.tageditor.resources.PreferencesManager;

/**
 * This structure stores properties for the report creation.
 * 
 * @author Christian Laireiter
 */
public class ReportConfig {

    /**
     * This constant stores the preference name for the recursive processing.
     */
    public static String PREF_RECURSIVE = ReportConfig.class.getName()
            + "_RECURSIVE";

    /**
     * This constant stores the preference name for the source directory.
     */
    public static String PREF_SOURCE = ReportConfig.class.getName() + "_SOURCE";

    /**
     * This constant stores the preference name for the target file.
     */
    public static String PREF_TARGET = ReportConfig.class.getName() + "_TARGET";

    /**
     * This constant stores the preference name for the tranformation target..
     */
    public static String PREF_TRANS_TARGET = ReportConfig.class.getName()
            + "_TRANS_TARGET";

    /**
     * This constant stores the preference name for the transformation type.
     */
    public static String PREF_TYPE = ReportConfig.class.getName() + "_TYPE";

    /**
     * This field stores the option whether to process {@link #sourceDirectory}
     * recursively or not.
     */
    private boolean recursive = true;

    /**
     * The path to the file which stores the report.
     */
    private String reportFile;

    /**
     * This field stores the direcotry path of the directory whose audio files
     * should be reported.
     */
    private String sourceDirectory;

    /**
     * Stores the Transformation target.
     */
    private TransformTarget transformTarget = null;

    /**
     * this bitmask stores all <code>REPORT_</code> values of the class
     * {@link entagged.listing.ListingProcessor}.<br>.
     */
    private int transformType;

    /**
     * Creates an instance and loads the last entered values.
     *  
     */
    public ReportConfig() {
        loadDefaults();
    }

    /**
     * @return Returns the reportFile.
     */
    public String getReportFile() {
        return reportFile;
    }

    /**
     * @return Returns the sourceDirectory.
     */
    public String getSourceDirectory() {
        return sourceDirectory;
    }

    /**
     * @return Returns the transformTarget.
     */
    public TransformTarget getTransformTarget() {
        return transformTarget;
    }

    /**
     * @return Returns the transformType.
     */
    public int getTransformType() {
        return transformType;
    }

    /**
     * @return Returns the recursive.
     */
    public boolean isRecursive() {
        return recursive;
    }

    /**
     * This method loads the last entered values using
     * {@link entagged.tageditor.resources.PreferencesManager}.<br>
     */
    public void loadDefaults() {
        try {
            //FIXME: he getBoolean reports a nullpointer sometimes
            //Need to fix this, don't know how, maybe set default values in the
            //default entagged.preferences file ?
            sourceDirectory = PreferencesManager.get(PREF_SOURCE);
            recursive = PreferencesManager.getBoolean(PREF_RECURSIVE);
            reportFile = PreferencesManager.get(PREF_TARGET);
            transformType = PreferencesManager.getInt(PREF_TYPE,
                    ListingProcessor.REPORT_TSV);
            String xslName = PreferencesManager.get(PREF_TRANS_TARGET);
            if (xslName != null && xslName.trim().length() > 0
                    && !xslName.equals("none")) {
                Collection transformTargets = XslTransformer
                        .getTransformTargets();
                Iterator it = transformTargets.iterator();
                while (it.hasNext() && this.transformTarget == null) {
                    TransformTarget current = (TransformTarget) it.next();
                    if (current.getXslFilename().equals(xslName))
                        this.transformTarget = current;
                }
            }
            if (sourceDirectory == null) {
                sourceDirectory = "";
            }
            if (reportFile == null) {
                reportFile = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method writes the current configuration using
     * {@link entagged.tageditor.resources.PreferencesManager}<br>
     *  
     */
    public void saveAsDefault() {
        try {
            PreferencesManager.put(PREF_SOURCE, getSourceDirectory());
            PreferencesManager.put(PREF_TARGET, getReportFile());
            PreferencesManager.putInt(PREF_TYPE, getTransformType());
            PreferencesManager.putBoolean(PREF_RECURSIVE, isRecursive());
            if (getTransformTarget() == null) {
                PreferencesManager.put(PREF_TRANS_TARGET, "none");
            } else {
                PreferencesManager.put(PREF_TRANS_TARGET, getTransformTarget()
                        .getXslFilename());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the recursive
     * 
     * @param rec
     *                  value to set
     */
    public void setRecursive(boolean rec) {
        this.recursive = rec;
    }

    /**
     * @param file
     *                  The reportFile to set.
     */
    public void setReportFile(String file) {
        this.reportFile = file;
    }

    /**
     * @param source
     *                  The sourceDirectory to set.
     */
    public void setSourceDirectory(String source) {
        this.sourceDirectory = source;
    }

    /**
     * @param target
     *                  The transformTarget to set.
     */
    public void setTransformTarget(TransformTarget target) {
        this.transformTarget = target;
    }

    /**
     * @param targetMask
     *                  The transformType to set.
     */
    public void setTransformType(int targetMask) {
        this.transformType = targetMask;
    }
}