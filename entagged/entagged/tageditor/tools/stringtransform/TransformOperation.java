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
 * Implementations of this class are meant to perform string transformations.<br>
 * Some of them may not be performed together, since they would override their
 * results. Think of the operation "to uppercase" and "to lowercase" performed
 * consecutively. For that a grouping is introduced.
 * 
 * @author Christian Laireiter
 */
public abstract class TransformOperation implements Comparable {

	/**
	 * Defines a group identifier. Used for sorting the tranformations in a list
	 * and not allowing two operations of the same group at the same time.
	 */
	private final int group;

	/**
	 * Defines an operation identifer, used for sorting the transformations in a
	 * list.
	 */
	private final int id;

	/**
	 * Initializes the abstract part of this classes.
	 * 
	 * @param transformId
	 *            The id of the transformation. (used for sorting)
	 * @param transformGroup
	 *            The group-id of the transformation. (used for sorting, and
	 *            exclusivity). <code>-1</code> stays for excluding anything.
	 */
	public TransformOperation(int transformId, int transformGroup) {
		this.id = transformId;
		this.group = transformGroup;
	}

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		TransformOperation t = (TransformOperation) o;
		if (t.group == group) {
			return id - t.id;
		}
		return group - t.group;
	}

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return this.compareTo(obj) == 0;
	}

	/**
	 * This method decides whether this operation can be used together with the
	 * given one.<br>
	 * 
	 * @param other
	 *            Other transfomration to check.
	 * @return <code>true</code>, if this transformation can be used in
	 *         conjunction with the given one.
	 */
	public boolean excludes(TransformOperation other) {
		assert other != null;
		return this.group == other.group || this.group == -1
				|| other.group == -1;
	}

	/**
	 * This method should return a human readable description for the
	 * implemented transformation. e.g.: "Convert to Uppercase"
	 * 
	 * @return A description for a human.
	 */
	public abstract String getDescription();

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getDescription();
	}

	/**
	 * This method performs the transformation of the given value.
	 * 
	 * @param value
	 *            The string which will be transformed.
	 * @return The transformed value.
	 */
	public abstract String transform(String value);

}
