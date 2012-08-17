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

package entagged.tageditor.tools.stringtransform;

/**
 * This is used to show that at least two
 * {@link entagged.tageditor.tools.stringtransform.TransformOperation} objects
 * were given to {@link entagged.tageditor.tools.stringtransform.TransformSet},
 * which exclude themselves.<br>
 * 
 * @see entagged.tageditor.tools.stringtransform.TransformOperation#excludes(TransformOperation)
 * @author Christian Laireiter
 */
public class IncompatibleOperationException extends Exception {

	/**
	 * Creates an instance.
	 * 
	 * @param operation1
	 *            The operation which excludes the other one.
	 * @param operation2
	 *            Excluded Operation.
	 */
	public IncompatibleOperationException(TransformOperation operation1,
			TransformOperation operation2) {
		super("\"" + operation1.getDescription() + "\" excludes \""
				+ operation2.getDescription() + "\"");
	}

}
