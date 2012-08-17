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

import java.util.ArrayList;
import java.util.Arrays;

import entagged.audioformats.Tag;

/**
 * This class is used to store multiple
 * {@link entagged.tageditor.tools.stringtransform.TransformOperation} objects.
 * Additional it may be used to process a string.<br>
 * 
 * @author Christian Laireiter
 */
public class TransformSet {

	/**
	 * This field stores the operation objects.
	 */
	private final TransformOperation[] operations;

	/**
	 * This method creates an emtpy set.
	 */
	public TransformSet() {
		this.operations = new TransformOperation[0];
	}

	/**
	 * Creates an instance and assigns the given operations.<br>
	 * 
	 * @param transformations
	 *            The transformation of the set.
	 * @throws IncompatibleOperationException
	 *             If two of the given transformation exclude themselves.<br>
	 * @see TransformOperation#excludes(TransformOperation)
	 */
	public TransformSet(TransformOperation[] transformations)
			throws IncompatibleOperationException {
		assert transformations != null;
		Arrays.sort(transformations);
		for (int i = 0; i < transformations.length; i++) {
			for (int j = i + 1; j < transformations.length; j++) {
				if (transformations[i].excludes(transformations[j])) {
					throw new IncompatibleOperationException(
							transformations[i], transformations[j]);
				}
				// and the other way around
				if (transformations[j].excludes(transformations[i])) {
					throw new IncompatibleOperationException(
							transformations[j], transformations[i]);
				}
			}
		}
		ArrayList tmp = new ArrayList(Arrays.asList(transformations));
		this.operations = (TransformOperation[]) tmp
				.toArray(new TransformOperation[tmp.size()]);
	}

	/**
	 * This method applies all stored Operations to the given value.
	 * 
	 * @param value
	 *            The value which should be transformed.
	 * @return Transformed value.
	 */
	public String transform(String value) {
		for (int i = 0; i < operations.length; i++) {
			value = operations[i].transform(value);
		}
		return value;
	}

	/**
	 * This method will transform all first values of the common fields.<br>
	 * 
	 * @param toTransform
	 *            Tag whose values are about to be transformed.
	 */
	public void transformFirstCommons(Tag toTransform) {
		if (operations.length > 0) {
			toTransform.setArtist(transform(toTransform.getFirstArtist()));
			toTransform.setAlbum(transform(toTransform.getFirstAlbum()));
			toTransform.setTitle(transform(toTransform.getFirstTitle()));
			toTransform.setTrack(transform(toTransform.getFirstTrack()));
			toTransform.setGenre(transform(toTransform.getFirstGenre()));
			toTransform.setYear(transform(toTransform.getFirstYear()));
			toTransform.setComment(transform(toTransform.getFirstComment()));
		}
	}

}
