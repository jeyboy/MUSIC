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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import entagged.tageditor.models.Navigator;

/**
 * This action simply calls {@link entagged.tageditor.models.Navigator#reload()}
 * of the assigned navigator. <br>
 * 
 * @author Christian Laireiter
 */
public class ReloadAction extends AbstractAction {

    /**
     * The navigator whose reload method will be invoked.
     */
    private Navigator control;

    /**
     * Creates an instance.
     * 
     * @param ctrl
     *                  The navigator whose reload method will be invoked.
     */
    public ReloadAction(Navigator ctrl) {
        assert ctrl != null;
        this.control = ctrl;
    }

    /**
     * (overridden)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        this.control.reload();
    }

}