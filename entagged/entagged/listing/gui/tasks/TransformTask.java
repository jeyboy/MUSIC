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

import javax.swing.JComponent;

import entagged.listing.gui.wizard.WizardControl;
import entagged.listing.gui.wizard.WizardTask;

/**
 * This task allows the configuration of a transformation target. <br>
 * 
 * @author Christian Laireiter
 */
public class TransformTask implements WizardTask {

    /**
     * Interface for Configuration.
     */
    private TransformPanel configPanel;

    private boolean finish;

    /**
     * Preceding task.
     */
    private ReportTypeDestTask predecessor;

    /**
     * Control for updating the buttons.
     */
    private WizardControl wizardControl;

    /**
     * Creates an instance.
     * 
     * @param pred
     *                  Predecessor
     * @param ctrl
     *                  Control to the dialog.
     */
    public TransformTask(ReportTypeDestTask pred, WizardControl ctrl) {
        this.predecessor = pred;
        this.wizardControl = ctrl;
        this.configPanel = new TransformPanel(this);
    }

    /**
     * This method checks the current configuration and decides if the task is
     * finished. <br>
     *  
     */
    public void dataUpdated() {
        finish = getConfiguration().getTransformTarget() != null;
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
     * Returns the configuration for the listing.
     * 
     * @return configuration.
     */
    public ReportConfig getConfiguration() {
        return predecessor.getConfiguration();
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#hasNext()
     */
    public boolean hasNext() {
        // not for now
        return false;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#hasPrevious()
     */
    public boolean hasPrevious() {
        return true;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#maybeFinished()
     */
    public boolean maybeFinished() {
        return finish;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#next()
     */
    public WizardTask next() {
        return null;
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.wizard.WizardTask#previous()
     */
    public WizardTask previous() {
        return predecessor;
    }

}