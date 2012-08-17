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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;

import entagged.audioformats.AudioFile;
import entagged.tageditor.exceptions.OperationException;
import entagged.tageditor.listeners.ControlPanelButtonListener;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.resources.UserComboBoxStringsManager;
import entagged.tageditor.tools.TagFromFilename;
import entagged.tageditor.tools.gui.ListHighlighter;
import entagged.tageditor.tools.stringtransform.TransformSet;
import entagged.tageditor.tools.stringtransform.TransformationList;
import entagged.tageditor.util.MultipleFieldsMergingTable;
import entagged.tageditor.util.swing.SimpleFocusPolicy;
import entagged.tageditor.util.swing.TagFromFilenameExtender;

public class TagPanel extends AbstractControlPanel {

	private class TagButtonListener extends ControlPanelButtonListener {
		String dp;

		private TransformSet transformSet;

		public TagButtonListener(TagEditorFrame tagEditorFrame,
				MultipleFieldsMergingTable audioFiles) {
			super(tagEditorFrame, audioFiles);
		}

		protected void audioFileAction(AudioFile af) throws OperationException {
			if (useDirectory.isSelected())
				TagPanel.this.fr.setDirectoryPattern(dp);
			else
				TagPanel.this.fr
						.setDirectoryPattern(TagFromFilename.IGNORE_MASK);

			TagPanel.this.fr.tagFromFilename(af, this.transformSet);
		}

		protected void finalizeAction() {
			// Save he selected indices
			int[] indices = transformationList.getSelectedIndices();

			PreferencesManager.putIntArray("tagpanel.transformations", indices);
			PreferencesManager.put("tagpanel.directory", (directoryPattern
					.getSelectedItem() == null) ? ""
					: (String) directoryPattern.getSelectedItem());
			PreferencesManager.put("tagpanel.filename", (filenamePattern
					.getSelectedItem() == null) ? "" : (String) filenamePattern
					.getSelectedItem());
		}

		protected String isDataValid() {
			if ("".equals(filenamePattern.getTextField().getText()))
				return LangageManager.getProperty("tagrename.warning.empty");

			return "";
		}

		protected void prepareAction() {
			String fp = filenamePattern.getTextField().getText();
			this.dp = directoryPattern.getTextField().getText();

			filenamePattern.putToTop(fp);
			directoryPattern.putToTop(dp);
			UserComboBoxStringsManager.addToList("tagpanel.filename.pattern",
					fp);
			UserComboBoxStringsManager.addToList("tagpanel.directory.pattern",
					dp);

			TagPanel.this.fr.setFilenamePattern(fp);
			this.transformSet = transformationList.getTransformSet();
		}
	}

	protected AutoCompleteComboBox filenamePattern, directoryPattern;

	protected TagFromFilename fr;

	protected TransformationList transformationList;

	protected JCheckBox useDirectory;

	public TagPanel(TagEditorFrame tagEditorFrame,
			MultipleFieldsMergingTable audioFiles) {
		super(tagEditorFrame, audioFiles);
	}

