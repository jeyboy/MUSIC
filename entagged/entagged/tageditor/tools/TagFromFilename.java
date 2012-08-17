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

import java.io.File;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotWriteException;
import entagged.audioformats.mp3.Id3v2Tag;
import entagged.tageditor.exceptions.OperationException;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.tools.stringtransform.TransformSet;

/**
 * This class implements some functionality for renaming (moving) audiofiles by
 * their tag values using directory- and filenamepatterns. Additionally the
 * other way is provided, change tag entries from filename using a pattern too.
 * <br>
 * 
 * Description of the Class. $Id: TagFromFilename.java,v 1.39 2005/01/10 23:00:33
 * kikidonk Exp $
 * 
 * @author Raphael Slinckx (KiKiDonK)
 */
public class TagFromFilename {

    public static final String YEAR_MASK = "<year>";
    public static final String TITLE_MASK = "<title>";
    public static final String TRACK_MASK = "<track>";
    public static final String GENRE_MASK = "<genre>";
    public static final String IGNORE_MASK = "<ignore>";
    public static final String COMMENT_MASK = "<comment>";
    public static final String ALBUM_MASK = "<album>";
    public static final String ARTIST_MASK = "<artist>";
    public static final String BITRATE_MASK = "<bitrate>";
    
	
    private String directoryPattern;

    private String filenamePattern;

    // --------------------------------------------------------------------------


    // ENDS TAG TRANSFO+WRITE------------------------

    public String getRawExtension(File f) {
        String name = f.getName();
        int i = name.lastIndexOf(".");

        if (i == -1)
            return "";

        return name.substring(i + 1).toLowerCase();
    }


    // Escapes all regular expresion special characters [\^$.|?*+()
    // in order to match a single instance of themselves
    private String replaceReservedRegexCharacters(String text) {
        text = text.replaceAll("\\\\", "\\\\\\\\");
        text = text.replaceAll("\\[", "\\\\\\[");
        text = text.replaceAll("\\^", "\\\\\\^");
        text = text.replaceAll("\\$", "\\\\\\$");
        text = text.replaceAll("\\.", "\\\\\\.");
        text = text.replaceAll("\\?", "\\\\\\?");
        text = text.replaceAll("\\*", "\\\\\\*");
        text = text.replaceAll("\\+", "\\\\\\+");
        text = text.replaceAll("\\(", "\\\\\\(");
        text = text.replaceAll("\\)", "\\\\\\)");
        return text;
    }

    public void setDirectoryPattern(String pattern) {
        this.directoryPattern = pattern;
    }

    public void setFilenamePattern(String pattern) {
        this.filenamePattern = pattern;
    }
//
//    private String stripReservedDirectoryCharacters(String rawString) {
//        if (rawString.length() > 2
//                && rawString.substring(0, 3).matches("[a-zA-Z]:\\\\")) {
//            if (rawString.length() > 3)
//                rawString = rawString.substring(0, 3)
//                        + rawString.substring(3).replaceAll(":", "-");
//        } else
//            rawString = rawString.replaceAll(":", "-");
//           
//        // on Win32, only the following characters are reserved: \ / : * ? " < > |
//        rawString = rawString.replaceAll("\\?", "");
//        rawString = rawString.replaceAll("\\*", "-");
//        rawString = rawString.replaceAll("\"", "'");
//        //rawString = rawString.replaceAll("\\'", "");
//        rawString = rawString.replaceAll("<", "-");
//        rawString = rawString.replaceAll(">", "-");
//        rawString = rawString.replaceAll("\\|", "-");
//        //rawString = rawString.replaceAll("\\!", "");
//        //rawString = rawString.replaceAll("%", "");
//        return rawString;
//    }

    // END FILENAME TO TAG--------------------

