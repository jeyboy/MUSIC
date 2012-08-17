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
package entagged.listing.gui.statistic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import entagged.listing.statistics.Statistic;
import entagged.listing.statistics.Statistic.StringCountRecord;
import entagged.tageditor.resources.LangageManager;

/**
 * Creates a panel which shows a given category of a
 * {@link entagged.listing.statistics.Statistic}object. <br>
 * 
 * @author Christian Laireiter (liree)
 */
class CategoryPanelCreator {

	/**
	 * This comparator is meant to sort numbers.<br>
	 * since "56" is greater than "112" from the point of lexically sorting, we
	 * need an implementation which interprets strings as numbers.
	 * 
	 * @author Christian Laireiter
	 */
	private final class NumberSorter implements Comparator {

		/**
		 * (overridden)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			int result = 0;
			if (o1 instanceof StringCountRecord
					&& o2 instanceof StringCountRecord) {
				StringCountRecord sc1 = (StringCountRecord) o1;
				StringCountRecord sc2 = (StringCountRecord) o2;
				result = new Integer(sc1.getString()).compareTo(new Integer(sc2
						.getString()));
			} 
			return result;
		}

	}

	/**
	 * Constant instace for sorting the bitrates (for now).
	 */
	public final static NumberSorter NUMBER_SORTER = new CategoryPanelCreator().new NumberSorter();

	/**
	 * This method will parse the values of the given records to numbers and
	 * compress them. <br>
	 * <54,64,96,112,128,160,192,256,512,>512 If the current Bitrate is more
	 * than 5 less as a listed value, the output will be the next lower vlaue.
	 * Else it will be the listed value.
	 * 
	 * @param records
	 * @return Strings
	 */
	protected static String[] compressBitrates(StringCountRecord[] records) {
		int[] classes = new int[] { 54, 64, 96, 112, 128, 160, 192, 256, 512 };
		/*
		 * +2 because of [0] = < classes[0] [classes.length] = >
		 * classes[classes.length-1]
		 */
		int[] counts = new int[classes.length + 2];
		for (int i = 0; i < records.length; i++) {
			int current = Integer.parseInt(records[i].getString());
			if (current < classes[0] - 5)
				counts[0] += records[i].getCounter();
			else if (current > classes[classes.length - 1])
				counts[counts.length - 1] += records[i].getCounter();
			else {
				for (int j = 0; j < classes.length; j++) {
					if (current >= classes[j] - 5 && current <= classes[j] + 5) {
						counts[j + 1] += records[i].getCounter();
						break;
					} else if (current < classes[j] - 5) {
						counts[j] += records[i].getCounter();
						break;
					}
				}
			}
		}
		ArrayList result = new ArrayList();
		if (counts[0] > 0) {
			result.add("<"
					+ classes[0]
					+ " ("
					+ counts[0]
					+ " "
					+ LangageManager
							.getProperty("statistic.occurrencedescriptor")
					+ ")");
		}
		for (int i = 0; i < classes.length - 1; i++) {
			if (counts[i + 1] > 0) {
				result.add(classes[i]
						+ " ("
						+ counts[i + 1]
						+ " "
						+ LangageManager
								.getProperty("statistic.occurrencedescriptor")
						+ ")");
			}
		}
		if (counts[counts.length - 1] > 0) {
			result.add("<"
					+ classes[classes.length - 1]
					+ " ("
					+ counts[counts.length - 1]
					+ " "
					+ LangageManager
							.getProperty("statistic.occurrencedescriptor")
					+ ")");
		}
		return (String[]) result.toArray(new String[0]);
	}

	/**
	 * This method does mostly the same as
	 * {@link #createFor(Statistic, String, String)}. The difference is its
	 * specialisation for showing used bitrates. <br>
	 * 
	 * @param statistic
	 *            The statistics object.
	 * @return A panel showing the bitrates.
	 */
	public static JPanel createBitratePanel(final Statistic statistic) {
		JPanel result = new JPanel(new GridBagLayout());
		StringCountRecord[] records = statistic
				.getRecords(Statistic.MAP_BITRATE);
		Arrays.sort(records, NUMBER_SORTER);
		final DefaultListModel listModel = new DefaultListModel();
		for (int i = 0; i < records.length; i++) {
			listModel.addElement(records[i]);
		}
		final JList list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JLabel desc = new JLabel(LangageManager
				.getProperty("statistic.categorydescription.bitrate"));
		final JCheckBox compressBox = new JCheckBox(LangageManager
				.getProperty("statistic.compressbitrates"));
		compressBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (compressBox.isSelected()) {
					StringCountRecord[] recs = statistic
							.getRecords(Statistic.MAP_BITRATE);
					String[] compressed = compressBitrates(recs);
					listModel.removeAllElements();
					for (int i = 0; i < compressed.length; i++) {
						listModel.addElement(compressed[i]);
					}
				} else {
					StringCountRecord[] recs = statistic
							.getRecords(Statistic.MAP_BITRATE);
					Arrays.sort(recs, NUMBER_SORTER);
					listModel.removeAllElements();
					for (int i = 0; i < recs.length; i++) {
						listModel.addElement(recs[i]);
					}
				}
				list.repaint();
			}
		});
		result.add(compressBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		result.add(desc, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		result.add(new JScrollPane(list), new GridBagConstraints(0, 2, 1, 1,
				1.0, 10.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		return result;
	}

	/**
	 * Creates a panel which displays the category.
	 * 
	 * @param statistic
	 *            The statistic object which contains the data.
	 * @param category
	 *            The category to be extracted.
	 * @param description
	 *            a description for what the user will see.
	 * @return A panel which displays the category.
	 */
	public static JPanel createFor(Statistic statistic, String category,
			String description) {
		JPanel result = new JPanel(new GridBagLayout());
		StringCountRecord[] records = statistic.getRecords(category);
		Arrays.sort(records);
		JList list = new JList(records);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JLabel desc = new JLabel(description);
		result.add(desc, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		result.add(new JScrollPane(list), new GridBagConstraints(0, 1, 1, 1,
				1.0, 10.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		return result;
	}
}
