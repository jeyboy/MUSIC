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
package entagged.listing.xml;

/**
 * This structure stores information about a configured transformation target.
 * 
 * @author Christian Laireiter
 */
public class TransformTarget implements Comparable {
	/**
	 * Optional description of the transformation target.
	 */
	private String description = "";

	/**
	 * Stores the language of the file the {@link #xslFilename}creates.
	 */
	private final String language;

	/**
	 * Stores the type of the resulting file.
	 */
	private final String type;

	/**
	 * Filename of the xsl.
	 */
	private String xslFilename;

	/**
	 * Creates an instance.
	 * 
	 * @param lang
	 *                  Language of the resulting transformed file.
	 * @param transformType
	 *                  type of the resulting file.
	 */
	public TransformTarget(String lang, String transformType) {
		if (lang == null || transformType == null) {
			throw new IllegalArgumentException("Argument must not be null");
		}
		this.language = lang;
		this.type = transformType;
	}

	/**
	 * This method compares the current object with the given
	 * {@link TransformTarget}object. <br>
	 * ascending by: <br>
	 * first: transform type <br>
	 * second: language <br>
	 * third: xsl filename <br>
	 * fourth: description <br>
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o2) {
		int result = 0;
		if (o2 instanceof TransformTarget) {
			TransformTarget second = (TransformTarget) o2;
			result = this.getType().compareTo(second.getType());
			if (result == 0) {
				result = this.getLanguage().compareTo(second.getLanguage());
			}
			if (result == 0) {
				result = this.getXslFilename().compareTo(
						second.getXslFilename());
			}
			if (result == 0) {
				result = this.getDescription().compareTo(
						second.getDescription());
			}
		}
		return result;
	}
	
	/** (overridden)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof TransformTarget) {
            return ((TransformTarget)obj).getXslFilename().equals(this.getXslFilename());
        }
        return false;
    }

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Returns the language.
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Returns the xslFilename.
	 */
	public String getXslFilename() {
		return xslFilename;
	}

	/**
	 * @param desc
	 *                  The description to set.
	 */
	public void setDescription(String desc) {
		if (desc == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
		this.description = desc;
	}

	/**
	 * @param filename
	 *                  The xslFilename to set.
	 */
	public void setXslFilename(String filename) {
		if (filename == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
		this.xslFilename = filename;
	}

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("TransformTarget: \n target type: " + getType() + "\n target language: "
				+ getLanguage() + "\n description:" + getDescription()+"\n");
		return result.toString();
	}
}