    // BEGINS TAG FROM FILENAME------------------------
    public void tagFromFilename(AudioFile f, TransformSet transformSet) throws OperationException {
        String filename = this.filenamePattern.toLowerCase();
        String directory = this.directoryPattern.toLowerCase();
        String extension = getRawExtension(f);

        Hashtable filenameHash = new Hashtable();
        Hashtable directoryHash = new Hashtable();

        String[][] fields = { { ARTIST_MASK, "" }, { ALBUM_MASK, "" },
                { TITLE_MASK, "" }, { TRACK_MASK, "" }, { GENRE_MASK, "" },
                { YEAR_MASK, "" }, { COMMENT_MASK, "" }, { IGNORE_MASK, "" } };

        // fields.length-1 so we don't get the ignore masks
        for (int i = 0; i < fields.length - 1; i++) {
            int filei = filename.lastIndexOf(fields[i][0]);
            int diri = directory.lastIndexOf(fields[i][0]);

            if ((filename.indexOf(fields[i][0]) != filei)
                    || (directory.indexOf(fields[i][0]) != diri)) {
                throw new OperationException("Cannot have two \""
                        + fields[i][0] + "\" masks in the same pattern");
            }

            if (filei != -1 && diri != -1) {
                throw new OperationException("Cannot have two \""
                        + fields[i][0]
                        + "\" masks in both filename and directory pattern");
            }

            filenameHash.put(new Integer(filei), new Integer(i));

            directoryHash.put(new Integer(diri), new Integer(i));
        }

        directory = replaceReservedRegexCharacters(directory);
        filename = replaceReservedRegexCharacters(filename);

        for (int i = 0; i < fields.length; i++) {
            if (fields[i][0].equals(IGNORE_MASK)) {
                filename = filename.replaceAll(fields[i][0], "(.+)");
                directory = directory.replaceAll(fields[i][0], "(.+)");
            } else {
                filename = filename.replaceAll(fields[i][0], "(.+?)");
                directory = directory.replaceAll(fields[i][0], "(.+?)");
            }

        }

        filename = filename + "." + extension;
        if (directory.endsWith(File.separator))
            directory = directory.substring(0, directory.length()
                    - File.separator.length());

        // Filename pattern matching ------------------------------------------
        System.out.println("Matching:\n" + f.getName() + "\nagainst:\n"
                + filename);
        Pattern pa = Pattern.compile(filename);
        Matcher ma = pa.matcher(f.getName().toLowerCase());

        int length = 0;
        if (ma.matches()) {
            for (int i = 1; i <= ma.groupCount(); i++) {
                // we can find a group that is not in the hashtable, if the user
                // entered %1-%1, for ex.
                // We work only with multiple ignore masks, not other..
                Integer pos = (Integer) filenameHash.get(new Integer(ma
                        .start(i)
                        + length));
                if (pos != null) {
                    fields[pos.intValue()][1] = ma.group(i);
                    System.out.println("Match " + i + ": " + ma.group(i));
                    length = length - ma.group(i).length()
                            + fields[pos.intValue()][0].length();
                } else {
                    // It was an ignore mask ok, or a double mask then we will
                    // fail
                    length = length - ma.group(i).length()
                            + fields[7][0].length();
                }
            }
        } else
            throw new OperationException(LangageManager
                    .getProperty("opex.fpattern"));
        // Filename matching finished
        // ---------------------------------------------------------------------

        // Directory name pattern
        // matching--------------------------------------------------------------
        System.out.println("Matching:\n" + f.getParentFile().getPath()
                + "\nagainst:\n" + directory);
        pa = Pattern.compile(directory);
        ma = pa.matcher(f.getParent().toLowerCase());

        length = 0;
        if (ma.matches()) {
            for (int i = 1; i <= ma.groupCount(); i++) {
                // we can find a group that is not in the hashtable, if the user
                // entered %1-%1, for ex.
                // We work only with multiple ignore masks, not other..
                Integer pos = (Integer) directoryHash.get(new Integer(ma
                        .start(i)
                        + length));
                if (pos != null) {
                    fields[pos.intValue()][1] = ma.group(i);
                    System.out.println("Match " + i + ": " + ma.group(i));
                    length = length - ma.group(i).length()
                            + fields[pos.intValue()][0].length();
                } else {
                    // It was an ignore mask ok, or a double mask then we will
                    // fail
                    length = length - ma.group(i).length()
                            + fields[7][0].length();
                }
            }
        } else
            throw new OperationException(LangageManager
                    .getProperty("opex.dpattern"));
        // Directory matching finished
        // ---------------------------------------------------------------------

        // !!! Here we have to respect to order we defined in our field to know
        // Wich fields correspond to wich index i
        /*
         * We defined it like that String[][] fields = { { ARTIST_MASK , ""}, {
         * ALBUM_MASK , ""}, { TITLE_MASK , ""}, { TRACK_MASK , ""}, {
         * GENRE_MASK , ""}, { YEAR_MASK , ""}, { COMMENT_MASK , ""} };
         */

        Tag tag = f.getTag();
        for (int i = 0; i < fields.length; i++) {
            String s = fields[i][1].trim();
            if (!s.equals("")) {
                s = transformSet.transform(s);
                switch (i) {
                case 0:
                    tag.setArtist(s);
                    break;
                case 1:
                    tag.setAlbum(s);
                    break;
                case 2:
                    tag.setTitle(s);
                    break;
                case 3:
                    tag.setTrack(s);
                    break;
                case 4:
                    tag.setGenre(s);
                    break;
                case 5:
                    tag.setYear(s);
                    break;
                case 6:
                    tag.setComment(s);
                    break;
                case 7:
                    break; // Ignore
                }
            }
        }

        transformSet.transformFirstCommons(tag);

        if(tag instanceof Id3v2Tag)
            tag.setEncoding(PreferencesManager.get("entagged.tag.encoding"));
        
        try {
            AudioFileIO.write(f);
        } catch (CannotWriteException e) {
            throw new OperationException(e.getMessage());
        }
    }

    // END TAG FROM FILENAME --------------------
   

}