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

import java.io.File;

import javax.swing.JComponent;

import entagged.listing.ListingProcessor;
import entagged.listing.gui.wizard.WizardControl;
import entagged.listing.gui.wizard.WizardTask;

/**
 * With this task a user will perform the selection of the destination and the
 * type of the report.
 * 
 * @author Christian Laireiter
 */
public class ReportTypeDestTask implements WizardTask {

    /**
     * Interface for performing current Task.
     */
    private final ReportTypeDestPanel configPanel;

    /**
     * Indicates wheter all necessary input is done.
     */
    private boolean finish;

    /**
     * The predecessor.
     */
    private final ReportTask reportTask;

    /**
     * Access for notifying changes.
     */
    private final WizardControl wizardControl;

    /**
     * Indicates if another target was specified.
     */
    private boolean next;

    /**
     * Creates an instance.
     * 
     * @param predecessor
     *                  Previous task.
     * @param ctrl
     *                  Access to the wizard control.
     */
    public ReportTypeDestTask(ReportTask predecessor, WizardControl ctrl) {
        if (predecessor == null || ctrl == null) {
            throw new IllegalArgumentException("Arguments must not be null.");
        }
        this.reportTask = predecessor;
        this.wizardControl = ctrl;
        this.finish = false;
        this.configPanel = new ReportTypeDestPanel(this);
    }

    /**
     * This method will cause the task to verify its current configuration and
     * activate/deactivate {@link #maybeFinished()}<br>
     */
    public void dataUpdated() {
        next = (getConfiguration().getTransformType() & ListingProcessor.REPORT_OTHER) > 0;
        if (getConfiguration().getReportFile() != null) {
            try {
                File file = new File(getConfiguration().getReportFile());
                this.finish = file.getParentFile().isDirectory()
                        && file.getParentFile().canWrite()
                        && file.getName().trim().length() > 0 && !next;
            } catch (Exception e) {
                e.printStackTrace();
                this.finish = false;
            }
        }
        wizardControl.updateView();
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#getComponent()
     */
    public JComponent getComponent() {
        return this.configPanel;
    }

    /**
     * Returns the configuration object.
     * 
     * @return configuration
     */
    public ReportConfig getConfiguration() {
        return reportTask.getConfiguration();
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#hasNext()
     */
    public boolean hasNext() {
        return next;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#hasPrevious()
     */
    public boolean hasPrevious() {
        // The ReportTask
        return true;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#maybeFinished()
     */
    public boolean maybeFinished() {
        return this.finish;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#next()
     */
    public WizardTask next() {
        if (next)
            return new TransformTask(this, wizardControl);
        return null;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#previous()
     */
    public WizardTask previous() {
        return this.reportTask;
    }
}