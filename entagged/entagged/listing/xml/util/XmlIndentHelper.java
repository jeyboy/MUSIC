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

import java.util.ArrayList;

/**
 * This class helps filling a Stringbuffer with XML-content in a pretty fashion.
 * <br>
 * 
 * @author Christian Laireiter
 */
public class XmlIndentHelper {

	/**
	 * This constant represents the character sequence which is used for
	 * indention. <br>
	 */
	public static final String INDENT_SEQUENCE = "    ";

	/**
	 * This constant represents the line separator of the current system (OS).
	 */
	public static final String LINE_SEPARATOR;

	static {
		// Determine LINE_SEPARATOR of current System.
		LINE_SEPARATOR = System.getProperty("line.separator");
	}

	/**
	 * This StringBuffer recieves the insertions.
	 */
	private StringBuffer content;

	/**
	 * Herein the names of the currently open tags are stored. <br>
	 */
	private ArrayList currentPath;

	/**
	 * With this field the number of current indentions is reflected.
	 */
	private int indentionCount = 0;

	/**
	 * Creates an Helper which is using a new StringBuffer for the XML-content.
	 */
	public XmlIndentHelper() {
		this(new StringBuffer());
	}

	/**
	 * Creates an Helper using given StringBuffer (<code>toFill</code>) for
	 * insertion of XML-Content.
	 * 
	 * @param toFill
	 *                  StringBuffer which should recieve XML-content.
	 */
	public XmlIndentHelper(StringBuffer toFill) {
		if (toFill == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
		this.content = toFill;
		this.currentPath = new ArrayList();
	}

	/**
	 * Appends the given <code>text</code> to the {@link #content}.
	 * 
	 * @param text
	 *                  Text to be appended.
	 * @param indent
	 *                  <code>true</code>, if an indention should be performed
	 *                  prior to inserting the <code>text</code>.
	 */
	public void append(String text, boolean indent) {
		if (indent) {
			indent();
		}
		this.content.append(text);
	}

	/**
	 * This methods writes the close tag for the most recent tag in the
	 * {@link #currentPath}
	 *  
	 */
	public void closeTag() {
		closeTag(true);
	}

	/**
	 * This methods writes the close tag for the most recent tag in the
	 * {@link #currentPath}.<br>
	 * 
	 * @param indent
	 *                  <code>true</code> if indention should occur prior to closing
	 *                  the tag.
	 */
	public void closeTag(boolean indent) {
		this.indentionCount--;
		if (indent) {
			this.indent();
		}
		this.append("</"
				+ currentPath.remove(currentPath.size() - 1).toString() + ">",
				false);
	}

	/**
	 * Appends {@link #indentionCount}times the {@link #INDENT_SEQUENCE}to the
	 * {@link #content}.
	 *  
	 */
	public void indent() {
		for (int i = 0; i < indentionCount; i++) {
			this.content.append(INDENT_SEQUENCE);
		}
	}

	/**
	 * This methods appends a line break.
	 */
	public void newlIne() {
		this.content.append(LINE_SEPARATOR);
	}

	/**
	 * This method creates a XML-tag with the Name
	 * 
	 * @param tagName
	 */
	public void openSimpleTag(String tagName) {
		if (tagName == null || tagName.trim().length() == 0) {
			throw new IllegalArgumentException("Tagnames must not be empty");
		}
		indent();
		tagName = tagName.trim();
		this.append("<" + tagName + ">", false);
		currentPath.add(tagName);
		this.indentionCount++;
	}

	/**
	 * Creates a tag with given attributes.
	 * 
	 * @param tagName
	 *                  Name of the Tag
	 * @param attrNames
	 *                  Names of attributes
	 * @param attrValues
	 *                  values of attributes.
	 */
	public void openTagWithAttributes(String tagName, String[] attrNames,
			String[] attrValues) {
		if (tagName == null || tagName.trim().length() == 0) {
			throw new IllegalArgumentException("Tagnames must not be empty");
		}
		tagName = tagName.trim();
		StringBuffer tmp = new StringBuffer("<" + tagName);
		for (int i = 0; i < attrNames.length; i++) {
			tmp.append(" ");
			tmp.append(attrNames[i]);
			tmp.append("=\"");
			tmp.append(attrValues[i]);
			tmp.append("\"");
		}
		tmp.append(">");
		this.append(tmp.toString(), true);
		this.indentionCount++;
		this.currentPath.add(tagName);
	}

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.content.toString();
	}
}