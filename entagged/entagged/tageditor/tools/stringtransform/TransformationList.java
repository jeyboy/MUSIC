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

import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import entagged.tageditor.tools.stringtransform.operations.CapitalizeFirstOp;
import entagged.tageditor.tools.stringtransform.operations.CapitalizeOp;
import entagged.tageditor.tools.stringtransform.operations.EmptyOp;
import entagged.tageditor.tools.stringtransform.operations.LowerCaseOp;
import entagged.tageditor.tools.stringtransform.operations.RomanLettersOp;
import entagged.tageditor.tools.stringtransform.operations.SlashToDashOp;
import entagged.tageditor.tools.stringtransform.operations.SpacesToUnderscoreOp;
import entagged.tageditor.tools.stringtransform.operations.StripNonUsASCIIOp;
import entagged.tageditor.tools.stringtransform.operations.TrimOp;
import entagged.tageditor.tools.stringtransform.operations.UnderscoreToSpaceOp;
import entagged.tageditor.tools.stringtransform.operations.UpperCaseOp;

/**
 * This list will contain list of availble
 * {@link entagged.tageditor.tools.stringtransform.TransformOperation} objects
 * and let the user select them.<br>
 * This list will look at the categories and provide a
 * {@link entagged.tageditor.tools.stringtransform.TransformSet}.
 * 
 * @author Christian Laireiter
 */
public final class TransformationList extends JList {

	/**
	 * This model is specially meant to resolve the conflicts of the contained
	 * {@link TransformOperation} objects.<br>
	 * No two operations of the same group may be selected at the same time.<br>
	 * 
	 * @author Christian Laireiter
	 */
	private final class ToggleSelectionModel extends DefaultListSelectionModel {

		/**
		 * Creates an instance.
		 */
		public ToggleSelectionModel() {
			this
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}

		/**
		 * (overridden)
		 * 
		 * @see javax.swing.DefaultListSelectionModel#addSelectionInterval(int,
		 *      int)
		 */
		public void addSelectionInterval(int index0, int index1) {
			setValueIsAdjusting(true);
			for (int i = index0; i <= index1; i++) {
				// If already selected, deselect (Toggle behavior)
				if (isSelectedIndex(i)) {
					removeSelectionInterval(i, i);
				} else {
					TransformOperation added = (TransformOperation) transformationList
							.get(i);
					// Process other transformations and find conflicts.
					int[] indices = getSelectedIndices();
					for (int j = 0; j < indices.length; j++) {
						TransformOperation op = (TransformOperation) transformationList
								.get(indices[j]);
						if (added.excludes(op)) {
							this
									.removeSelectionInterval(indices[j],
											indices[j]);
						}
					}
					super.addSelectionInterval(i, i);
				}
			}
			setValueIsAdjusting(false);
		}

		/**
		 * (overridden)
		 * 
		 * @see javax.swing.DefaultListSelectionModel#setSelectionInterval(int,
		 *      int)
		 */
		public void setSelectionInterval(int index0, int index1) {
			addSelectionInterval(index0, index1);
		}

	}

	/**
	 * Contains the transformation operations, managed by this list.
	 */
	protected Vector transformationList;

	/**
	 * Creates an instance with a default set of transformations.
	 * 
	 * @param selectedIndices
	 *            An array of indexes, which should be applied to the list.
	 */
	public TransformationList(int[] selectedIndices) {
		this.transformationList = new Vector();
		transformationList.add(new CapitalizeOp());
        transformationList.add(new CapitalizeFirstOp());
		transformationList.add(new EmptyOp());
		transformationList.add(new LowerCaseOp());
		transformationList.add(new RomanLettersOp());
		transformationList.add(new SlashToDashOp());
		transformationList.add(new SpacesToUnderscoreOp());
		transformationList.add(new StripNonUsASCIIOp());
		transformationList.add(new TrimOp());
		transformationList.add(new UnderscoreToSpaceOp());
		transformationList.add(new UpperCaseOp());
		Collections.sort(transformationList);
		this.setListData(transformationList);
		this.setSelectionModel(new ToggleSelectionModel());
		if (selectedIndices != null) {
			for (int i = 0; i < selectedIndices.length; i++) {
				if (selectedIndices[i] >= 0
						&& selectedIndices[i] < transformationList.size()) {
					this.addSelectionInterval(selectedIndices[i],
							selectedIndices[i]);
				}
			}
		}
	}

	/**
	 * This method returns a set with the selected transformations.<br>
	 * 
	 * @return Set of transformations
	 */
	public TransformSet getTransformSet() {
		Object[] selected = getSelectedValues();
		TransformOperation[] ops = new TransformOperation[selected.length];
		System.arraycopy(selected, 0, ops, 0, selected.length);
		try {
			return new TransformSet(ops);
		} catch (IncompatibleOperationException e) {
			e.printStackTrace();
			return new TransformSet();
		}
	}
}
