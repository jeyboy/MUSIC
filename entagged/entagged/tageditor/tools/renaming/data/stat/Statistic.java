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

package entagged.tageditor.tools.renaming.data.stat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.stat.properties.CanNotWriteProperty;
import entagged.tageditor.tools.renaming.data.stat.properties.DestinationExistsProperty;
import entagged.tageditor.tools.renaming.data.stat.properties.DuplicateErrorProperty;
import entagged.tageditor.tools.renaming.data.stat.properties.NoChangeProperty;
import entagged.tageditor.tools.renaming.data.stat.properties.TagsMissingProperty;

/**
 * This class will help to collect information about a file or a directory.<br>
 * With the {@link #getProperty(String)} method you can get a Statistic object
 * for a given {@link entagged.tageditor.tools.renaming.data.AbstractFile}.<br>
 * There are default statistic instances for directories and audiofiles. They
 * contain {@link entagged.tageditor.tools.renaming.data.stat.Prop} objects
 * which are meant to tell something about the file.<br>
 * For example
 * {@link entagged.tageditor.tools.renaming.data.stat.properties.CanNotWriteProperty}
 * will determine if the existing file or directory can be modified. If so it
 * places a 1 into the corresponding name
 * {@link entagged.tageditor.tools.renaming.data.stat.Prop#getName()}, else 0.<br>
 * With the method {@link #merge(Statistic)} it is possible to count those
 * values. It is so possible to identifiy from a directory how many
 * subdirectories or underlying files cannot be renamed. Then the renaming will
 * need to copy them instead.
 * 
 * @author Christian Laireiter
 */
public final class Statistic {

	/**
	 * This statistics constant is meant to gather default properties for
	 * {@link entagged.tageditor.tools.renaming.data.DirectoryDescriptor}
	 * objects.
	 */
	public final static Statistic DEFAULT_DIR_STAT = new Statistic(new Prop[] {
			new CanNotWriteProperty(), new DestinationExistsProperty(false) });

	/**
	 * This statistics constant is meant to gather default properties for
	 * {@link entagged.tageditor.tools.renaming.data.FileDescriptor} objects.
	 */
	public final static Statistic DEFAULT_FILE_STAT = new Statistic(new Prop[] {
			new CanNotWriteProperty(), new DestinationExistsProperty(true),
			new TagsMissingProperty(), new NoChangeProperty(),
			new DuplicateErrorProperty() });

	/**
	 * This method processes the given files.<br>
	 * 
	 * @param file
	 *            The file, on which information should be gathered.
	 * @return A Statistics object, containing the properties.
	 */
	public static Statistic processFile(AbstractFile file) {
		if (file.isDirectory()) {
			return DEFAULT_DIR_STAT.scanFile(file);
		}
		return DEFAULT_FILE_STAT.scanFile(file);
	}

	/**
	 * This array may contain some {@link Prop} objects for the default
	 * instances.
	 */
	private Prop[] propertyResolvers;

	/**
	 * Hier werden zu den namen der jeweiligen eigenschaften deren Werten
	 * abgelegt.
	 */
	private Properties values;

	/**
	 * Creates an instance.
	 */
	private Statistic() {
		this.values = new Properties();
	}

	/**
	 * Creates an Statistics objects with property operations.
	 * 
	 * @param properties
	 *            Property instances.
	 */
	private Statistic(Prop[] properties) {
		this();
		this.propertyResolvers = properties;
	}

	/**
	 * Creates an instance, with values.
	 * 
	 * @param propertyValues
	 *            The values of the statistic object.
	 * @param used
	 *            The property objects used to make up the values.<br>
	 *            However, this may be empty but not <code>null</code>.
	 */
	private Statistic(Properties propertyValues, Prop[] used) {
		assert propertyValues != null && used != null;
		this.values = propertyValues;
		this.propertyResolvers = used;
	}

	/**
	 * This method looks at all used properties ({@link #propertyResolvers})
	 * and their category. If the value of at least one property is set which is
	 * in the given category, this method returns <code>true</code>.
	 * 
	 * @param toFind
	 *            The category, for which should be looked.
	 * @return <code>true</code> if at least one property is set which was
	 *         assigned to the given category.
	 */
	public boolean containsCategory(Category toFind) {
		boolean result = false;
		for (int i = 0; i < propertyResolvers.length && !result; i++) {
			result = propertyResolvers[i].getCategories().contains(toFind)
					&& (getProperty(propertyResolvers[i].getName()) > 0);
		}
		return result;
	}

	/**
	 * This method returns all {@link Prop} instances, which where used to make
	 * up the current statistics.<br>
	 * 
	 * @return The property objects used to create the statistic object.
	 */
	public Prop[] getProperties() {
		Prop[] result = new Prop[this.propertyResolvers.length];
		System.arraycopy(this.propertyResolvers, 0, result, 0, result.length);
		return result;
	}

