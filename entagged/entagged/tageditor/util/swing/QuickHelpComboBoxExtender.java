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
package entagged.tageditor.util.swing;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;

/**
 * This class specializes the
 * {@link entagged.tageditor.util.swing.QuickHelpFieldExtender}for use with
 * comboboxes. <br>
 * If the completion window is shown, the user navigates using the arrow keys.
 * This will trigger the popupmenu of a combobox. So this class does some
 * implementation to suppress this.
 * 
 * @author Christian Laireiter
 */
public class QuickHelpComboBoxExtender extends QuickHelpFieldExtender {

    /**
     * This class should catch the opening of the popup of the registered
     * combobox, decide whether it should be shown and interact if needed.
     */
    private final class PopUpListener implements PopupMenuListener {

        /**
         * (overridden)
         * 
         * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuCanceled(PopupMenuEvent e) {
            // Of no interest.
        }

        /**
         * (overridden)
         * 
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            // Of no interest.
        }

        /**
         * (overridden)
         * 
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            if (getSelectionWindow().isVisible()) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        comboBox.hidePopup();
                    }
                });
            }
        }
    }

    /**
     * The currently registered combobox.
     */
    protected JComboBox comboBox = null;

    /**
     * Listener for the popup.
     */
    private PopUpListener listener = null;

    /**
     * Creates an instance which will register itself to the given combobox.
     * 
     * @param listenTo
     *                  The combobox on which this helper is applied.
     */
    public QuickHelpComboBoxExtender(JComboBox listenTo) {
        super();
        registerTo(listenTo);
    }

    /**
     * Will remove all listeners and don't bother the previously registered
     * combobox any further.
     */
    public void deinstall() {
        if (comboBox != null && listener != null) {
            comboBox.removePopupMenuListener(this.listener);
            this.listener = null;
            this.comboBox = null;
        }
    }

    /**
     * @param comboBox2
     */
    public void install(JComboBox comboBox2) {
        this.comboBox = comboBox2;
        if (comboBox.getEditor().getEditorComponent() instanceof JTextComponent) {
            super.registerTo((JTextComponent) comboBox.getEditor()
                    .getEditorComponent());
            comboBox.addPopupMenuListener(this.listener = new PopUpListener());
            super.registerTo((JTextComponent) comboBox.getEditor()
                    .getEditorComponent());
        }
    }

    /**
     * This method will register all needed listeners.
     * 
     * @param comboBox2
     *                  The box to register on.
     */
    private void registerTo(JComboBox comboBox2) {
        if (this.comboBox != null) {
            deinstall();
        }
        install(comboBox2);
    }

    /**
     * (overridden)
     * 
     * @see entagged.tageditor.util.swing.QuickHelpFieldExtender#showWindow()
     */
    protected void showWindow() {
        if (!comboBox.isPopupVisible())
            super.showWindow();
        	SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    comboBox.requestFocus();
                }
            });
    }

}