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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.stringtransform.TransformOperation;

/**
 * This operation identifies Roman lettes and uppercases them.<br>
 * 
 * @author Christian Laireiter
 */
public final class RomanLettersOp extends TransformOperation {

	/**
	 * This pattern identifies roman letters within a string.
	 */
	private final static Pattern ROMAN_LETTER_PATTERN = Pattern
			.compile("\\b[ivxlcIVXLC]+\\b");

	/**
	 * Creates an instance.
	 */
	public RomanLettersOp() {
		super(6, 2);
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#excludes(entagged.tageditor.tools.stringtransform.TransformOperation)
	 */
	public boolean excludes(TransformOperation other) {
		if (other instanceof UpperCaseOp) {
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
		return LangageManager.getProperty("transfo.romanletters");
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#transform(java.lang.String)
	 */
	public String transform(String value) {
		return uppercaseRomanLetters(value);
	}

	/**
	 * This method finds roman letters and uppercases them.
	 * 
	 * @param text
	 *            The text which should be transformed.
	 * @return Text with capital roman letters.
	 */
	private String uppercaseRomanLetters(String text) {
		// first replace underscores with spaces to use word separator
		Matcher ma = ROMAN_LETTER_PATTERN.matcher(text.replaceAll("_", " "));
		StringBuffer out = new StringBuffer();
		while (ma.find()) {
			ma.appendReplacement(out, ma.group().toUpperCase());
		}
		ma.appendTail(out);
		return out.toString();
	}
}
