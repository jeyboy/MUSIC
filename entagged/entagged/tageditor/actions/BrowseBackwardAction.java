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
 * This action will cause entagged to browse to the previous visited location.
 * 
 * @author Christian Laireiter
 */
public final class BrowseBackwardAction extends AbstractAction {

    /**
     * The navigator instance on which this action will be applied.
     */
    private final Navigator control;

    /**
     * Creates the action.
     * 
     * @param parent
     *                  Instance on which this action will be applied.
     */
    public BrowseBackwardAction(Navigator parent) {
        this.control = parent;
    }

    /** (overridden)
     * @see javax.swing.AbstractAction#isEnabled()
     */
    public boolean isEnabled() {
        return true;
    }
    
    /**
     * (overridden)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        control.browseBackward();
    }

}