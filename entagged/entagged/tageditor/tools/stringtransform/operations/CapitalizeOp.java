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
 * This operation will capitalize strings.
 * 
 * @author Christian Laireiter
 */
public final class CapitalizeOp extends TransformOperation {

	/**
	 * Creates an instance.
	 */
	public CapitalizeOp() {
		super(1, 0);
	}

	/**
	 * Capitalize the given string after each given separator
	 * 
	 * @param text
	 *            the text to be capitalized
	 * @param sepEx
	 *            the regular expression separator
	 * @param separator
	 *            the separator after wich a capitalization is done
	 * @return the capitalized string
	 */
	private String capitalize(String text, String sepEx, String separator) {
		// text = text.toLowerCase();
		if (text.length() > 1) {
			String[] parts = text.split(sepEx);

			if (parts.length == 0)
				return text;
			String ret = "";

			for (int i = 0; i < parts.length; i++)
				if (parts[i].length() > 1) {
					ret += parts[i].substring(0, 1).toUpperCase()
							+ parts[i].substring(1);
					if (i != (parts.length - 1))
						ret += separator;
				} else {
					ret += parts[i].toUpperCase();
					if (i != (parts.length - 1))
						ret += separator;
				}

			return ret;
		} else if (text.length() == 1)
			return text.toUpperCase();
		else
			return text;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#getDescription()
	 */
	public String getDescription() {
		return LangageManager.getProperty("case.capitalize");
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#transform(java.lang.String)
	 */
	public String transform(String value) {
		return capitalize(capitalize(capitalize(capitalize(capitalize(
				capitalize(value.toLowerCase(), "\\n", "\n"), " ", " "), "\\.",
				"."), "_", "_"), "\\(", "("), "-", "-");
	}

}
