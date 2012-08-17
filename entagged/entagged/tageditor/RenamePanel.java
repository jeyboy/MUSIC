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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.resources.UserComboBoxStringsManager;
import entagged.tageditor.tools.gui.ListHighlighter;
import entagged.tageditor.tools.renaming.FileRenamer;
import entagged.tageditor.tools.stringtransform.TransformationList;
import entagged.tageditor.util.MultipleFieldsMergingTable;
import entagged.tageditor.util.swing.FilenameFromTagExtender;
import entagged.tageditor.util.swing.SimpleFocusPolicy;

public class RenamePanel extends AbstractControlPanel {

	/**
	 * This will start the renaming processor.
	 * 
	 * @author Christian Laireiter
	 */
	final class RenameAction extends AbstractAction {
		/**
		 * Creates an instance.
		 */
		public RenameAction() {
			super(LangageManager.getProperty("renamefromtagpanel.button"));
		}

		/**
		 * (overridden)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			/*
			 * Take the entered patterns into the lists.
			 */
			String dirPattern = null;
			if (getUseDirectory().isSelected()) {
				dirPattern = getDirectoryPattern().getTextField().getText();
			}
			String filePattern = getFilenamePattern().getTextField().getText();

			getFilenamePattern().putToTop(filePattern.trim());
			UserComboBoxStringsManager.addToList(
					"renamepanel.filename.pattern", filePattern);
			if (dirPattern != null) {
				getDirectoryPattern().putToTop(dirPattern.trim());
				UserComboBoxStringsManager.addToList(
						"renamepanel.directory.pattern", dirPattern);
			}

			tagEditorFrame.getEditorSettings().prepareAudioProcessing();

			/*
			 * Perform processing
			 */
			FileRenamer fileRenamer = new FileRenamer(
					RenamePanel.this.tagEditorFrame,
					(File[]) RenamePanel.this.audioFiles.getAudioFiles()
							.toArray(new File[0]), dirPattern, filePattern,
					getTransformationList().getTransformSet());
			fileRenamer.getConfig().setExtendTrackNumbers(
					getExtendTrackNumbers().isSelected());
			Thread t = new Thread(fileRenamer);
			t.start();

			/*
			 * Save patterns and transformations
			 */
			if (!fileRenamer.isAborted()) {
				// Save he selected indices
				int[] indices = getTransformationList().getSelectedIndices();
				PreferencesManager.putIntArray("renamepanel.transformations",
						indices);
				PreferencesManager.put("renamepanel.directory",
						(getDirectoryPattern().getSelectedItem() == null) ? ""
								: (String) getDirectoryPattern()
										.getSelectedItem());
				PreferencesManager.put("renamepanel.filename",
						(getFilenamePattern().getSelectedItem() == null) ? ""
								: (String) getFilenamePattern()
										.getSelectedItem());
			}

