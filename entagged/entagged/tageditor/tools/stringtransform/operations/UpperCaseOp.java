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

package entagged.tageditor.tools.stringtransform.operations;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.stringtransform.TransformOperation;

/**
 * This operations converts all caracters of a string to uppercase.
 * 
 * @author Christian Laireiter
 */
public class UpperCaseOp extends TransformOperation {

	/**
	 * Creates an instance.
	 */
	public UpperCaseOp() {
		super(2, 0);
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#excludes(entagged.tageditor.tools.stringtransform.TransformOperation)
	 */
	public boolean excludes(TransformOperation other) {
		if (other instanceof RomanLettersOp) {
			return true;
		}
		return super.excludes(other);
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#getDescription()
	 */
	public String getDescription() {
		return LangageManager.getProperty("case.uppercase");
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#transform(java.lang.String)
	 */
	public String transform(String value) {
		return value.toUpperCase();
	}

}
