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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.renaming.FileRenamer;
import entagged.tageditor.tools.renaming.data.DirectoryDescriptor;
import entagged.tageditor.tools.renaming.model.DefaultFilter;
import entagged.tageditor.tools.renaming.model.DefaultTreeModel;
import entagged.tageditor.tools.renaming.model.TargetTreeFilter;

/**
 * This class is meant to build a frame, which displays the results of the
 * renaming preprocessing.<br>
 * 
 * @author Christian Laireiter
 */
public class InspectionControl {

	/**
	 * Action for aborting the renaming.
	 * 
	 * @author Christian Laireiter
	 */
	private final class AbortAction extends AbstractAction {

		/**
		 * Creates an instance.
		 */
		public AbortAction() {
			super(LangageManager.getProperty("common.dialog.abort"));
		}

		/**
		 * (overridden)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			abort();
		}

	}

	/**
	 * Action for confirming the renaming results.
	 * 
	 * @author Christian Laireiter
	 */
	private final class ConfirmAction extends AbstractAction {

		/**
		 * Creates an instance.
		 */
		public ConfirmAction() {
			super(LangageManager.getProperty("common.dialog.apply"));
		}

		/**
		 * (overridden)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			confirm();
		}

	}

	/**
	 * This class should perform the link-button event.
	 * 
	 * @author Christian Laireiter
	 */
	private final class LinkListener implements ItemListener {

		/**
		 * (overridden)
		 * 
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent event) {
			inspectionPanel.repaint();
		}

	}

	/**
	 * This class is meant to forward the window closing event (cross) to the
	 * abort method.<br>
	 * 
	 * @author Christian Laireiter
	 */
	private final class WindowCloser extends WindowAdapter {
		/**
		 * (overridden)
		 * 
		 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
		 */
		public void windowClosing(WindowEvent e) {
			abort();
		}

	}

	/**
	 * This field will be set by the windows cross, or the confirm abort button.<br>
	 */
	protected boolean aborted = true;

	/**
	 * This field holds the created frame or dialog for the prieview.
	 */
	private JDialog dialog;

	/**
	 * This field stores the {@link TagFromFilename} for which the current instance
	 * creates a dialog.
	 */
	protected final FileRenamer fileRenamer;

	/**
	 * Stores the inspection panel.
	 */
	protected InspectionPanel inspectionPanel = null;

	/**
	 * Store the optionspanel
	 */
	protected InspectionOptionPanel optionPanel = null;

	/**
	 * This tree displays the scanned directories and files.
	 */
	private JTree sourceTree;

	/**
	 * Tis tree displays the result of the renaming operation.
	 */
	private JTree targetTree;

	/**
	 * Creates an instance of the inspection control.
	 * 
	 * @param renamer
	 *            The filerenamer which performs the renaming.
	 */
	public InspectionControl(FileRenamer renamer) {
		assert renamer != null;
		this.fileRenamer = renamer;
	}

	/**
	 * This method sets the abort flag and closes the window.<br>
	 */
	protected void abort() {
		this.aborted = true;
		this.dialog.dispose();
	}

	/**
	 * This metod sets resets the abort flag and closes the window.<br>
	 */
	protected void confirm() {
		this.aborted = false;
		this.dialog.dispose();
	}

	/**
	 * Initializes the {@link #inspectionPanel} and creates a {@link JFrame} and
	 * displays it.
	 */
	public void displayInspector() {
		if (this.inspectionPanel == null) {
			inspectionPanel = new InspectionPanel(new ConfirmAction(),
					new AbortAction());
			inspectionPanel.sourceTree.setViewportView(getSourceTree());
			inspectionPanel.targetTree.setViewportView(getTargetTree());
			new TreeSelectionLinker(this);
			inspectionPanel.linkButton.addItemListener(new LinkListener());
			inspectionPanel.optionsPanel.add(
					this.optionPanel = new InspectionOptionPanel(this),
					BorderLayout.CENTER);
			new MessageSetter(this);
			expandAll(getSourceTree(), new TreePath(getSourceTree().getModel()
					.getRoot()));
			expandAll(getTargetTree(), new TreePath(getTargetTree().getModel()
					.getRoot()));
		}

		this.dialog = new JDialog(fileRenamer.getParentFrame());
		this.dialog.setModal(true);
		this.dialog.getContentPane().add(inspectionPanel);
		this.dialog.addWindowListener(new WindowCloser());
		this.dialog.setSize(800, 500);
		this.dialog.setLocationRelativeTo(null);
		this.dialog.doLayout();
		this.dialog.setVisible(true);
	}

