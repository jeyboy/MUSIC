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
package entagged.tageditor.util;

import java.io.File;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JTable;

import entagged.tageditor.models.TableSorter;
import entagged.tageditor.models.TagEditorTableModel;

/**
 * This simple structure is used to store a current selection of the tageditor's
 * table view and apply changes after some operation has been performed.<br>
 * <b>Reason and Behavior:</b><br>
 * If only one file is selected and has been changed, it is possible that its
 * row in the table view changes. For the cases that won't happen the selection
 * should switch to the next row, so the user may alter files one after another
 * without utilizing his mouse. But if there is a column sorting active, the
 * altered file can change its location more to the beginning or stay where it
 * is. In that case the selection index must be increased by one. In the other
 * cases the selection index remains as it is.<br>
 * <br>
 * If multiple files have been selected and altered at once, those files should
 * remain selected.<br>
 * <br>
 * To be able to do so, we need the selected files and their indices to identify
 * the case that needs to be applied. And this is the purpose of this class.
 * Store the selection data and apply the selection to the changed table.<br>
 * <b>ATTENTION:</b><br>
 * This class assumes to be used with entagged tag editor. Additionally the
 * implementation of {@link TableSorter} and {@link TagEditorTableModel} is
 * known to this class and should be regarded if changed.<br>
 * 
 * @author Christian Laireiter
 */
public final class SelectionRecord {

	/**
	 * The table from which this record has been created.<br>
	 * {@link #applyChange()} will work on that table.
	 */
	private final JTable affected;

	/**
	 * The model of the tageditor which contains the file objects.<br>
	 */
	private final TagEditorTableModel model;

	/**
	 * Stores the selected objects.<br>
	 * If the vector just contains one object {@link #singleFileIndex} is
	 * utilized when this record is applied.
	 */
	private Vector selectedFiles = new Vector();

	/**
	 * Only if one file is selected we can change the selection based on the row
	 * index.<br>
	 * If multiply files have been selected, they should remain selected after
	 * alteration. For that the objects (files) identities must server.<br>
	 */
	private int singleFileIndex = -1;

	/**
	 * The table sorter which maps the rows of {@link #model} to the sorted
	 * index of their values.<br>
	 * s
	 */
	private final TableSorter sorter;

	/**
	 * Creates and instance which stores the selection data.
	 * 
	 * @param table
	 *            the table to get the selected objects from.
	 * @param tableSorterModel
	 *            the sorting table model of entagged tag editor.
	 * @param tableModel
	 *            the table model of entagged tag editor.
	 * 
	 */
	public SelectionRecord(JTable table, TableSorter tableSorterModel,
			TagEditorTableModel tableModel) {
		if (table == null || tableSorterModel == null || tableModel == null) {
			throw new IllegalArgumentException("argument must not be null!");
		}
		this.affected = table;
		this.sorter = tableSorterModel;
		this.model = tableModel;
		int[] selectedRows = table.getSelectedRows();
		if (selectedRows != null && selectedRows.length > 0) {
			for (int i = 0; i < selectedRows.length; i++) {
				selectedFiles.add(tableModel.getFileAt(sorter
						.modelIndex(selectedRows[i])));
				singleFileIndex = selectedRows[i];
			}
		}
	}

	/**
	 * This method adjusts the selection of the table (given at construction) to
	 * the rules described in the class header comment.<br>
	 * It should be called after the table has changed.
	 */
	public void applyChange() {
		if (this.selectedFiles.size() > 0) {
			if (this.selectedFiles.size() == 1) {
				applySingleSelection();
			} else {
				handleMulitpleSelection();
			}
		}
	}

	/**
	 * This method will handle the selection change, if just one row has been
	 * selected on this records creation.<br>
	 */
	private void applySingleSelection() {
		affected.getSelectionModel().setValueIsAdjusting(true);
		affected.getSelectionModel().clearSelection();
		File searchFor = (File) selectedFiles.get(0);
		for (int i = 0; i < model.getRowCount(); i++) {
			if (model.getFileAt(sorter.modelIndex(i)).equals(searchFor)) {
				int newIndex = -1;
				if (i <= singleFileIndex) {
					newIndex = singleFileIndex + 1;
					if (newIndex >= model.getRowCount()) {
						newIndex = model.getRowCount() - 1;
					}
				} else {
					newIndex = singleFileIndex;
				}
				affected.changeSelection(newIndex, 0, false, false);
				break;
			}
		}
		affected.getSelectionModel().setValueIsAdjusting(false);
	}

	/**
	 * This method will select all objects in {@link #selectedFiles}.<br>
	 */
	private void handleMulitpleSelection() {
		HashSet fileSet = new HashSet(selectedFiles);
		affected.getSelectionModel().setValueIsAdjusting(true);
		affected.getSelectionModel().clearSelection();
		for (int i = 0; !fileSet.isEmpty() && i < model.getRowCount(); i++) {
			File current = model.getFileAt(sorter.modelIndex(i));
			if (fileSet.contains(current)) {
				fileSet.remove(current);
				affected.getSelectionModel().addSelectionInterval(i, i);
			}
		}
		affected.getSelectionModel().setValueIsAdjusting(false);
	}

}
