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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import entagged.tageditor.resources.ResourcesRepository;

/**
 * This panel contains the elements used to display the processing results of
 * the file renaming operation.<br>
 * 
 * @author Christian Laireiter
 */
class InspectionPanel extends JPanel {

	/**
	 * This class is used to adjust the used sliders of the panel, when it first
	 * becomes visible.
	 * 
	 * @author Christian Laireiter
	 */
	private final class SliderUpdater implements AncestorListener {

		/**
		 * (overridden)
		 * 
		 * @see javax.swing.event.AncestorListener#ancestorAdded(javax.swing.event.AncestorEvent)
		 */
		public void ancestorAdded(AncestorEvent event) {
			((InspectionPanel) event.getSource()).removeAncestorListener(this);
			// Now the panel became visible, so..
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					updateSliders();
				}
			});
		}

		/**
		 * (overridden)
		 * 
		 * @see javax.swing.event.AncestorListener#ancestorMoved(javax.swing.event.AncestorEvent)
		 */
		public void ancestorMoved(AncestorEvent event) {
			// Nothing to do
		}

		/**
		 * (overridden)
		 * 
		 * @see javax.swing.event.AncestorListener#ancestorRemoved(javax.swing.event.AncestorEvent)
		 */
		public void ancestorRemoved(AncestorEvent event) {
			// Nothing to do
		}

	}

	/**
	 * This field holds the button, that will abort the operation.
	 */
	private JButton abortButton;

	/**
	 * This field contains {@link #confirmButton} and {@link #abortButton}.
	 */
	private JPanel buttonPanel;

	/**
	 * This field holds the button, that will confirm the operation results.<br>
	 */
	private JButton confirmButton;

	/**
	 * This field stores the link button, which will activate the selections of
	 * both trees being synchronized.
	 */
	protected JToggleButton linkButton;

	/**
	 * This field contains messages.
	 */
	protected JTextArea messageField;

	/**
	 * This field contains the message panel at the bottom and the trees at the
	 * top.
	 */
	JSplitPane messageTreePanel;

	/**
	 * This panel can be used to place options at the top of the panel.
	 */
	protected JPanel optionsPanel;

	/**
	 * This scroll pane contains the tree for the source view.
	 */
	protected JScrollPane sourceTree;

	/**
	 * This scroll pane contains the tree for the target view.
	 */
	protected JScrollPane targetTree;

	/**
	 * This field contains the two trees.
	 */
	JSplitPane treesPanel;

	/**
	 * Creates an instance.<br>
	 * 
	 * @param confirm
	 *            Action, which is used to confirm the results.
	 * @param abort
	 *            Action, which is used to discard the results.
	 */
	public InspectionPanel(Action confirm, Action abort) {
		this.initialize(confirm, abort);
	}

	/**
	 * Creates an lays out components.
	 * 
	 * @param confirm
	 *            Action, which is used to confirm the results.
	 * @param abort
	 *            Action, which is used to discard the results.
	 */
	private void initialize(Action confirm, Action abort) {
		this.setLayout(new GridBagLayout());
		// Trees and message
		this.messageTreePanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		// source and target tree
		this.treesPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		// Source tree
		this.sourceTree = new JScrollPane();
		// target tree
		this.targetTree = new JScrollPane();
		// insert the treepanes
		this.treesPanel.setLeftComponent(sourceTree);
		this.treesPanel.setRightComponent(targetTree);
		// message area
		this.messageField = new JTextArea();
		this.messageField.setLineWrap(true);
		this.messageField.setWrapStyleWord(true);
		this.messageField.setEditable(false);
		JScrollPane messageSP = new JScrollPane(messageField);
		// insert message area and treesPanel
		this.messageTreePanel.setLeftComponent(treesPanel);
		this.messageTreePanel.setRightComponent(messageSP);
		// insert messageTreePanel into current panel
		this.add(messageTreePanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		// Create linkbutton
		this.linkButton = new JToggleButton(ResourcesRepository
				.getImageIcon("link.png"));
		this.linkButton.setSelected(true);
		this.add(linkButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		// Options panel
		this.optionsPanel = new JPanel(new BorderLayout());
		this.add(optionsPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		/*
		 * Button panel and buttons
		 */
		this.buttonPanel = new JPanel(new GridLayout(1, 2));
		this.confirmButton = new JButton(confirm);
		this.confirmButton.setDefaultCapable(true);
		this.abortButton = new JButton(abort);
		this.buttonPanel.add(confirmButton);
		this.buttonPanel.add(abortButton);
		this.add(buttonPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));
		// Add ancestor listener which will adjust the dividers position
		// of messagetreepanel and treespanel
		this.addAncestorListener(new SliderUpdater());
	}

	/**
	 * This method will layot the sliders of {@link #messageTreePanel} and
	 * {@link #treesPanel}
	 */
	void updateSliders() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				messageTreePanel.setDividerLocation(0.7d);
				treesPanel.setDividerLocation(0.5d);
			}
		});
		invalidate();
	}

}
