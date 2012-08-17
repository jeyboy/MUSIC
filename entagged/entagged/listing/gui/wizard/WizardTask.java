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

import javax.swing.JComponent;

/**
 * Implementors of this interface represent a configuration task for the
 * {@link entagged.listing.gui.wizard.WizardDialog}.<br>
 * 
 * 
 * @author Christian Laireiter
 */
public interface WizardTask {

	/**
	 * Returns the visual interface for handling the current Task.
	 * 
	 * @return A interface for configuration.
	 */
	JComponent getComponent();

	/**
	 * This method returns <code>true</code> if the current task has one
	 * following to complete its configuration. <br>
	 * 
	 * @return <code>true</code> if another task must be performed.
	 */
	boolean hasNext();

	/**
	 * This method returns <code>true</code> if the current task has one
	 * predecessor <br>
	 * 
	 * @return <code>true</code> if predecessor exists.
	 */
	boolean hasPrevious();

	/**
	 * This method returns <code>true</code> if all necessary configurations
	 * are done and the task can be perfomed. <br>
	 * 
	 * @return <code>true</code> if all mandatory configurations are complete.
	 *              Optional may follow ({@link #hasNext()}==<code>true</code>)
	 */
	boolean maybeFinished();

	/**
	 * Returns the next {@link WizardTask}if exists. <br>
	 * 
	 * @return Next WizardTask.
	 */
	WizardTask next();

	/**
	 * Returns the previous {@link WizardTask}if exists. <br>
	 * 
	 * @return Previous WizardTask.
	 */
	WizardTask previous();
}