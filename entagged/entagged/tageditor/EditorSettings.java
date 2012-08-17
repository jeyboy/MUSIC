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
package entagged.tageditor;

import entagged.tageditor.tools.FileTimePreserver;

/**
 * This class stores global settings and provides some functionality which will
 * be used by mulitple components.<br>
 * 
 * @author Christian Laireiter (liree@web.de)
 */
public final class EditorSettings {

	/**
	 * This method applies global options.<br>
	 * For example, the {@link entagged.tageditor.tools.FileTimePreserver} will
	 * be registered or deregistered to the static instance of
	 * {@link entagged.audioformats.AudioFileIO}, according to the preference.
	 */
	public void prepareAudioProcessing() {
		FileTimePreserver.register(true);
	}

}