	/**
	 * This method returns the {@link Prop} objects, which are assigned to the
	 * given category, if the property is really set in current statistics
	 * object.
	 * 
	 * @param filter
	 *            The category to filer.
	 * @return The property objects of given category, if the current statistics
	 *         object has this property set.
	 */
	public Prop[] getPropertiesOfCategory(Category filter) {
		ArrayList result = new ArrayList();
		for (int i = 0; i < propertyResolvers.length; i++) {
			if (propertyResolvers[i].getCategories().contains(filter)
					&& (getProperty(propertyResolvers[i].getName()) > 0)) {
				result.add(propertyResolvers[i]);
			}
		}
		return (Prop[]) result.toArray(new Prop[result.size()]);
	}

	/**
	 * This method returns the value of the given Property.
	 * 
	 * @param name
	 *            Name of the property, to extract.
	 * @return The value of the property. If <code>0</code>, the property
	 *         hasn't been set and does not exist.
	 */
	public int getProperty(String name) {
		Integer value = (Integer) this.values.get(name);
		if (value != null)
			return value.intValue();
		return 0;
	}

	/**
	 * With this method you can determine, if the given property has been set at
	 * all. {@link #getProperty(String)} would return <code>0</code> if it
	 * hasn't.
	 * 
	 * @param name
	 *            Name of the property
	 * @return <code>true</code>, if property has been set in the current
	 *         statistics object.
	 */
	public boolean hasProperty(String name) {
		return values.containsKey(name);
	}

	/**
	 * This method will merge the current values with those of the given
	 * statistics object.
	 * 
	 * @param stats
	 *            Statistics to merge.
	 * @return New statistics object containing merged values.
	 */
	public Statistic merge(Statistic stats) {
		final Properties mergedProps = new Properties();
		final HashSet processed = new HashSet();
		Iterator iterator = values.keySet().iterator();
		while (iterator.hasNext()) {
			String currentProp = (String) iterator.next();
			processed.add(currentProp);
			mergedProps.put(currentProp, new Integer(getProperty(currentProp)
					+ stats.getProperty(currentProp)));
		}
		iterator = stats.values.keySet().iterator();
		while (iterator.hasNext()) {
			String currentProp = (String) iterator.next();
			if (!processed.contains(currentProp)) {
				mergedProps.put(currentProp, new Integer(
						getProperty(currentProp)
								+ stats.getProperty(currentProp)));
			}
		}
		// Now merge the used prop instances
		Hashtable determinedProps = new Hashtable();
		for (int i = 0; i < propertyResolvers.length; i++) {
			determinedProps.put(propertyResolvers[i].getName(),
					propertyResolvers[i]);
		}
		for (int i = 0; i < stats.propertyResolvers.length; i++) {
			if (!determinedProps.containsKey(stats.propertyResolvers[i]
					.getName())) {
				determinedProps.put(stats.propertyResolvers[i].getName(),
						stats.propertyResolvers[i]);
			}
		}
		return new Statistic(mergedProps, (Prop[]) determinedProps.values()
				.toArray(new Prop[determinedProps.size()]));
	}

	/**
	 * This method will add the given property to the statistics, if not already
	 * contained ({@link #hasProperty(String)}).<br>
	 * 
	 * @param file
	 *            the file, on which the property should be tested.
	 * @param toProcess
	 *            The property to process (determine).
	 */
	public void processProperty(AbstractFile file, Prop toProcess) {
		assert toProcess != null;
		if (!this.hasProperty(toProcess.getName())) {
			values.put(toProcess.getName(),
					new Integer(toProcess.operate(file)));
			Prop[] copy = new Prop[propertyResolvers.length + 1];
			System.arraycopy(propertyResolvers, 0, copy, 0,
					propertyResolvers.length);
			copy[copy.length - 1] = toProcess;
			this.propertyResolvers = copy;
		}
	}

	/**
	 * This method will apply the internal {@link Prop} objects on the given
	 * file and return a statistic object containing the results.
	 * 
	 * @param file
	 *            The file which should be processed.
	 * @return A statistics objects containing the results.
	 */
	private Statistic scanFile(AbstractFile file) {
		Properties result = new Properties();
		for (int i = 0; i < propertyResolvers.length; i++) {
			result.put(propertyResolvers[i].getName(), new Integer(
					propertyResolvers[i].operate(file)));
		}

		return new Statistic(result, propertyResolvers);
	}

	/**
	 * This method will reread the property given by name, if property exists.
	 * 
	 * @param file
	 *            The file instance on which the properties should operate.
	 * @param propertyName
	 *            Name of the property.
	 */
	public void updateProperty(String propertyName, AbstractFile file) {
		for (int i = 0; i < propertyResolvers.length; i++) {
			if (propertyResolvers[i].getName().equals(propertyName)) {
				values.put(propertyResolvers[i].getName(), new Integer(
						propertyResolvers[i].operate(file)));
				break;
			}
		}
	}
}
