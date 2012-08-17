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
/*
 * Created on 01-oct.-2005
 */
package entagged.tageditor.tools.stringtransform.operations;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.stringtransform.TransformOperation;

/**
 * This operation will capitalize the first character of an entire string.
 * The rest will get lowercased.<br>
 * 
 * @author Raphaël Slinckx
 */
public class CapitalizeFirstOp extends TransformOperation {

	/**
	 * Creates an instance.
	 * 
	 */
	public CapitalizeFirstOp() {
		super(4, 0);
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#getDescription()
	 */
	public String getDescription() {
		return LangageManager.getProperty("case.capitalizefirst");
	}

	/**
	 * Capitalizes the first character of the given String. The rest will
	 * become lowercase<br>
	 * 
	 * @param value
	 *            The string whose first character is about to be converted.
	 * @return <code>value</code>, where the first character is asserted to
	 *         be uppercase and the rest to be lowercase.
	 */
	public String transform(String value) {
		if (value.length() <= 0)
			return value;

		value = value.toLowerCase();

		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
}