			RenamePanel.this.tagEditorFrame.refreshCurrentTableView();
		}

	}

	/**
	 * This box is used to select or deselect the extension of the track number
	 * values.<br>
	 * If a track value is a number, the renamer will add a leading zero.
	 * &quot;1&quot; will become &quot;01&quot;.<br>
	 */
	private JCheckBox extendTrackNumbers;

	private AutoCompleteComboBox filenamePattern, directoryPattern;

	private TransformationList transformationList;

	private JCheckBox useDirectory;

	public RenamePanel(TagEditorFrame tagEditorFrame,
			MultipleFieldsMergingTable audioFiles) {
		super(tagEditorFrame, audioFiles);
	}

	/**
	 * Creates the components of the rename panel, and adds them to it.<br>
	 */
	protected void createPanel() {
		/*
		 * Create Label fields.
		 */
		JLabel filenamePatternL = new JLabel(LangageManager
				.getProperty("tagrename.filenamepattern"));
		JLabel directoryPatternL = new JLabel(LangageManager
				.getProperty("tagrename.directorypattern"));
		JLabel caseTransformationL = new JLabel(LangageManager
				.getProperty("tagrename.casetransformation"));

		// Load the pattern data
		UserComboBoxStringsManager.readFile("renamepanel.filename.pattern");
		UserComboBoxStringsManager.readFile("renamepanel.directory.pattern");

		// Create the button for starting the renaming
		JButton rename = new JButton(new RenameAction());

		// create the button which will erase stored patterns.
		JButton clear = new JButton(LangageManager
				.getProperty("tagrename.clearmasks"));
		/*
		 * Since it has not much to do, assign the functionality here.
		 */
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserComboBoxStringsManager
						.clearList("renamepanel.filename.pattern");
				UserComboBoxStringsManager
						.clearList("renamepanel.directory.pattern");
				getFilenamePattern().removeAllItems();
				getDirectoryPattern().removeAllItems();
				PreferencesManager.put("renamepanel.directory", "");
				PreferencesManager.put("renamepanel.filename", "");
			}
		});

		// Assign the Focus Policy.
		SimpleFocusPolicy policy = new SimpleFocusPolicy(this);
		policy.setComponents(new Component[] { getDirectoryPattern(),
				getFilenamePattern(), useDirectory, getExtendTrackNumbers(),
				clear, rename });

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(2, 1, 2, 1);
		gbc.weighty = 1;

		this.setLayout(new GridBagLayout());

		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		gbc.gridwidth = 1;
		this.add(directoryPatternL, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		this.add(getDirectoryPattern(), gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0.3;
		this.add(filenamePatternL, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		this.add(getFilenamePattern(), gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0.3;
		gbc.weighty = 0.2;
		this.add(getUseDirectory(), gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		this.add(getExtendTrackNumbers(), gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 0.2;
		this.add(clear, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		this.add(caseTransformationL, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridheight = 6;
		this.add(new JScrollPane(getTransformationList()), gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 3;
		this.add(rename, gbc);

	}

	protected AutoCompleteComboBox getDirectoryPattern() {
		if (this.directoryPattern == null) {
			this.directoryPattern = new AutoCompleteComboBox(
					UserComboBoxStringsManager
							.getList("renamepanel.directory.pattern"));// new
			new FilenameFromTagExtender(directoryPattern, true);

			directoryPattern.removeDuplicates();
			((JTextComponent) this.directoryPattern.getEditor()
					.getEditorComponent()).setHighlighter(ListHighlighter
					.createRenameFromTagHighlighter());
			String sel = PreferencesManager.get("renamepanel.directory");
			directoryPattern.getTextField().setText(sel);

			this.directoryPattern.setToolTipText(LangageManager
					.getProperty("renamefromtagpanel.tooltip"));
		}
		return this.directoryPattern;
	}

	/**
	 * This method will return a proper configured {@link JCheckBox} for the
	 * configuration of the number extension mechanism of the renaming.<br>
	 * If {@link #extendTrackNumbers} is <code>null</code> it will be created
	 * and presetted by the preference value if exists.<br>
	 * 
	 * @return The checkbox for the number extension.
	 */
	protected JCheckBox getExtendTrackNumbers() {
		if (this.extendTrackNumbers == null) {
			this.extendTrackNumbers = new JCheckBox(LangageManager
					.getProperty("tagrename.extendtracks"));
			this.extendTrackNumbers.setSelected(PreferencesManager
					.getBoolean("renamepanel.extendtracks"));
			this.extendTrackNumbers.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PreferencesManager.putBoolean("renamepanel.extendtracks",
							getExtendTrackNumbers().isSelected());
				}
			});
		}
		return this.extendTrackNumbers;
	}

	protected AutoCompleteComboBox getFilenamePattern() {
		if (this.filenamePattern == null) {
			this.filenamePattern = new AutoCompleteComboBox(
					UserComboBoxStringsManager
							.getList("renamepanel.filename.pattern"));
			new FilenameFromTagExtender(filenamePattern, true);
			((JTextComponent) this.filenamePattern.getEditor()
					.getEditorComponent()).setHighlighter(ListHighlighter
					.createRenameFromTagHighlighter());
			String sel = PreferencesManager.get("renamepanel.filename");
			filenamePattern.getTextField().setText(sel);
			filenamePattern.removeDuplicates();
			this.filenamePattern.setToolTipText(LangageManager
					.getProperty("renamefromtagpanel.tooltip"));
		}
		return this.filenamePattern;
	}

	protected TransformationList getTransformationList() {
		if (this.transformationList == null) {
			int[] indices = PreferencesManager
					.getIntArray("renamepanel.transformations");
			this.transformationList = new TransformationList(indices);
		}
		return this.transformationList;
	}

	protected JCheckBox getUseDirectory() {
		if (this.useDirectory == null) {
			this.useDirectory = new JCheckBox(LangageManager
					.getProperty("tagrename.usedirectorypattern"));
			useDirectory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getDirectoryPattern().setEnabled(
							(getDirectoryPattern().isEnabled()) ? false : true);
					PreferencesManager.putBoolean("renamepanel.usedirectory",
							getUseDirectory().isSelected());
					if (getDirectoryPattern().getItemCount() != 0)
						getDirectoryPattern().setSelectedIndex(0);
				}
			});
			this.useDirectory.setSelected(PreferencesManager
					.getBoolean("renamepanel.usedirectory"));
			this.getDirectoryPattern().setEnabled(useDirectory.isSelected());
		}
		return this.useDirectory;
	}

	// Nothing to do
	public void update() {
		// Nothing to do
	}
}
