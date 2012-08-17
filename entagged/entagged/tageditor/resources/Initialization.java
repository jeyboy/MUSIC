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

import entagged.tageditor.resources.InitializationMonitor;

import java.awt.Toolkit;


/**
 *  $Id: Initialization.java,v 1.1 2007/03/23 14:17:16 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK) ; Nicolas Velin
 * @version    16 decembre 2003
 */
public class Initialization {
	
	public static boolean isInitialized = false;

	public static void exit() {
		/*
		//!!!!!!!!!SPLASH SOUND!!!!!!!!!!!!!!!
		File splashAudioFile = new File("../resources/splash-rev.wav");
		try {
			AudioClip splashAudio = Applet.newAudioClip( splashAudioFile.toURL() );
			splashAudio.play();
		} catch (MalformedURLException e) {
			System.out.println("Failed to open splash sound, proceeding...");
		}
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		*/
		try{
			PreferencesManager.cleanPreferences();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		miage.sgbd.Connexion.getInstance().shutdown();
		
		UserComboBoxStringsManager.exit();
		System.out.println( "Thanks for using entagged - tag editor" );
		System.exit( 0 );
	}


	public static void init( InitializationMonitor monitor ) {
		monitor.setBounds( 0, 100 );
		monitor.setStatus( "Loading Entagged...", 0 );

		monitor.setStatus( "Loading Preferences", 5 );
		try{
			PreferencesManager.initPreferences();
		}catch (Exception e) {
			System.out.println("FATAL ERROR: could not load preferences, try reinstalling entagged, or deleting by hand the preference file");
			System.out.println(e);
			System.out.println("...EXITING...");
			exit();
		}
		UserComboBoxStringsManager.init();
		monitor.setStatus( "Preferences Loaded", 10 );
		
		/*
		//!!!!!!!!!SPLASH SOUND!!!!!!!!!!!!!!!
		File splashAudioFile = new File("../resources/splash.wav");
		try {
			AudioClip splashAudio = Applet.newAudioClip( splashAudioFile.toURL() );
			splashAudio.play();
		} catch (MalformedURLException e) {
			System.out.println("Failed to open splash sound, proceeding...");
		}
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		*/
		
		monitor.setStatus( "Loading Langage", 5 );
		LangageManager.initLangage();
		monitor.setStatus( "Langage Loaded", 10 );

		monitor.setStatus( "Loading LAF", 5 );
		String laf = PreferencesManager.get( "entagged.lookandfeel" );
		LAFManager.setupLookAndFeel( laf );
		monitor.setStatus( "LAF Loaded", 20 );

		
		monitor.setStatus( "Loading Misc.inits", 15 );
		PreferencesManager.putInt( "entagged.screen.width", (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		PreferencesManager.putInt( "entagged.screen.height",(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		if(PreferencesManager.get("entagged.freedb.login").equals(""))
		    PreferencesManager.put("entagged.freedb.login", System.getProperty("user.name"));
		if(PreferencesManager.get("entagged.freedb.domain").equals(""))
		    PreferencesManager.put("entagged.freedb.domain", System.getProperty("os.name"));
		monitor.setStatus( "Misc. inits Loaded", 10 );
		
		monitor.setStatus( "Loading Interface", 5 );
		
		isInitialized = true;
	}

}

