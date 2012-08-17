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
package entagged.tageditor;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import entagged.tageditor.resources.LangageManager;

/**
 * This is a simple dialog which can be easily created to show the progress of a
 * processing operation. <br>
 * 
 * 
 * @author Christian Laireiter
 */
public class ProgressDialog extends JDialog {

    /**
     * To act if the cross was used to close the dialog.
     *  
     */
    private final class WindowCloseListener extends WindowAdapter {
        /**
         * (overridden)
         * 
         * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
         */
        public void windowClosing(WindowEvent e) {
            e.getNewState();
            aborted = true;
            setAbortable(false);
        }
    }

    /**
     * indicates whether the task allows abortions.
     */
    private boolean abortable;

    /**
     * This button allows the user to abort the operation indirectly. <br>
     * The application which created this dialog must look at
     * {@link #isAborted()}.<br>
     */
    private JButton abortButton;

    /**
     * This field reflects, whether the user aborted the operation.
     */
    protected boolean aborted = false;

    /**
     * If true, the task is finished and the abort button becomes a close
     * button. <br>
     */
    private boolean finished;

    /**
     * Displays the current progress.
     */
    private JProgressBar progressBar;

    /**
     * This field will show the description of the currently performed task.
     */
    private JLabel taskDescription;

    /**
     * Creates an instance and initializes the dialog to show the progress of
     * the given task with indeterminable progress.
     * 
     * @param parent
     *            Owner of the dialog (useful for inheriting the window icon)
     * @param title
     *            Title of the dialog.
     * @param description
     *            Description of the initial task.
     */
    public ProgressDialog(JFrame parent, String title, String description) {
        super(parent);
        initialize(false, -1);
        this.setTitle(title);
        setDescription(description);
    }

    /**
     * This method will resize the dialog and centers it on the screen.
     */
    private void centerScreen() {
        Dimension dim = new Dimension(250, 100);
        this.setSize(dim);
		// Center the window on screen.
		setLocationRelativeTo(null);
    }

    public void hideAbortButton(){
    	abortButton.setVisible(false);
    }
    
    /**
     * Creates and lays out all components.
     * 
     * @param abortable
     *            if <code>true</code> a button will be placed to allow the
     *            user an abortion. (Real implementation does setVisible()).
     * @param maxValue
     *            The maximum for the progressbar. if less than zero the state
     *            will be set to indeterminable.
     */
    private void initialize(boolean abortable, int maxValue) {
        Container content = this.getContentPane();
        content.setLayout(new GridBagLayout());
        this.addWindowListener(new WindowCloseListener());

        this.abortButton = new JButton(LangageManager
                .getProperty("common.dialog.abort"));
        this.abortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                e.getActionCommand();
                setTitle(getTitle());
                if (finished) {
                    ProgressDialog.this.dispose();
                } else {
                    aborted = true;
                    // Will disable the abort button. Somehow feedback for the
                    // user
                    setAbortable(false);
                }
            }
        });
        
        taskDescription = new JLabel();
        JLabel processDescriptor = new JLabel(LangageManager
                .getProperty("common.dialog.progresstask")
                + ":");
        progressBar = new JProgressBar();

        content.add(processDescriptor, new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        content.add(taskDescription, new GridBagConstraints(1, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        content.add(progressBar, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        content.add(abortButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
                        0, 0, 0), 0, 0));
        centerScreen();
        setAbortable(abortable);
        setMaximum(maxValue);
    }

    /**
     * @return Returns the aborted.
     */
    public boolean isAborted() {
        return aborted;
    }

    /**
     * This method will actually show the hidden abortbutton. If it is already
     * visible this method will set the editable state to the given value. <br>
     * 
     * @param value
     *            <code>true</code> if progress maybe aborted.
     */
    public void setAbortable(boolean value) {
        this.abortable = value;
        if (value && !abortButton.isVisible()) {
            abortButton.setVisible(value);
            this.getContentPane().validate();
        }
        if (!finished)
            abortButton.setEnabled(value);
    }

    /**
     * @param aborted
     *            The aborted to set.
     */
    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    /**
     * This method sets serveral values with one call.
     * 
     * @param description
     *            {@link #setDescription(String)}
     * @param abortable
     *            {@link #setAbortable(boolean)}
     * @param maximum
     *            {@link #setMaximum(int)}
     * @param value
     *            {@link #setValue(int)}
     */
    public void setAll(String description, boolean abortable, int maximum,
            int value) {
        this.setDescription(description);
        this.setAbortable(abortable);
        this.setMaximum(maximum);
        this.setValue(value);
    }

    /**
     * Sets the description of the currently performed task.
     * 
     * @param description
     *            Description of the current task.
     */
    public void setDescription(String description) {
        this.taskDescription.setText(description);
        this.validate();
    }

    /**
     * This methods alters the abort button behavior and look. <br>
     * It will be used to dispose the dialog.
     * 
     * @param value
     *            Set state of progress to finished or not.
     */
    public void setFinished(boolean value) {
        this.finished = value;
        if (value) {
            abortButton.setText(LangageManager
                    .getProperty("common.dialog.quit"));
            abortButton.setVisible(true);
            abortButton.setEnabled(true);
        } else {
            abortButton.setText(LangageManager
                    .getProperty("common.dialog.abort"));
            setAbortable(this.abortable);
        }
        this.progressBar.setVisible(!value);
    }

    /**
     * This method will set the maximum of the progressbar. <br>
     * If <code>maxValue</code> is less than zero an indeterminable state will
     * be applied.
     * 
     * @param maxValue
     *            New maximum for the progressbar.
     */
    public void setMaximum(int maxValue) {
        this.progressBar.setIndeterminate(maxValue < 0);
        this.progressBar.setMaximum(maxValue);
    }
    
    /** (Overridden)
     * @see java.awt.Dialog#setModal(boolean)
     */
    public void setModal(boolean b) {
        super.setModal(b);
        if (b) {
            this.addWindowFocusListener(new WindowAdapter() {
                public void windowLostFocus(WindowEvent e) {
                    ProgressDialog.this.requestFocus();
                }
            });
        }
    }

    /**
     * This method simply forwards the value to the progressbar <br>
     * 
     * @param value
     *            New progress.
     */
    public void setValue(int value) {
        this.progressBar.setValue(value);
    }
    
}