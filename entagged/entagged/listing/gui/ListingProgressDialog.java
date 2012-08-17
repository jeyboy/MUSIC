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
package entagged.listing.gui;

import java.awt.Color;
import java.awt.Container;
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
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

import entagged.tageditor.resources.LangageManager;

/**
 * This dialog displays the progress of a
 * {@link entagged.listing.ListingProcessor}.
 * 
 * @author Christian Laireiter
 */
public class ListingProgressDialog extends JDialog implements
        ListingProgressListener {

    /**
     * Allows aborting the listing.
     */
    private JButton abortButton;

    /**
     * Indicates if the abort button was pressed.
     */
    protected boolean aborted = false;

    /**
     * Displays the currently processed directory.
     */
    private JLabel currentDirectory;

    /**
     * Displays the currently processed file.
     */
    private JLabel currentFile;

    /**
     * Indicates that the processing has been finished.
     */
    protected boolean finished = false;

    /**
     * Displays various text messages.
     */
    private JTextPane messagePane;

    /**
     * Displays the progress within current directory.
     */
    private JProgressBar progressBar;

    /**
     * Creates an instance
     */
    public ListingProgressDialog() {
        super();
        initialize();
    }

    /**
     * Creates an instance
     * 
     * @param parent
     *                  owner.
     */
    public ListingProgressDialog(JDialog parent) {
        super(parent);
        initialize();
    }

    /**
     * Creates an instance
     * 
     * @param parent
     *                  owner.
     */
    public ListingProgressDialog(JFrame parent) {
        super(parent);
        initialize();
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.ListingProgressListener#abort()
     */
    public boolean abort() {
        return this.aborted;
    }

    /**
     * Adds a text to the message field {@link #messagePane}.
     * 
     * @param message
     *                  Message to display
     */
    public void appendMessage(String message) {
        try {
            if (message != null && !message.endsWith("\n")) {
                message += "\n";
            }
            MutableAttributeSet inputAttributes = messagePane
                    .getInputAttributes();
            StyleConstants.setForeground(inputAttributes, Color.BLACK);
            messagePane.getDocument().insertString(
                    messagePane.getDocument().getLength(), message,
                    inputAttributes);
            messagePane
                    .setSelectionStart(messagePane.getDocument().getLength() - 1);
            messagePane.setSelectionEnd(messagePane.getDocument().getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.ListingProgressListener#directoryChanged(int,
     *           java.lang.String)
     */
    public void directoryChanged(int fileCount, String name) {
        progressBar.setMaximum(fileCount);
        currentDirectory.setText(LangageManager
                .getProperty("listgen.progress.directory")
                + ": " + name);
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.ListingProgressListener#errorOccured(java.lang.String)
     */
    public void errorOccured(String errorDescription) {
        MutableAttributeSet inputAttributes = messagePane.getInputAttributes();
        StyleConstants.setForeground(inputAttributes, Color.RED);
        if (errorDescription != null && !errorDescription.endsWith("\n")) {
            errorDescription += "\n";
        }
        try {
            messagePane.getDocument().insertString(
                    messagePane.getDocument().getLength(), errorDescription,
                    inputAttributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.ListingProgressListener#fileChanged(int,
     *           java.lang.String)
     */
    public void fileChanged(int fileNum, String name) {
        progressBar.setValue(fileNum);
        currentFile.setText(LangageManager.getProperty("listgen.progress.file")
                + ": " + name);
    }

    /**
     * Initializes all components.
     */
    private void initialize() {
        Container content = this.getContentPane();
        content.setLayout(new GridBagLayout());

        abortButton = new JButton(LangageManager
                .getProperty("common.dialog.abort"));
        abortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (finished) {
                    ListingProgressDialog.this.dispose();
                } else {
                    if (JOptionPane.showConfirmDialog(
                            ListingProgressDialog.this, LangageManager
                                    .getProperty("listgen.confirmabort"),
                            LangageManager.getProperty("common.dialog.abort")
                                    + "?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        aborted = true;
                    }
                }
            }
        });

        currentDirectory = new JLabel("not set");
        currentFile = new JLabel("not set");
        progressBar = new JProgressBar();
        messagePane = new JTextPane();
        messagePane.setEditable(false);
        messagePane.setAutoscrolls(true);

        content.add(currentDirectory, new GridBagConstraints(0, 0, 2, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        content.add(currentFile, new GridBagConstraints(0, 1, 2, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        content.add(progressBar, new GridBagConstraints(0, 2, 2, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        content.add(new JScrollPane(messagePane), new GridBagConstraints(0, 3,
                2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 150));
        content.add(abortButton, new GridBagConstraints(1, 4, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
                        0, 0, 0), 0, 0));
        setSize(500, 300);
        this.setModal(true);
        this.setTitle(LangageManager.getProperty("listgen.progress"));
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                aborted = true;
            }
        });
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.gui.ListingProgressListener#processingFinished()
     */
    public void processingFinished() {
        this.finished = true;
        abortButton.setText(LangageManager.getProperty("common.dialog.quit"));
        currentFile.setText("");
        currentDirectory
                .setText(LangageManager.getProperty("listgen.finished"));
    }
}