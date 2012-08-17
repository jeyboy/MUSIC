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

import entagged.listing.gui.wizard.WizardControl;
import entagged.listing.gui.wizard.WizardTask;

/**
 * This task allows users to select a directory of which contents a xml or csv
 * report will be created. <br>
 * 
 * @author Christian Laireiter
 */
public class ReportTask implements WizardTask {

    /**
     * Interface for configuring current task.
     */
    private ReportPanel configPanel;

    /**
     * This field stores all selected options.
     */
    private final ReportConfig configuration;

    /**
     * This field indicates that all necessary configuration has benn done for
     * the current task and the user may switch to the next wizard step.
     */
    private boolean nextEnabled;

    /**
     * Stores the control to the wizard in order to notify about the
     * availability of the next task.
     * 
     * @see #dataUpdated()
     */
    private WizardControl wizardControl;

    /**
     * Creates an instance.
     * 
     * @param ctrl
     *                  Access to the wizard.
     */
    public ReportTask(WizardControl ctrl) {
        this.configuration = new ReportConfig();
        this.configPanel = new ReportPanel(this);
        this.wizardControl = ctrl;
    }

    /**
     * This method will cause the task to verify its current
     * {@linkplain #configuration configuration}and activate/deactivate
     * {@link #hasNext()}<br>
     */
    public void dataUpdated() {
        if (configuration.getSourceDirectory() != null) {
            try {
                File file = new File(configuration.getSourceDirectory());
                this.nextEnabled = file.exists() && file.isDirectory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (wizardControl != null)
            this.wizardControl.updateView();
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
     * Returns the configuration object of this task.
     * 
     * @return configuration.
     */
    public ReportConfig getConfiguration() {
        return this.configuration;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#hasNext()
     */
    public boolean hasNext() {
        return this.nextEnabled;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#hasPrevious()
     */
    public boolean hasPrevious() {
        return false;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#maybeFinished()
     */
    public boolean maybeFinished() {
        /*
         * Never, because we will ask for the type of report the next step.
         */
        return false;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#next()
     */
    public WizardTask next() {
        if (hasNext())
            return new ReportTypeDestTask(this, wizardControl);
        return null;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#previous()
     */
    public WizardTask previous() {
        return null;
    }

    /**
     * This method sets the source directory and updates the config panel
     * in order to reflect the change.
     * @param absolutePath The sourcepath.
     */
    public void setSourceDirectory(String absolutePath) {
        getConfiguration().setSourceDirectory(absolutePath);
        configPanel.updateView();
    }
}