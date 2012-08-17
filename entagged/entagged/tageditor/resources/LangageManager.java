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

import java.util.Properties;

/**
 *  $Id: LangageManager.java,v 1.1 2007/03/23 14:17:15 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    16 décembre 2003
 */
public class LangageManager {
	/**  Langage resource */
	public static Properties langage;
	public static Properties defaultLangage;
	//default lang file, to use it in case some key does not exist in the active lang file 
	// That way not updated translations are usable
	private static final String defaulLang = "english.lang";
	private static boolean initialized = false;

	public static String getProperty(String s) {
		assert initialized;
		String res = langage.getProperty(s);

		if ((res == null || "".equals(res)))
			if (defaultLangage != null)
				res = defaultLangage.getProperty(s);
		if ((res == null || "".equals(res)))
			res = "** " + s + " **";
		return res;
	}

	public static void initLangage() {
		try {
			langage = ResourcesRepository.getLangage(PreferencesManager.get("entagged.langage"));
			initialized = true;
			defaultLangage = ResourcesRepository.getLangage(defaulLang);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