	/**
	 * This method will expand the given node recursivels
	 * 
	 * @param tree
	 *            The tree whose nodes should be expanded.
	 * @param path
	 *            The path identifying the node, which should be expanded.
	 */
	private void expandAll(JTree tree, TreePath path) {
		tree.expandPath(path);
		System.out.println("Expanding "+path.getLastPathComponent());
		Object current = path.getLastPathComponent();
		int childCount = tree.getModel().getChildCount(current);
		for (int i = 0; i < childCount; i++) {
			Object[] newPath = new Object[path.getPath().length + 1];
			System.arraycopy(path.getPath(), 0, newPath, 0, newPath.length - 1);
			newPath[newPath.length - 1] = tree.getModel().getChild(current, i);
			expandAll(tree, new TreePath(newPath));
		}
	}

	/**
	 * This method returns all expanded treepaths.<br>
	 * 
	 * @param tree
	 *            The tree whose expanded paths should be determined.
	 * @return All expanded treepaths.
	 */
	private TreePath[] getExpanded(JTree tree) {
		Enumeration enumeration = tree.getExpandedDescendants(new TreePath(tree
				.getModel().getRoot()));
		ArrayList result = new ArrayList();
		while (enumeration.hasMoreElements()) {
			result.add(enumeration.nextElement());
		}
		return (TreePath[]) result.toArray(new TreePath[result.size()]);
	}

	/**
	 * This method returns a field where messages could be placed.<br>
	 * 
	 * @return Messagefield.
	 */
	protected JTextArea getMessageField() {
		return inspectionPanel.messageField;
	}

	/**
	 * Creates treemodel and renderer for the source tree.
	 * 
	 * @return the source tree.
	 */
	protected JTree getSourceTree() {
		if (sourceTree == null) {
			DirectoryDescriptor[] results = fileRenamer.getResult()
					.getFileSystemRoots();
			DefaultTreeModel model = new DefaultTreeModel(results,
					new DefaultFilter(true), LangageManager
							.getProperty("tagrename.sourcetree"));
			this.sourceTree = new JTree(model);
			this.sourceTree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			this.sourceTree.setCellRenderer(new SourceTreeCellRenderer());
			fileRenamer.getConfig().addConfigurationChangeListener(model);
		}
		return this.sourceTree;
	}

	/**
	 * Creates treemodel and renderer for the target tree.
	 * 
	 * @return the target tree.
	 */
	protected JTree getTargetTree() {
		if (targetTree == null) {
			DirectoryDescriptor[] results = fileRenamer.getResult()
					.getFileSystemRoots();
			DefaultTreeModel model = new DefaultTreeModel(results,
					new TargetTreeFilter(fileRenamer.getConfig()),
					LangageManager.getProperty("tagrename.targettree"));
			this.targetTree = new JTree(model);
			this.targetTree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			this.targetTree.setCellRenderer(new TargetTreeCellRenderer());
			this.fileRenamer.getConfig().addConfigurationChangeListener(model);
		}
		return this.targetTree;
	}

	/**
	 * This method returns <code>true</code>, if the user decided to abort
	 * the renaming.<br>
	 * 
	 * @return <code>true</code> if aborted.
	 */
	public boolean isAborted() {
		return this.aborted;
	}

	/**
	 * If this method returns <code>true</code> the tree selections should be
	 * synchronized.<br>
	 * 
	 * @return <code>true</code> if selection of trees should be in synch.
	 */
	public boolean isLinkingActive() {
		return inspectionPanel.linkButton.isSelected();
	}

	/**
	 * This method causes the control class to place every gui option into the
	 * rename configuration and update the preview.
	 */
	protected synchronized void optionsChanged() {
		TreePath[] expandedSource = getExpanded(getSourceTree());
		TreePath sourceSelected = getSourceTree().getSelectionPath();
		TreePath[] expandedTarget = getExpanded(getTargetTree());
		TreePath targetSelected = getTargetTree().getSelectionPath();
		/*
		 * Now change the configuration
		 */
		fileRenamer.setCopyUnmodifiableFiles(this.optionPanel.copyIfUnmodifiableBox.isSelected());
		fileRenamer.getConfig().fireConfigChange();
		/*
		 * Expand previously expanded.
		 */
		revertTreeState(getSourceTree(), expandedSource, sourceSelected);
		revertTreeState(getTargetTree(), expandedTarget, targetSelected);
	}

	/**
	 * This method will try to expand all given <code>expanded</code> paths
	 * and select the <code>selected</code>.<br>
	 * 
	 * @param tree
	 *            The to act upon
	 * @param expanded
	 *            Paths to expand
	 * @param selected
	 *            Path to select.
	 */
	private void revertTreeState(JTree tree, TreePath[] expanded,
			TreePath selected) {
		for (int i = 0; i < expanded.length; i++) {
			tree.expandPath(expanded[i]);

		}
		tree.setSelectionPath(selected);
	}
}
