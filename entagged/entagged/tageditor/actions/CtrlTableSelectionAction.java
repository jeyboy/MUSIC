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
package entagged.tageditor.actions;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

/**
 * This class is meant to perform advanced selection with the keyboard using the
 * ctrl-key. <br>
 * Example. <br>
 * If the ctrl-key is pressed and one use the down arrow key the current
 * selection should stay just the last selected row will be unselected and the
 * next row will be.
 * 
 * @author Christian Laireiter
 */
public class CtrlTableSelectionAction extends AbstractAction {

    /**
     * This method will create an instance of this class for each permutation of
     * the constructors parameters and registers them to the
     * {@link javax.swing.InputMap}and {@link javax.swing.ActionMap}of the
     * given table. <br>
     * 
     * @param table
     *                  The table which should recieve the new actions.
     */
    public static void registerCombinations(JTable table) {
        /*
         * Register CTRL+UP
         */
        CtrlTableSelectionAction tmp = new CtrlTableSelectionAction(table,
                true, false);
        table.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK),
                tmp);
        table.getActionMap().put(tmp, tmp);
        /*
         * Register CTRL+DWON
         */
        tmp = new CtrlTableSelectionAction(table, false, false);
        table.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK),
                tmp);
        table.getActionMap().put(tmp, tmp);
    }

    /**
     * If <code>true</code>, the current instance will select the new row
     * without deselecting the current one.
     */
    private boolean performExtension;

    /**
     * This field holds the table whose selection will be altered.
     */
    private JTable targetTable;

    /**
     * If <code>true</code>, the current instance will select the previous
     * row.
     */
    private boolean upWards;

    /**
     * Creates an instance.
     * 
     * @param table
     *                  The affected table.
     * @param up
     * @see #upWards
     * @param extend
     * @see #performExtension
     */
    public CtrlTableSelectionAction(JTable table, boolean up, boolean extend) {
        this.targetTable = table;
        this.upWards = up;
        this.performExtension = extend;
    }

    /**
     * (overridden)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        ListSelectionModel model = targetTable.getSelectionModel();
        //        int[] indices = targetTable.getSelectedRows();
        int leading = model.getLeadSelectionIndex();
        if (leading != -1) {
            if (upWards) {
                if (leading > 0) {
                    model.removeSelectionInterval(leading, leading);
                    model.addSelectionInterval(leading - 1, leading - 1);
                    model.setLeadSelectionIndex(leading - 1);
                    //                    if (!performExtension)
                    //                        model.removeIndexInterval(leading, leading);
                    scrollToLead();
                }
            } else {
                if (leading < targetTable.getRowCount() - 1) {
                    model.removeSelectionInterval(leading, leading);
                    model.addSelectionInterval(leading + 1, leading + 1);
                    model.setLeadSelectionIndex(leading + 1);
                    //                    if (!performExtension)
                    //                        model.removeIndexInterval(leading, leading);
                    scrollToLead();
                }
            }
        }
    }

    /**
     * Scrolls the leading selection to visible location if not.
     */ 
    private void scrollToLead() {
        Rectangle cellRect = targetTable.getCellRect(targetTable
                .getSelectionModel().getLeadSelectionIndex()+3, 0, true);
        targetTable.scrollRectToVisible(cellRect);
    }

    /**
     * (overridden)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer result = new StringBuffer(getClass().getName());
        result.append(" (direction: ");
        result.append(upWards ? "Up , " : "Down , ");
        result.append("extend ");
        result.append(performExtension);
        result.append(")");
        return result.toString();
    }

}