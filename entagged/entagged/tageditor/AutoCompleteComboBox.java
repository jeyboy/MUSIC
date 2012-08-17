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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// Source: http://spotlight.de/zforen/jav/m/jav-1073685652-23048.html
// $Id: AutoCompleteComboBox.java,v 1.1 2007/03/23 14:17:08 nicov1 Exp $
public class AutoCompleteComboBox extends JComboBox implements KeyListener {

	public AutoCompleteComboBox() {
		super();
		setAutoComplete();
	}

	public AutoCompleteComboBox(Object[] o) {
		super(o);
		setAutoComplete();
	}

	public AutoCompleteComboBox(Vector o) {
		super(o);
		setAutoComplete();
	}

	private boolean getClosestFetch(String search) {
		int searchLength = search.length();
		DefaultComboBoxModel model = (DefaultComboBoxModel) getModel();
		for (int i = 0; i < model.getSize(); i++) {
			String entry = (String) model.getElementAt(i);
			if (entry.length() >= searchLength
					&& entry.substring(0, searchLength).toLowerCase().equals(
							search.toLowerCase())) {
				((JTextField) getEditor().getEditorComponent()).setText(entry);
				((JTextField) getEditor().getEditorComponent()).select(
						searchLength, entry.length());
				return true;
			}
		}
		return false;
	}

	public JTextField getTextField() {
		return (JTextField) getEditor().getEditorComponent();
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
		JTextField comp = (JTextField) getEditor().getEditorComponent();
		String search = comp.getText().substring(0, comp.getSelectionStart())
				+ String.valueOf(e.getKeyChar());
		if (getClosestFetch(search)) {
			e.consume();
		}
	}

	/**
	 * This method searches for the given string in the model of the box.<br>
	 * If it cannot be found, it is added at index '0'. If found, the item will
	 * be put to top.<br>
	 * This method only works, if all items are strings. If one of them is not a
	 * string, {@link JComboBox#insertItemAt(java.lang.Object, int)} with index
	 * 0 is called.<br>
	 * <b>Consider</b> since this box will be used to store user entered
	 * values, the implementation of this method will not be the best art.
	 * However, this method will only be called upon user actions and should not
	 * take too long.
	 * 
	 * @param string
	 *            The string which should be added or inserted at top.
	 */
	public synchronized void putToTop(String string) {
		ArrayList values = new ArrayList();
		int itemCount = getItemCount();
		for (int i = 0; i < itemCount; i++) {
			Object currentItem = getItemAt(i);
			if (currentItem instanceof String) {
				values.add(currentItem);
			} else {
				((DefaultComboBoxModel) getModel()).insertElementAt(string, 0);
				return;
			}
		}
		// If we are here, we just have the strings.
		int index = values.indexOf(string);		
		if (index >= 0) {
			((DefaultComboBoxModel) getModel()).removeElementAt(index);
		}
		((DefaultComboBoxModel) getModel()).insertElementAt(string, 0);
		// Put this into the AWT-Queue.
		/*
		 * Workaround for encountered Deadlocks.
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setSelectedIndex(0);
			}
		});
	}

	/**
	 * This method will remove all duplicate values.
	 */
	public synchronized void removeDuplicates() {
		if (false) {
			return;
		}
		int itemCount = getItemCount();
		ArrayList usedItems = new ArrayList ();
		for (int i = 0; i < itemCount; i++) {
			if (!usedItems.contains(getItemAt(i))) {
				usedItems.add(getItemAt(i));
			}
		}
		((DefaultComboBoxModel) getModel()).removeAllElements();
		for (int i = 0; i < usedItems.size(); i++) {
			((DefaultComboBoxModel) getModel()).addElement(usedItems.get(i));
		}
	}

	private void setAutoComplete() {
		getEditor().getEditorComponent().addKeyListener(this);
		setEditable(true);
		((JTextField) getEditor().getEditorComponent()).setText("");
	}
}
