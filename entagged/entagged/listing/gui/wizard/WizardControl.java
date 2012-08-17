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
package entagged.listing.gui.wizard;

/**
 * This interface defines an access to the wizard. <br>
 * It is mainly meant for use with
 * {@link entagged.listing.gui.wizard.WizardTask}objects so they can notify the
 * Wizard that there might have been a change in respect to
 * {@link entagged.listing.gui.wizard.WizardTask#next()}.
 * 
 * @author Christian Laireiter
 */
public interface WizardControl {
	/**
	 * This method should check the current Task and enable the button for
	 * switching to the next task or finishing the wizard.
	 */
	void updateView();
}