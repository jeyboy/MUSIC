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
package entagged.listing.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import entagged.tageditor.resources.LangageManager;

/**
 * This class creates a {@link javax.swing.JDialog}with next and previous
 * button.
 * 
 * @author Christian Laireiter
 */
public class WizardDialog extends JDialog implements ActionListener {

    /**
     * The button to cancel the Wizard at any time.
     */
    private JButton cancelButton;

    /**
     * This field indicates whether the wizard dialog was cancelled or not. <br>
     * Initial to <code>true</code>, because the window closing functionality
     * is not overridden.
     */
    private boolean cancelled = true;

    /**
     * This field is used to display each wizard configuration panel.
     */
    private JPanel configPanel;

    /**
     * The content Panel of this dialog.
     */
    private Container content;

    /**
     * This field stores the currently displayed Task.
     */
    private WizardTask currentTask;

    /**
     * The button which will perform the configurated tasks.
     */
    private JButton finishButton;

    /**
     * The button to step to the next task.
     */
    private JButton nextButton;

    /**
     * The button to step to the previous task.
     */
    private JButton previousButton;

    /**
     * Creates an instance.
     */
    public WizardDialog() {
        initialize();
    }

    /**
     * (overridden)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getNextButton()) {
            setTask(currentTask.next());
        }
        if (e.getSource() == getPreviousButton()) {
            setTask(currentTask.previous());
        }
        if (e.getSource() == getCancelButton()) {
            this.cancelled = true;
            this.dispose();
        }
        if (e.getSource() == getFinishButton()) {
            this.cancelled = false;
            this.dispose();
        }
    }

    /**
     * This method returns (prior to creating if needed) the cancel button.
     * 
     * @return The cancel button
     */
    private JButton getCancelButton() {
        if (this.cancelButton == null) {
            this.cancelButton = new JButton(LangageManager
                    .getProperty("common.dialog.abort"));
            this.cancelButton.addActionListener(this);
        }
        return this.cancelButton;
    }

    /**
     * This method returns (prior to creating if needed) the finish button.
     * 
     * @return The finish button
     */
    private JButton getFinishButton() {
        if (this.finishButton == null) {
            this.finishButton = new JButton(LangageManager
                    .getProperty("common.dialog.finish"));
            this.finishButton.addActionListener(this);
        }
        return this.finishButton;
    }

    /**
     * This method returns (prior to creating if needed) the next button.
     * 
     * @return The next button
     */
    private JButton getNextButton() {
        if (this.nextButton == null) {
            this.nextButton = new JButton(LangageManager
                    .getProperty("common.dialog.next"));
            this.nextButton.addActionListener(this);
        }
        return this.nextButton;
    }

    /**
     * This method returns (prior to creating if needed) the previous button.
     * 
     * @return The next button
     */
    private JButton getPreviousButton() {
        if (this.previousButton == null) {
            this.previousButton = new JButton(LangageManager
                    .getProperty("common.dialog.previous"));
            this.previousButton.addActionListener(this);
        }
        return this.previousButton;
    }

    /**
     * Creates the dialog and buttons.
     */
    private void initialize() {
        this.setModal(true);
        this.content = this.getContentPane();
        content.setLayout(new GridBagLayout());
        this.configPanel = new JPanel(new BorderLayout());
        content.add(configPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        2, 2, 2, 2), 0, 0));
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        /*
         * Insert buttons
         */
        buttonPanel.add(getCancelButton(), new GridBagConstraints(0, 0, 1, 1,
                1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        buttonPanel.add(getFinishButton(), new GridBagConstraints(1, 0, 1, 1,
                1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        buttonPanel.add(getPreviousButton(), new GridBagConstraints(2, 0, 1, 1,
                1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        buttonPanel.add(getNextButton(), new GridBagConstraints(3, 0, 1, 1,
                1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        /*
         * Insert buttonPanel
         */
        this.content.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1.0,
                0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0));
    }

    /**
     * This method shows if the dialog was closed using the cancel button.
     * 
     * @return <code>true</code> if cancelled.
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * This method sets a WizardTask which should be performed.
     * 
     * @param task
     *                  Task to perform.
     */
    public void setTask(WizardTask task) {
        if (task == null) {
            throw new IllegalArgumentException(
                    "Argument task must not be null.");
        }
        this.currentTask = task;
        configPanel.removeAll();
        configPanel.add(task.getComponent(), BorderLayout.CENTER);
        configPanel.validate();
        configPanel.repaint();
        updateView();
    }

    /**
     * This method adjusts the buttons state in respect to the
     * {@linkplain #currentTask CurrentTask}.
     */
    public void updateView() {
        if (this.currentTask != null) {
            getNextButton().setEnabled(currentTask.hasNext());
            getPreviousButton().setEnabled(currentTask.hasPrevious());
            getFinishButton().setEnabled(currentTask.maybeFinished());
            this.configPanel.removeAll();
            this.configPanel.add(currentTask.getComponent(),
                    BorderLayout.CENTER);
        }
    }
}