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
package entagged.tageditor.tools.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.Icon;

/**
 * This class allows one to create an Icon out of multiple others. <br>
 * One can insert an arbitrary amount of icons to 5 different locations. The
 * resulting size of the whole icon will be made up of the largest inserted one.<br>
 * The order of inserting is the order of painting.
 * 
 * @author Christian Laireiter (liree)
 */
public class MultiIcon implements Icon {

	/**
	 * Internal information Structure.
	 * 
	 * @author Christian Laireiter
	 */
	private final class IconInfo {
		/**
		 * The represented Icon.
		 */
		Icon icon;

		/**
		 * The location of the icon.
		 */
		int location;

		/**
		 * Creates an instance.
		 * 
		 * @param i
		 *            The icon
		 * @param m
		 *            The location of the Icon.
		 */
		public IconInfo(Icon i, int m) {
			this.icon = i;
			this.location = m;
		}
	}

	/**
	 * This constant is used to place an icon at the bottom left of the whole
	 * icon.
	 */
	public final static int BOTTOM_LEFT = 1;

	/**
	 * This constant is used to place an icon at the bottom right of the whole
	 * icon.
	 */
	public final static int BOTTOM_RIGHT = 2;

	/**
	 * This constant is used to place an icon at the Center of the whole icon.
	 */
	public final static int CENTER = 0;

	/**
	 * This constant is used to place an icon at the left side of the whole
	 * icon.
	 */
	public final static int LEFT = 5;

	/**
	 * This constant is used to place an icon at the top left of the whole icon.
	 */
	public final static int TOP_LEFT = 3;

	/**
	 * This constant is used to place an icon at the top right of the whole
	 * icon.
	 */
	public final static int TOP_RIGHT = 4;

	/**
	 * Stores the other icons wich will be painted.
	 */
	private final ArrayList icons = new ArrayList();

	/**
	 * Stores the height of the tallest inserted icon.
	 */
	private int maxHeight;

	/**
	 * Stores the width of the widest inserted icon.
	 */
	private int maxWidth;

	/**
	 * Adds an icon to this collection.<br>
	 * <b>consider</b>: The order you insert the icon will be the order the
	 * icons are painted. If you insert the largest icon in the center and at
	 * last, you will only see that.
	 * 
	 * @param toAdd
	 *            An icon which should be inserted. This may be
	 *            <code>null</code> if you want to skip the position the Icon
	 *            would be placed at.
	 * @param location
	 *            the location of the added icon within the resulting icon.
	 */
	public void addIcon(Icon toAdd, int location) {
		assert toAdd != this : "Would cause stackoverflow.";
		icons.add(new IconInfo(toAdd, location));
		if (toAdd != null && toAdd.getIconHeight() > maxHeight) {
			maxHeight = toAdd.getIconHeight();
		}
		if (toAdd != null && toAdd.getIconWidth() > maxWidth) {
			maxWidth = toAdd.getIconWidth();
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return maxHeight;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return maxWidth;
	}

	/**
	 * (overridden)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 *      int, int)
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		for (int i = 0; i < icons.size(); i++) {
			IconInfo current = (IconInfo) icons.get(i);
			if (current == null) {
				continue;
			}
			int relX = -1;
			int relY = -1;
			switch (current.location) {
			case 0:
				relX = (maxWidth - current.icon.getIconWidth()) / 2;
				relY = (maxHeight - current.icon.getIconHeight()) / 2;
				break;
			case 1:
				relX = 0;
				relY = maxHeight - current.icon.getIconHeight();
				break;
			case 2:
				relX = maxWidth - current.icon.getIconWidth();
				relY = maxHeight - current.icon.getIconHeight();
				break;
			case 3:
				relX = 0;
				relY = 0;
				break;
			case 4:
				relX = maxWidth - current.icon.getIconWidth();
				relY = 0;
				break;
			case 5:
				relX = 0;
				relY = (maxHeight - current.icon.getIconHeight()) / 2;
				break;
			}
			current.icon.paintIcon(c, g, x + relX, y + relY);
		}
	}

	/**
	 * clears all icons.
	 */
	public void reset() {
		maxHeight = -1;
		maxWidth = -1;
		icons.clear();
	}

}
