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
package entagged.tageditor.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * $Id: PreferencesManager.java,v 1.1 2007/03/23 14:17:16 nicov1 Exp $
 * 
 * @author Raphael Slinckx (KiKiDonK)
 * @version 16 decembre 2003
 */
public class PreferencesManager {
	/** Path to the preferences xml file */
	private final static String DEFAULT_PROPERTIES = "default.preferences";

	private static boolean initialized = false;

	private static Properties prefs;

	public final static File USER_PROPERTIES_DIR = new File(System
			.getProperty("user.home"), ".entagged");

	private final static File USER_PROPERTIES_FILE = new File(
			USER_PROPERTIES_DIR, "user.preferences");

	public static void cleanPreferences() throws Exception {
		if (!USER_PROPERTIES_DIR.exists()) {
			USER_PROPERTIES_DIR.mkdirs();
			System.out.println("Directory created: " + USER_PROPERTIES_DIR);
		}

		assert initialized;
		try {
			prefs.store(new FileOutputStream(USER_PROPERTIES_FILE),
						"Entagged User Preferences. Do not edit this by hand unless you really know what you're doing, in case of problem try deleting this file...");
			System.out.println("Preferences stored in: " + USER_PROPERTIES_FILE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error: the preferences could not be saved in -"
							+ USER_PROPERTIES_FILE + "- :\n" + e);
		}
	}

	public static String get(String key) {
		return prefs.getProperty(key);
	}

	public static boolean getBoolean(String key) {
		String prop = prefs.getProperty(key);
		// FIXME: This may not be a good thing !
		if (prop == null)
			return false;

		return Boolean.valueOf(prop.trim()).booleanValue();
	}

	public static int getInt(String key) {
		String prop = prefs.getProperty(key);
		if (prop == null)
			return -1;

		return Integer.parseInt(prop.trim());
	}

	public static int getInt(String key, int def) {
		String s = prefs.getProperty(key);
		if (s == null)
			return def;

		return Integer.parseInt(s.trim());
	}

	public static int[] getIntArray(String key) {
		int[] result = new int[0];
		String value = prefs.getProperty(key);
		if (value != null && value.length() > 0) {
			String[] splitted = value.split(",");
			ArrayList parsedValues = new ArrayList();
			for (int i = 0; i < splitted.length; i++) {
				if (splitted[i] != null && splitted[i].length() > 0) {
					try {
						parsedValues.add(Integer.valueOf(splitted[i]));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			result = new int[parsedValues.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = ((Integer) parsedValues.get(i)).intValue();
			}
		}
		return result;
	}

	/**
	 * Setup the preferences, creates new one if the file do not exist, else
	 * read the prefs from the file
	 * 
	 */
	public static void initPreferences() throws Exception {
		Properties defaults = null;
		try {
			defaults = ResourcesRepository.getPreferences(DEFAULT_PROPERTIES);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error: Cannot read the Default preferences file -"
							+ DEFAULT_PROPERTIES + "- :\n" + e);
		}
		assert defaults != null;

		// Check if there is an existing preference File
		if (USER_PROPERTIES_FILE.exists()) {
			// Import that preferences file into the preferences
			try {
				prefs = new Properties(defaults);
				prefs.load(new FileInputStream(USER_PROPERTIES_FILE));
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(
						"Error: Cannot read the user preferences file -"
								+ USER_PROPERTIES_FILE + "- :\n" + e);
			}
			assert prefs != null;

			System.out.println("Preferences imported");
		} else {
			// Creates a new pref file with default values
			prefs = new Properties(defaults);
		}
		initialized = true;
	}

	public static void put(String key, String s) {
		prefs.setProperty(key, s);
	}

	public static void putBoolean(String key, boolean b) {
		prefs.setProperty(key, Boolean.toString(b));
	}

	public static void putInt(String key, int i) {
		prefs.setProperty(key, Integer.toString(i));
	}

	public static void putIntArray(String key, int[] values) {
		if (values == null || values.length == 0) {
			prefs.setProperty(key, "");
		} else {
			StringBuffer builder = new StringBuffer();
			for (int i = 0; i < values.length; i++) {
				if (i > 0) {
					builder.append(",");
				}
				builder.append(values[i]);
			}
			prefs.setProperty(key, builder.toString());
		}
	}

}
