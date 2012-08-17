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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entagged.listing.gui.ListingProgressDialog;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.TrackEnumerator;
import entagged.tageditor.tools.gui.TrackEnumSettingsPanel;
import entagged.tageditor.util.MultipleFieldsMergingTable;

/**
 * Panel for use with the main window of entagged. <br>
 * Gives access to the Track Enumeration Processor.
 * 
 * @author Christian Laireiter
 */
public class TrackEnumerationPanel extends JPanel implements ActionListener {

    /**
     * The processor.
     */
    private TrackEnumerator enumerator = null;

    /**
     * This button will trigger the execution of the track enumeration.
     */
    private JButton execute = null;

    /**
     * Externally updated, stores the current selection.
     */
    private MultipleFieldsMergingTable fileSelection;

    /**
     * Main window of entagged.
     */
    private TagEditorFrame mainFrame;

    /**
     * The panel which allows to configure the track enumeration settings.
     */
    private TrackEnumSettingsPanel settingsPanel = null;

    /**
     * Creates an instance.
     * 
     * @param audioFiles
     *                  The audiofiles. This field must be updated externally.
     * @param tagEditorFrame
     *                  The Frame.
     *  
     */
    public TrackEnumerationPanel(TagEditorFrame tagEditorFrame,
            MultipleFieldsMergingTable audioFiles) {
        this.mainFrame = tagEditorFrame;
        this.fileSelection = audioFiles;
        initialize();
    }

    /**
     * (overridden)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.execute) {
            final ListingProgressDialog pd = new ListingProgressDialog(
                    mainFrame);
            this.enumerator.getSettings().setProgressListener(pd);
            new Thread(new Runnable() {
                public void run() {
                    if (!execute()) {
                        pd.dispose();
                    }
                }
            }, "Track enum").start();
            pd.setModal(true);
            pd.setVisible(true);
            mainFrame.refreshCurrentTableView();
        }
    }

    /**
     * This method lets the {@link #enumerator}run.
     * 
     * @return <code>true</code> if successful.
     */
    protected boolean execute() {
        boolean result = true;
        Exception error = null;
        int count = 0;
        settingsPanel.storeSettings(enumerator.getSettings());
        try {
        	mainFrame.getEditorSettings().prepareAudioProcessing();
            enumerator.getSettings()
                    .setFiles(fileSelection.getSelectionOrderedFiles());
            count = enumerator.process(false);
        } catch (Exception e) {
            error = e;
        }
        if (count == -1 || error != null) {
            result = false;
            JOptionPane.showMessageDialog(this, LangageManager
                    .getProperty("trackenum.error"), LangageManager
                    .getProperty("common.dialog..error"),
                    JOptionPane.ERROR_MESSAGE);
        } else {
            settingsPanel.storePatterns();
            settingsPanel.storeSettings(enumerator.getSettings());
            enumerator.getSettings().saveToDefaults();
        }
        return result;
    }

    /**
     * Creates Components,
     */
    private void initialize() {
        this.setLayout(new GridBagLayout());
        this.enumerator = new TrackEnumerator();
        this.settingsPanel = new TrackEnumSettingsPanel(enumerator
                .getSettings());

        this.execute = new JButton(LangageManager.getProperty("common.dialog.apply"));
        this.execute.addActionListener(this);

        this.add(settingsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
                        5, 5, 5), 0, 0));
        this.add(execute, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
                        5, 5, 5), 0, 0));
    }
}