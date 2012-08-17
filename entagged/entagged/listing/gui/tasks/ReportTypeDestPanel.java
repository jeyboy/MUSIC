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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import entagged.listing.ListingProcessor;
import entagged.listing.gui.Utils;
import entagged.tageditor.resources.LangageManager;

/**
 * Interface for configuring the
 * {@link entagged.listing.gui.tasks.ReportTypeDestTask}.
 * 
 * @author Christian Laireiter
 */
public class ReportTypeDestPanel extends HelpReportPanel implements
        ActionListener, ItemListener {

    /**
     * This constant holds the name {@link #browseButton}will recieve using
     * {@link java.awt.Component#setName(java.lang.String)}<br>
     */
    public final static String NAME_FILESELECTION_BUTTON = "fileselection";

    /**
     * This constant holds the name {@link #targetTypeBox}will recieve using
     * {@link java.awt.Component#setName(java.lang.String)}.<br>
     */
    public final static String NAME_TYPESELECTION_BOX = "typeselection";

    /**
     * This button shows a file chooser for saving.
     */
    private JButton browseButton;

    /**
     * Task for which current panel is created.
     */
    protected final ReportTypeDestTask parent;

    /**
     * Input for the target where the result should be written to.
     */
    protected JTextField targetField;

    /**
     * With this box a user can choose the type of the generated report.
     */
    protected JComboBox targetTypeBox;

    /**
     * Creates an instance.
     * 
     * @param task
     *                  Task to be configured.
     */
    public ReportTypeDestPanel(ReportTypeDestTask task) {
        super("entagged/listing/gui/tasks/resource/reporttypedestpanelhelp");
        if (task == null) {
            throw new IllegalArgumentException("Argument must not be null.");
        }
        this.parent = task;
        initialize();
    }

    /**
     * (overridden)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.browseButton) {
            File selection = Utils.chooseTargetFile(targetField, parent
                    .getConfiguration().getReportFile());
            if (selection != null) {
                parent.getConfiguration().setReportFile(
                        selection.getAbsolutePath());
            }
        }
        updateView();
    }

    /**
     * Creates components
     */
    private void initialize() {
        getContentPane().setLayout(new GridBagLayout());
        this.targetField = new JTextField();
        this.targetField.getDocument().addDocumentListener(
                new DocumentChangeHelper(new Runnable() {
                    public void run() {
                        parent.getConfiguration().setReportFile(
                                targetField.getText());
                        parent.dataUpdated();
                        targetField.requestFocus();
                    }
                }, DocumentChangeHelper.RUN_AWT_QUEUE));
        this.browseButton = new JButton(LangageManager
                .getProperty("listgen.label.browse"));
        this.browseButton.setName(NAME_FILESELECTION_BUTTON);
        this.browseButton.addActionListener(this);

        // TODO Language for comments
        this.targetTypeBox = new JComboBox(new String[] {
                "tsv (tab seperated values)", "xml", "other..." });
        this.targetTypeBox.setName(NAME_TYPESELECTION_BOX);
        this.targetTypeBox.addItemListener(this);

        getContentPane().add(
                new JLabel(LangageManager.getProperty("listgen.label.saveas")),
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));
        getContentPane().add(
                targetField,
                new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
                        0, 0));
        getContentPane().add(
                browseButton,
                new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));
        getContentPane().add(
                new JLabel(LangageManager
                        .getProperty("listgen.label.reporttype")),
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.EAST,
                        new Insets(0, 0, 0, 0), 0, 0));
        getContentPane().add(
                targetTypeBox,
                new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.WEST,
                        new Insets(0, 0, 0, 0), 0, 0));
        // Now fill existing config.
        updateView();
    }

    /**
     * This method enters all configuration into the components.
     */
    private void updateView() {
        this.targetField.setText(parent.getConfiguration().getReportFile());
        if ((parent.getConfiguration().getTransformType() & ListingProcessor.REPORT_TSV) > 0) {
            this.targetTypeBox.setSelectedIndex(0);
        } else if ((parent.getConfiguration().getTransformType() & ListingProcessor.REPORT_XML) > 0) {
            this.targetTypeBox.setSelectedIndex(1);
        } else if ((parent.getConfiguration().getTransformType() & ListingProcessor.REPORT_OTHER) > 0) {
            this.targetTypeBox.setSelectedIndex(2);
        }
    }

    /**
     * (overridden)
     * 
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == this.targetTypeBox) {
            String selection = (String) targetTypeBox.getSelectedItem();
            if (selection.startsWith("tsv")) {
                parent.getConfiguration().setTransformType(
                        ListingProcessor.REPORT_TSV);
            }
            if (selection.startsWith("xml")) {
                parent.getConfiguration().setTransformType(
                        ListingProcessor.REPORT_XML);
            }
            if (selection.startsWith("other")) {
                parent.getConfiguration().setTransformType(
                        ListingProcessor.REPORT_OTHER);
            }
        }
        parent.dataUpdated();
    }
}