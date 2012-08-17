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

package entagged.tageditor.tools.renaming.gui;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.DirectoryDescriptor;
import entagged.tageditor.tools.renaming.data.FileDescriptor;

/**
 * Instances of this class are meant to be registered to trees which contain the
 * source and target structure of the renaming facility. If the linking is
 * activated ({@link entagged.tageditor.tools.renaming.gui.InspectionControl#isLinkingActive()}),
 * a selection of any file in the source tree should display the file in the
 * target tree; and the other way around.<br>
 * However, since errorneous files can't be renamed, they won't show up in the
 * target tree, and can't be linked.<br>
 * 
 * @author Christian Laireiter
 */
final class TreeSelectionLinker implements TreeSelectionListener {

	/**
	 * This field stores the control instance of the inspection view.<br>
	 * It is needed to identify from which tree a selection event occured and
	 * whether the linking is activated.
	 */
	private InspectionControl inspectionControl;

	/**
	 * Creates an instance.<br>
	 * This constructor will register itself to the trees.<br>
	 * <b>Consider</b><br>
	 * Since this is a helper class, no unregistration is implemented.<br>
	 * 
	 * @param ctrl
	 *            The inpsection control
	 */
	public TreeSelectionLinker(InspectionControl ctrl) {
		assert ctrl != null;
		this.inspectionControl = ctrl;
		ctrl.getSourceTree().addTreeSelectionListener(this);
		ctrl.getTargetTree().addTreeSelectionListener(this);
	}

	/**
	 * This method will try to expand the given path (<code>descriptor</code>)
	 * in the given tree and afterwards select the given file.<br>
	 * 
	 * @param descriptor
	 *            The directory which has to be expanded.
	 * @param tree
	 *            The tree, which is to be expanded.
	 * @param selectedFile
	 *            The file which should be selected afterwards.
	 */
	private void expandAndSelect(DirectoryDescriptor descriptor, JTree tree,
			FileDescriptor selectedFile) {
		/*
		 * This may happen if the sourcetree was selected and the file contains
		 * errors. The file won't have a target directory set and won't appear
		 * in the target tree.
		 */
		if (descriptor == null) {
			tree.clearSelection();
			return;
		}
		AbstractFile[] directoryPath = descriptor.getTreePath();
		Object[] completePath = new Object[directoryPath.length + 2];
		completePath[0] = tree.getModel().getRoot();
		System.arraycopy(directoryPath, 0, completePath, 1,
				directoryPath.length);
		completePath[completePath.length - 1] = selectedFile;

		TreePath toAssign = new TreePath(completePath);
		tree.getSelectionModel().setSelectionPath(toAssign);
		tree.scrollPathToVisible(toAssign);
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent event) {
		/*
		 * Detemine, on which tree the event occured.
		 */
		boolean sourceTree = event.getSource() == inspectionControl
				.getSourceTree();
		// Is the linking marked active ?
		if (inspectionControl.isLinkingActive()) {
			/*
			 * Only if path was added
			 */
			if (event.isAddedPath()) {
				Object selectedItem = ((JTree) event.getSource())
						.getSelectionPath().getLastPathComponent();
				/*
				 * Just proceed if selected Item is a FileDescriptor
				 */
				if (selectedItem instanceof FileDescriptor) {
					FileDescriptor selectedFile = (FileDescriptor) selectedItem;
					/*
					 * Now expand and select the corresponding parent directory.
					 */
					expandAndSelect(sourceTree ? selectedFile
							.getTargetDirectory() : selectedFile
							.getSourceDirectory(),
							sourceTree ? inspectionControl.getTargetTree()
									: inspectionControl.getSourceTree(),
							selectedFile);
				}
			}
		} else {
			// Now deselect the other tree.
			if (sourceTree)
				inspectionControl.getTargetTree().clearSelection();
			else
				inspectionControl.getSourceTree().clearSelection();
		}
	}

}
