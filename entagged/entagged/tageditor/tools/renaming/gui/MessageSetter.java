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

import javax.swing.JTextArea;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.stat.Category;
import entagged.tageditor.tools.renaming.data.stat.Prop;
import entagged.tageditor.tools.renaming.data.stat.Statistic;

/**
 * This class catches the tree selection events and displays various information
 * about the selected Item.<br>
 * 
 * @author Christian Laireiter
 */
final class MessageSetter implements TreeSelectionListener {

	/**
	 * The control of the renaming preview.
	 */
	private InspectionControl inspectionControl;

	/**
	 * Creates an instance.<br>
	 * <b>Consider</b>:<br>
	 * This object registers itself to the source and target tree.
	 * 
	 * @param control
	 *            The control of the renaming preview.
	 */
	public MessageSetter(InspectionControl control) {
		assert control != null;
		this.inspectionControl = control;
		control.getSourceTree().addTreeSelectionListener(this);
		control.getTargetTree().addTreeSelectionListener(this);
	}

	/**
	 * This method reads the errors of the current file and places the into the
	 * message field of the renaming prieview.
	 * 
	 * @param file
	 *            The file whose errors are about to be described.
	 */
	private void handleFile(AbstractFile file) {
		Statistic stats = file.getStatistic();
		Prop[] properties = stats.getProperties();
		StringBuffer message = new StringBuffer();
		for (int i = 0; i < properties.length; i++) {
			if (properties[i].getCategories().contains(
					Category.INFORMATIVE_CATEGORY)) {
				String msg = properties[i].getDescriptionFor(file);
				if (msg != null) {
					message.append(" - ");
					message.append(msg);
					message.append("\n");
				}
			}
		}
		JTextArea messageField = inspectionControl.getMessageField();
		messageField.setText(message.toString());
		messageField.revalidate();
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		// Just handle if a file has been selected.
		if (e.isAddedPath()) {
			Object component = e.getNewLeadSelectionPath()
					.getLastPathComponent();
			if (component instanceof AbstractFile) {
				handleFile((AbstractFile) component);
			}
		}
	}

}