	public void createPanel() {
		JLabel filenamePatternL = new JLabel(LangageManager
				.getProperty("tagrename.filenamepattern"));
		JLabel directoryPatternL = new JLabel(LangageManager
				.getProperty("tagrename.directorypattern"));
		JLabel caseTransformationL = new JLabel(LangageManager
				.getProperty("tagrename.casetransformation"));

		this.fr = new TagFromFilename();
		this.fr.setFilenamePattern("");
		this.fr.setDirectoryPattern("");

		UserComboBoxStringsManager.readFile("tagpanel.filename.pattern");
		UserComboBoxStringsManager.readFile("tagpanel.directory.pattern");

		this.filenamePattern = new AutoCompleteComboBox(
				UserComboBoxStringsManager.getList("tagpanel.filename.pattern"));// new
		// String[]{"%1
		// -
		// %2","%4
		// -
		// %3"});
		filenamePattern.removeDuplicates();
		new TagFromFilenameExtender(filenamePattern);
		((JTextComponent) this.filenamePattern.getEditor().getEditorComponent())
				.setHighlighter(ListHighlighter
						.createTagFromFilenameHighlighter());
		String sel = PreferencesManager.get("tagpanel.filename");
		filenamePattern.getTextField().setText(sel);

		this.directoryPattern = new AutoCompleteComboBox(
				UserComboBoxStringsManager
						.getList("tagpanel.directory.pattern"));// new
		directoryPattern.removeDuplicates();
		// String[]{"/home/kikidonk/mus/%1/%2","c:\\home\\music\\%1\\%2"});
		new TagFromFilenameExtender(directoryPattern);
		((JTextComponent) this.directoryPattern.getEditor()
				.getEditorComponent()).setHighlighter(ListHighlighter
				.createTagFromFilenameHighlighter());
		sel = PreferencesManager.get("tagpanel.directory");
		directoryPattern.getTextField().setText(sel);

		this.filenamePattern.setToolTipText(LangageManager
				.getProperty("tagfromfilenamepanel.tooltip"));
		this.directoryPattern.setToolTipText(LangageManager
				.getProperty("tagfromfilenamepanel.tooltip"));

		ActionListener tagButtonListener = new TagButtonListener(
				tagEditorFrame, audioFiles);
		// this.filenamePattern.addActionListener(tagButtonListener);
		// this.directoryPattern.addActionListener(tagButtonListener);

		this.useDirectory = new JCheckBox(LangageManager
				.getProperty("tagrename.usedirectorypattern"));
		useDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				directoryPattern
						.setEnabled((directoryPattern.isEnabled()) ? false
								: true);
				PreferencesManager.putBoolean("tagpanel.usedirectory",
						useDirectory.isSelected());
				if (directoryPattern.getItemCount() != 0)
					directoryPattern.setSelectedIndex(0);
			}
		});
		this.useDirectory.setSelected(PreferencesManager
				.getBoolean("renamepanel.usedirectory"));
		this.directoryPattern.setEnabled(useDirectory.isSelected());

		int[] indices = PreferencesManager
				.getIntArray("tagpanel.transformations");

		this.transformationList = new TransformationList(indices);
		// this.transformationList.setVisibleRowCount(3);
		JScrollPane transformationListS = new JScrollPane(transformationList);

		JButton tag = new JButton(LangageManager
				.getProperty("tagfromfilenamepanel.button"));
		tag.addActionListener(tagButtonListener);

		JButton clear = new JButton(LangageManager
				.getProperty("tagrename.clearmasks"));
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserComboBoxStringsManager
						.clearList("tagpanel.filename.pattern");
				UserComboBoxStringsManager
						.clearList("tagpanel.directory.pattern");
				filenamePattern.removeAllItems();
				directoryPattern.removeAllItems();
				PreferencesManager.put("tagpanel.directory", "");
				PreferencesManager.put("tagpanel.filename", "");
			}
		});

		SimpleFocusPolicy policy = new SimpleFocusPolicy(this);
		policy.setComponents(new Component[] { directoryPattern,
				filenamePattern, useDirectory, clear, tag });

		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(2, 1, 2, 1);

		gbc.weighty = 1;

		this.setLayout(gbl);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbl.setConstraints(directoryPatternL, gbc);
		this.add(directoryPatternL);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbl.setConstraints(directoryPattern, gbc);
		this.add(directoryPattern);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbl.setConstraints(filenamePatternL, gbc);
		this.add(filenamePatternL);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbl.setConstraints(filenamePattern, gbc);
		this.add(filenamePattern);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0.8;
		gbl.setConstraints(useDirectory, gbc);
		this.add(useDirectory);

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0.2;
		gbl.setConstraints(clear, gbc);
		this.add(clear);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbl.setConstraints(caseTransformationL, gbc);
		this.add(caseTransformationL);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(transformationListS, gbc);
		this.add(transformationListS);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 3;
		gbl.setConstraints(tag, gbc);
		this.add(tag);
	}

	public void update() {
		// TODO
		// This set the dir pattern to current dir, needs to be more useful !
		directoryPattern.getTextField().setText(
				tagEditorFrame.getNavigator().getCurrentFolder()
						.getAbsolutePath());
	}
}
