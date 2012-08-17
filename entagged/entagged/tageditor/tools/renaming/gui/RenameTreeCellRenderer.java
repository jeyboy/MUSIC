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

import java.awt.Component;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import entagged.tageditor.resources.ResourcesRepository;
import entagged.tageditor.tools.gui.MultiIcon;
import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.DirectoryDescriptor;
import entagged.tageditor.tools.renaming.data.FileDescriptor;
import entagged.tageditor.tools.renaming.data.stat.Category;
import entagged.tageditor.tools.renaming.data.stat.properties.NoChangeProperty;

/**
 * This renderer is used to display the results of the renaming operation.<br>
 * 
 * @author Christian Laireiter
 */
public class RenameTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * This field contains resourcenames to the resources themselves.
	 */
	private HashMap iconMap = new HashMap();

	/**
	 * This is the icon instance, that will be modified.
	 */
	protected final MultiIcon multiIcon = new MultiIcon();

	/**
	 * If <code>true</code>, the labels for files are taken from the source.
	 * Else their destination.
	 */
	private final boolean sourceRendering;

	/**
	 * Creates an instance.
	 * 
	 * @param source
	 *            Rendering for the source tree.<br>
	 */
	public RenameTreeCellRenderer(boolean source) {
		this.sourceRendering = source;
	}

	/**
	 * Returns the fileIcon for the given extension and state.
	 * 
	 * @param ext
	 *            The file extension for which the icon should be loaded
	 * @param error
	 *            if <code>true</code> the errorIcon will be loaded.
	 * @return The requested icon.
	 */
	protected ImageIcon getExtensionIcon(String ext, boolean error) {
		assert ext != null && ext.trim().length() > 0;
		String iconName = ext;
		if (error) {
			iconName += "-error";
		}
		iconName += "-icon.png";
		return getIcon(iconName);
	}

	/**
	 * This method will retrieve the icon using {@link ResourcesRepository}and
	 * put it in cache.
	 * 
	 * @param iconName
	 *            name of the icon.
	 * @return The icon
	 */
	protected ImageIcon getIcon(String iconName) {
		ImageIcon result = (ImageIcon) iconMap.get(iconName);
		if (result == null) {
			System.out.println(iconName);
			result = ResourcesRepository.getImageIcon(iconName);
			iconMap.put(iconName, result);
		}
		return result;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
	 *      java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean currentHasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, currentHasFocus);
		if (value instanceof AbstractFile) {
			processAbstractFile((AbstractFile) value);
		}
		return this;
	}

	/**
	 * This method adjusts the current label for an {@link AbstractFile}
	 * 
	 * @param file
	 *            Object to gather information from.
	 */
	protected void processAbstractFile(AbstractFile file) {
		this.setIcon(multiIcon);
		this.multiIcon.reset();
		this.multiIcon.addIcon(getIcon("renamebackground.png"),
				MultiIcon.CENTER);
		if (!file.isDirectory()) {
			processFileDescritpor((FileDescriptor) file);
		} else {
			processDirectoryDescriptor();
		}
	}

	/**
	 * This method handles the labels for {@link DirectoryDescriptor}.
	 */
	protected void processDirectoryDescriptor() {
		multiIcon.addIcon(getIcon("directory-icon.png"), MultiIcon.LEFT);
	}

	/**
	 * This method adjusts the current label for a {@link FileDescriptor}
	 * 
	 * @param descriptor
	 *            Object to gather information from.
	 */
	protected void processFileDescritpor(FileDescriptor descriptor) {
		String extension = descriptor.getExtension();
		multiIcon.addIcon(getExtensionIcon(extension, false), MultiIcon.LEFT);
		if (descriptor.isUnreadable()
				|| descriptor.getStatistic().containsCategory(
						Category.ERROR_CATEGORY)) {
			multiIcon
					.addIcon(getIcon("error-icon-overlay.png"), MultiIcon.LEFT);
		}
		if (descriptor.getStatistic().getProperty(
				NoChangeProperty.PROPERTY_NAME) > 0) {
			multiIcon.addIcon(getIcon("nochange.png"), MultiIcon.BOTTOM_RIGHT);
		}
		// Set targetfilename if flag is not set.
		// source filename will be taken from toString() and is already set.
		if (!sourceRendering) {
			setText(descriptor.getTargetName());
		}
	}
}
