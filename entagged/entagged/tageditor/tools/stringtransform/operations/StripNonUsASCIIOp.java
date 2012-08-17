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
 * This operation replaces all characters with unicode not bewteen 0 and 0x007f
 * (us-ascii) with underscore. Also, it tries to convert iso8859-1 accentuated
 * letters to us-ascii normal letters.<br>
 * 
 * @author Christian Laireiter
 */
public final class StripNonUsASCIIOp extends TransformOperation {

	private final static char[] ASCII_TRANSLATIONS = { '_', // 0x00A0 # NO-BREAK
			// SPACE
			'!', // 0x00A1 # INVERTED EXCLAMATION MARK
			'c', // 0x00A2 # CENT SIGN
			'L', // 0x00A3 # POUND SIGN
			'_', // 0x00A4 # CURRENCY SIGN
			'Y', // 0x00A5 # YEN SIGN
			'|', // 0x00A6 # BROKEN BAR
			'S', // 0x00A7 # SECTION SIGN
			'_', // 0x00A8 # DIAERESIS
			'_', // 0x00A9 # COPYRIGHT SIGN
			'a', // 0x00AA # FEMININE ORDINAL INDICATOR
			'<', // 0x00AB # LEFT-POINTING DOUBLE ANGLE QUOTATION MARK
			'_', // 0x00AC # NOT SIGN
			'_', // 0x00AD # SOFT HYPHEN
			'R', // 0x00AE # REGISTERED SIGN
			'_', // 0x00AF # MACRON
			'o', // 0x00B0 # DEGREE SIGN
			'_', // 0x00B1 # PLUS-MINUS SIGN
			'2', // 0x00B2 # SUPERSCRIPT TWO
			'3', // 0x00B3 # SUPERSCRIPT THREE
			'_', // 0x00B4 # ACUTE ACCENT
			'u', // 0x00B5 # MICRO SIGN
			'P', // 0x00B6 # PILCROW SIGN
			'.', // 0x00B7 # MIDDLE DOT
			'_', // 0x00B8 # CEDILLA
			'1', // 0x00B9 # SUPERSCRIPT ONE
			'o', // 0x00BA # MASCULINE ORDINAL INDICATOR
			'>', // 0x00BB # RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK
			'_', // 0x00BC # VULGAR FRACTION ONE QUARTER
			'_', // 0x00BD # VULGAR FRACTION ONE HALF
			'_', // 0x00BE # VULGAR FRACTION THREE QUARTERS
			'?', // 0x00BF # INVERTED QUESTION MARK
			'A', // 0x00C0 # LATIN CAPITAL LETTER A WITH GRAVE
			'A', // 0x00C1 # LATIN CAPITAL LETTER A WITH ACUTE
			'A', // 0x00C2 # LATIN CAPITAL LETTER A WITH CIRCUMFLEX
			'A', // 0x00C3 # LATIN CAPITAL LETTER A WITH TILDE
			'A', // 0x00C4 # LATIN CAPITAL LETTER A WITH DIAERESIS
			'A', // 0x00C5 # LATIN CAPITAL LETTER A WITH RING ABOVE
			'A', // 0x00C6 # LATIN CAPITAL LETTER AE
			'C', // 0x00C7 # LATIN CAPITAL LETTER C WITH CEDILLA
			'E', // 0x00C8 # LATIN CAPITAL LETTER E WITH GRAVE
			'E', // 0x00C9 # LATIN CAPITAL LETTER E WITH ACUTE
			'E', // 0x00CA # LATIN CAPITAL LETTER E WITH CIRCUMFLEX
			'E', // 0x00CB # LATIN CAPITAL LETTER E WITH DIAERESIS
			'I', // 0x00CC # LATIN CAPITAL LETTER I WITH GRAVE
			'I', // 0x00CD # LATIN CAPITAL LETTER I WITH ACUTE
			'I', // 0x00CE # LATIN CAPITAL LETTER I WITH CIRCUMFLEX
			'I', // 0x00CF # LATIN CAPITAL LETTER I WITH DIAERESIS
			'D', // 0x00D0 # LATIN CAPITAL LETTER ETH (Icelandic)
			'N', // 0x00D1 # LATIN CAPITAL LETTER N WITH TILDE
			'O', // 0x00D2 # LATIN CAPITAL LETTER O WITH GRAVE
			'O', // 0x00D3 # LATIN CAPITAL LETTER O WITH ACUTE
			'O', // 0x00D4 # LATIN CAPITAL LETTER O WITH CIRCUMFLEX
			'O', // 0x00D5 # LATIN CAPITAL LETTER O WITH TILDE
			'O', // 0x00D6 # LATIN CAPITAL LETTER O WITH DIAERESIS
			'x', // 0x00D7 # MULTIPLICATION SIGN
			'O', // 0x00D8 # LATIN CAPITAL LETTER O WITH STROKE
			'U', // 0x00D9 # LATIN CAPITAL LETTER U WITH GRAVE
			'U', // 0x00DA # LATIN CAPITAL LETTER U WITH ACUTE
			'U', // 0x00DB # LATIN CAPITAL LETTER U WITH CIRCUMFLEX
			'U', // 0x00DC # LATIN CAPITAL LETTER U WITH DIAERESIS
			'Y', // 0x00DD # LATIN CAPITAL LETTER Y WITH ACUTE
			'_', // 0x00DE # LATIN CAPITAL LETTER THORN (Icelandic)
			's', // 0x00DF # LATIN SMALL LETTER SHARP S (German)
			'a', // 0x00E0 # LATIN SMALL LETTER A WITH GRAVE
			'a', // 0x00E1 # LATIN SMALL LETTER A WITH ACUTE
			'a', // 0x00E2 # LATIN SMALL LETTER A WITH CIRCUMFLEX
			'a', // 0x00E3 # LATIN SMALL LETTER A WITH TILDE
			'a', // 0x00E4 # LATIN SMALL LETTER A WITH DIAERESIS
			'a', // 0x00E5 # LATIN SMALL LETTER A WITH RING ABOVE
			'a', // 0x00E6 # LATIN SMALL LETTER AE
			'c', // 0x00E7 # LATIN SMALL LETTER C WITH CEDILLA
			'e', // 0x00E8 # LATIN SMALL LETTER E WITH GRAVE
			'e', // 0x00E9 # LATIN SMALL LETTER E WITH ACUTE
			'e', // 0x00EA # LATIN SMALL LETTER E WITH CIRCUMFLEX
			'e', // 0x00EB # LATIN SMALL LETTER E WITH DIAERESIS
			'i', // 0x00EC # LATIN SMALL LETTER I WITH GRAVE
			'i', // 0x00ED # LATIN SMALL LETTER I WITH ACUTE
			'i', // 0x00EE # LATIN SMALL LETTER I WITH CIRCUMFLEX
			'i', // 0x00EF # LATIN SMALL LETTER I WITH DIAERESIS
			'o', // 0x00F0 # LATIN SMALL LETTER ETH (Icelandic)
			'n', // 0x00F1 # LATIN SMALL LETTER N WITH TILDE
			'o', // 0x00F2 # LATIN SMALL LETTER O WITH GRAVE
			'o', // 0x00F3 # LATIN SMALL LETTER O WITH ACUTE
			'o', // 0x00F4 # LATIN SMALL LETTER O WITH CIRCUMFLEX
			'o', // 0x00F5 # LATIN SMALL LETTER O WITH TILDE
			'o', // 0x00F6 # LATIN SMALL LETTER O WITH DIAERESIS
			'_', // 0x00F7 # DIVISION SIGN
			'o', // 0x00F8 # LATIN SMALL LETTER O WITH STROKE
			'u', // 0x00F9 # LATIN SMALL LETTER U WITH GRAVE
			'u', // 0x00FA # LATIN SMALL LETTER U WITH ACUTE
			'u', // 0x00FB # LATIN SMALL LETTER U WITH CIRCUMFLEX
			'u', // 0x00FC # LATIN SMALL LETTER U WITH DIAERESIS
			'y', // 0x00FD # LATIN SMALL LETTER Y WITH ACUTE
			'_', // 0x00FE # LATIN SMALL LETTER THORN (Icelandic)
			'y' // 0x00FF # LATIN SMALL LETTER Y WITH DIAERESIS
	};

	/**
	 * Creates an instance.
	 */
	public StripNonUsASCIIOp() {
		super(9, 5);
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#getDescription()
	 */
	public String getDescription() {
		return LangageManager.getProperty("transfo.stripnonusascii");
	}

	/**
	 * replaces all characters with unicode not bewteen 0 and 0x007f (us-ascii)
	 * with underscore. Also, it tries to convert iso8859-1 accentuated letters
	 * to us-ascii normal letters
	 * 
	 * @param text
	 *            Text to transform.
	 * @return transfomed text.
	 */
	private String stripNonUsASCII(String text) {
		if (text == null)
			return null;

		char[] data = text.toCharArray();
		for (int i = 0; i < data.length; i++) {
			if (data[i] >= 0x00A0 && data[i] <= 0x00FF)
				data[i] = ASCII_TRANSLATIONS[data[i] - 0x00A0];
			else if (data[i] > 0x007f)
				data[i] = '_';
		}

		return new String(data);
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#transform(java.lang.String)
	 */
	public String transform(String value) {
		return stripNonUsASCII(value);
	}

}
