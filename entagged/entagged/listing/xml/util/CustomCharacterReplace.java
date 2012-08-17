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
package entagged.listing.xml.util;


/**
 * This class provides functionality to convert a given {@link java.lang.String}
 * to another String which can safely be written to xml or html. <br>
 * Characters like "&" will be replaced by theri named entities "&amp;". <br>
 * Other not configured and xml unconfrom characters will be replaced by
 * "&amp;#&lt;code&gt;"
 * 
 * @author Christian Laireiter
 */
public class CustomCharacterReplace {

    /**
     * A default instance. <br>
     */
    private final static CustomCharacterReplace DEFAULT = new CustomCharacterReplace();

    /**
     * This method returns the default instance of this class.
     * 
     * @return A CustomCharacterReplace object.
     */
    public static CustomCharacterReplace getDefault() {
        return DEFAULT;
    }

    /**
     * This method will replace all non xml (html) compatible characters either
     * with their named entities or their numerical value. <br>
     * 
     * @param value
     *                  String to convert to a xml conformance one.
     * @return String which can be used for xml PCDATA.
     */
    public static String replace(String value) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            char curr = value.charAt(i);
            /*
             * That's my only sollution to create a valid xml file, if there
             * are unsupported characters.
             * If theres a better way, we would be pleased if you inform us.
             */
            if (Character.isIdentifierIgnorable(curr)) {
                continue;
            }
            switch (curr) {
            case '&':
                break;
            case '<':
                result.append("&lt;");
                break;
            case '>':
                result.append("&gt;");
                break;
            case '"':
                result.append("&quot;");
                break;
            default:
                result.append(curr);
            }
        }
        return result.toString();
    }

    /**
     * For now no use for individual instances. <br>
     * use {@link #getDefault()}-
     *  
     */
    protected CustomCharacterReplace() {
        // No use
    }

}