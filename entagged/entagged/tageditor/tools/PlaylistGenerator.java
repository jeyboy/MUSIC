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
package entagged.tageditor.tools;

import entagged.audioformats.*;

import java.io.*;

/**
 *  $Id: PlaylistGenerator.java,v 1.1 2007/03/23 14:16:43 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    16 d?cembre 2003
 */
public class PlaylistGenerator {

	private static String outputFile = "album";


	public static void generateM3U( AudioFile[] alb, String outputDir ) {
		StringBuffer buf = new StringBuffer();

		buf.append( "#EXTM3U\n" );
		for ( int i = 1; i <= alb.length; i++ ) {
			AudioFile track = alb[ i - 1];

			buf.append( "#EXTINF:" + track.getLength() + "," + track.getTag().getFirstArtist() + " - " + track.getTag().getFirstAlbum() + " - " + track.getTag().getFirstTitle() + "\n" );
			buf.append( track.getPath() + "\n" );
		}

		try {
			File f = new File( outputDir + "/" + outputFile + ".m3u" );

			if ( f.exists() )
				f.delete();

			RandomAccessFile raf = new RandomAccessFile( f, "rw" );

			raf.write( buf.toString().getBytes( "ISO-8859-1" ) );
			raf.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}


	public static void generatePLS( AudioFile[] alb, String outputDir ) {
		StringBuffer buf = new StringBuffer();

		buf.append( "[playlist]\n" );
		for ( int i = 1; i <= alb.length; i++ ) {
			AudioFile track = alb[i - 1];

			buf.append( "File" + i + "=" + track.getPath() + "\n" );
			buf.append( "Title" + i + "=" + track.getTag().getArtist() + " - " + track.getTag().getAlbum() + " - " + track.getTag().getTitle() + "\n" );
			buf.append( "Length" + 1 + "=" + track.getLength() + "\n" );
		}
		buf.append( "NumberOfEntries=" + alb.length + "\n" );
		buf.append( "Version=2" );

		try {
			File f = new File( outputDir + "/" + outputFile + ".pls" );

			if ( f.exists() )
				f.delete();

			RandomAccessFile raf = new RandomAccessFile( f, "rw" );

			raf.write( buf.toString().getBytes( "ISO-8859-1" ) );
			raf.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

}

