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
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import entagged.listing.gui.Utils;
import entagged.tageditor.resources.LangageManager;

/**
 * This panel represents the interface for configuring the first step of the
 * report creation.
 * 
 * @author Christian Laireiter
 */
public class ReportPanel extends HelpReportPanel implements ActionListener {

	/**
	 * This contant holds the name {@link #browseSourceButton}will recieve by
	 * {@link java.awt.Component#setName(java.lang.String)}.<br>
	 */
	public final static String NAME_DIRECTORY_BUTTON = "directory";

	/**
	 * This contant holds the name {@link #recursiveOption}will recieve by
	 * {@link java.awt.Component#setName(java.lang.String)}.<br>
	 */
	public final static String NAME_RECURSIVE_CHECKBOX = "recursive";

	/**
	 * The button for starting a {@link javax.swing.JFileChooser}.
	 */
	private JButton browseSourceButton;

	/**
	 * The task which configuration should be performed.
	 */
	protected final ReportTask parent;

	/**
	 * The field containing the path to the directory which should be reported.
	 */
	protected JTextField sourceField;

	/**
	 * With this component a user may activate/deactivate the recursiv
	 * processing of the selected folder.
	 */
	private JCheckBox recursiveOption;

	/**
	 * Creates an instance.
	 * 
	 * @param task
	 */
	public ReportPanel(ReportTask task) {
		super("entagged/listing/gui/tasks/resource/reportpanelhelp");
		this.parent = task;
		initialize();
	}

	/**
	 * (overridden)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.browseSourceButton) {
			File choosen = Utils.chooseDirectory(sourceField, parent
					.getConfiguration().getSourceDirectory());
			if (choosen != null) {
				parent.getConfiguration().setSourceDirectory(
						choosen.getAbsolutePath());
			}
		}
		this.parent.getConfiguration().setRecursive(
				recursiveOption.isSelected());
		// Reflect changes
		updateView();
		// Tell parent about possible changes.
		parent.dataUpdated();
	}

	/**
	 * Creates fields buttons and configures them.
	 */
	private void initialize() {
		getContentPane().setLayout(new GridBagLayout());
		this.sourceField = new JTextField();
		sourceField.getDocument().addDocumentListener(
				new DocumentChangeHelper(new Runnable() {
					public void run() {
						parent.getConfiguration().setSourceDirectory(
								sourceField.getText());
						parent.dataUpdated();
						sourceField.requestFocus();
					}
				}, DocumentChangeHelper.RUN_AWT_QUEUE));
		this.browseSourceButton = new JButton(LangageManager.getProperty("listgen.label.browse"));
		this.browseSourceButton.setName(NAME_DIRECTORY_BUTTON);
		this.browseSourceButton.addActionListener(this);
		this.recursiveOption = new JCheckBox(LangageManager.getProperty("listgen.label.recursively"));
		this.recursiveOption.setName(NAME_RECURSIVE_CHECKBOX);
		this.recursiveOption.addActionListener(this);

		getContentPane().add(
				new JLabel(LangageManager.getProperty("listgen.label.directory")),
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(
				sourceField,
				new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		getContentPane().add(
				browseSourceButton,
				new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(
				recursiveOption,
				new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		// Fill initial configuration
		updateView();
	}

	/**
	 * This method fils all input fields with the current configuration.
	 * 
	 * @see ReportTask#getConfiguration()
	 */
	protected void updateView() {
		String txt = parent.getConfiguration().getSourceDirectory();
		if (txt == null) {
			txt = "";
		}
		this.sourceField.setText(txt);
		this.recursiveOption.setSelected(parent.getConfiguration()
				.isRecursive());
	}

}