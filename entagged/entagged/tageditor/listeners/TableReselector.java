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
package entagged.tageditor.listeners;

import java.awt.Rectangle;
import java.io.File;
import java.util.Hashtable;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class will listen to the {@link entagged.tageditor.models.Navigator}and
 * will store the selection location of the table on each browse action. <br>
 * If then a browse backward or browse up occurs, this class will cause the
 * registered {@link javax.swing.JTable}to select the stored selection and make
 * the selected row visible. <br>
 * If a reload event occurs the complete selection will be restored, if still
 * possible.
 * 
 * @author Christian Laireiter
 */
public class TableReselector implements NavigatorListener,
        ListSelectionListener {

    /**
     * This mask extends {@link #RESTORE_EVENT_MASK}with
     * {@link NavigatorListener#EVENT_JUMPED}. Only if one of these events
     * occurs the TableReselector will perform something. <br>
     * Think of the reload action that will perform a selection change.
     */
    public final static int PERFORM_EVENT_MASK = NavigatorListener.EVENT_BACKWARD
            | NavigatorListener.EVENT_PARENT | NavigatorListener.EVENT_JUMPED;

    /**
     * This mask stores all bits of the events types in
     * {@link NavigatorListener}which should perform a restoration of the
     * table's selection and view position.
     */
    public final static int RESTORE_EVENT_MASK = NavigatorListener.EVENT_BACKWARD
            | NavigatorListener.EVENT_PARENT;

    /**
     * This field will hold the current file which was provided by
     * {@link #directoryChanged(File, File[], int)}.<br>
     */
    private File currentFolder = null;

    /**
     * On each selection change, the selected indices will be stored.
     */
    private int[] currentSelection;

    /**
     * This hashtable stores the {@link File}instances to the selected indices
     * of the {@link #targetTable}as {@link Integer}.<br>
     */
    private final Hashtable positionTable;

    /**
     * This table will be affected by this object.
     */
    protected final JTable targetTable;

    /**
     * Creates an instance.
     * 
     * @param target
     *                  the table which will recieve scroll commands by this object.
     */
    public TableReselector(JTable target) {
        this.targetTable = target;
        this.positionTable = new Hashtable();
        this.currentSelection = new int[0];
        targetTable.getSelectionModel().addListSelectionListener(this);
    }

    /**
     * (overridden)
     * 
     * @see entagged.tageditor.listeners.NavigatorListener#directoryChanged(java.io.File,
     *           java.io.File[], int)
     */
    public void directoryChanged(File newDirectory, File[] contents, int how) {
        if ((how & PERFORM_EVENT_MASK) > 0) {
            storeCurrentLocation();
            this.currentFolder = newDirectory;
            if ((how & RESTORE_EVENT_MASK) > 0) {
                restoreView();
            } else {
                selectFirst();
            }
            scrollToSelection();
        }
        if ((how & NavigatorListener.EVENT_RELOAD) > 0) {
            if (currentSelection.length > 0) {
                this.targetTable.getSelectionModel().setValueIsAdjusting(true);
                // On reload apply stored seleciton
                this.targetTable.getSelectionModel().clearSelection();
                int i = 0;
                for (; i < currentSelection.length - 1; i++) {
                    if (targetTable.getRowCount() > currentSelection[i])
                        this.targetTable.getSelectionModel()
                                .addSelectionInterval(currentSelection[i],
                                        currentSelection[i]);
                }
                if (targetTable.getRowCount() > currentSelection[i])
                    this.targetTable.getSelectionModel().addSelectionInterval(
                            currentSelection[i], currentSelection[i]);
                this.targetTable.getSelectionModel().setValueIsAdjusting(false);
            }
        }
    }

    /**
     * This method will try to find a stored selected index for the current file
     * and applys the last selected index to the table.
     */
    private void restoreView() {
        if (currentFolder != null) {
            Integer lastIndex = (Integer) positionTable.get(currentFolder);
            if (lastIndex != null) {
                targetTable.setRowSelectionInterval(lastIndex.intValue(),
                        lastIndex.intValue());
                return;
            }
        }
        selectFirst();
    }

    /**
     * This method looks at the current selection of the table and will make it
     * visible. <br>
     */
    private void scrollToSelection() {
        int selection = this.targetTable.getSelectedRow();
        if (selection >= 0) {
            Rectangle cellRect = this.targetTable.getCellRect(0, 0, false);
            targetTable.scrollRectToVisible(cellRect);
            cellRect = this.targetTable.getCellRect(selection+3, 0, false);
            targetTable.scrollRectToVisible(cellRect);
            targetTable.validate();
        }
    }

    /**
     * This method will select the first and best row of the current table
     * contents. <br>
     * First the method tries to select the first child of the current folder
     * which will be index "2" (counted from 0). If this isn't possible the
     * parent folder will be selected, which will be index "1".
     */
    private void selectFirst() {
        if (targetTable.getRowCount() >= 3) {
            targetTable.getSelectionModel().setSelectionInterval(2, 2);
        } else {
            targetTable.setRowSelectionInterval(1, 1);
        }
    }

    /**
     * This method will store the last selected index of the table and connects
     * it with the currently selected File.
     */
    private void storeCurrentLocation() {
        if (currentFolder != null) {
            positionTable.remove(currentFolder);
            /*
             * Just store a selection if selection exists.
             */
            if (currentSelection.length > 0) {
                int lastValue = currentSelection[currentSelection.length - 1];
                if (lastValue >= 0) {
                    positionTable.put(currentFolder, new Integer(lastValue));
                }
            }
        }
    }

    /**
     * (overridden)
     * 
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e) {
        /*
         * Each table content change triggers a zero selection. These values
         * should not be stored.
         */
        if (targetTable.getSelectedRows().length > 0
                && !e.getValueIsAdjusting()) {
            this.currentSelection = targetTable.getSelectedRows();
        }
    }
}