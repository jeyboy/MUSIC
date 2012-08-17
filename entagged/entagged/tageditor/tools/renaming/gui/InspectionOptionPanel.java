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

package entagged.tageditor.tools.renaming.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.renaming.data.RenameConfiguration;

/**
 * This panel contains some widgets to let the user enter some processing
 * options.<br>
 * For example activate the copy function (instead of renaming) if the audio
 * file cannot be modified.<br>
 * 
 * @author Christian Laireiter
 */
final class InspectionOptionPanel extends JPanel implements ActionListener {

	/**
	 * This box refers to {@link RenameConfiguration#isCopyUnmodifiableFiles()}.<br>
	 * 
	 * @see RenameConfiguration#setCopyUnmodifiableFiles(boolean)
	 */
	protected JCheckBox copyIfUnmodifiableBox;

	/**
	 * The control of the renamin preview.
	 */
	private InspectionControl inspectionControl;

	/**
	 * Creates an instance.
	 * 
	 * @param ctrl
	 *            The control of the copyoperation.
	 */
	public InspectionOptionPanel(InspectionControl ctrl) {
		assert ctrl != null;
		this.inspectionControl = ctrl;
		initialize();
	}

	/**
	 * (overridden) This method will inform {@link #inspectionControl}, to
	 * update the configuration.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		this.inspectionControl.optionsChanged();
	}

	/**
	 * Creates the fields.
	 */
	private void initialize() {
		RenameConfiguration config = inspectionControl.fileRenamer.getConfig();
		this.setLayout(new GridBagLayout());
		// Copy if Unmodifiable
		copyIfUnmodifiableBox = new JCheckBox(LangageManager
				.getProperty("tagrename.option.copyifunmodifiable"));
		copyIfUnmodifiableBox.setSelected(config.isCopyUnmodifiableFiles());
		this.add(copyIfUnmodifiableBox, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		copyIfUnmodifiableBox.addActionListener(this);
	}

}
