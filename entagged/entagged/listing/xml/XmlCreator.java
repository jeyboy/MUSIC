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
package entagged.listing.xml;

import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;
import entagged.listing.Lister;
import entagged.listing.xml.util.CustomCharacterReplace;
import entagged.listing.xml.util.XmlIndentHelper;

/**
 * This class creates an XML out of multiple {@link entagged.audioformats.AudioFile}
 * insertions. <br>
 * 
 * @author Christian Laireiter
 */
public class XmlCreator implements Lister {

    /**
     * This constant defines the xml attribute name for the xml-tag tag.
     */
    public final static String ATTR_NAME = "name";

    /**
     * This constant defines the name for the xml tag which contains the album.
     */
    public final static String TAG_ALBUM = "album";

    /**
     * This constant defines the name for the xml tag which contains the artist.
     */
    public final static String TAG_ARTIST = "artist";

    /**
     * This constant defines the name for the xml tag which contains the
     * comment.
     */
    public final static String TAG_COMMENT = "comment";

    /**
     * This constant defines the name for the xml tag which contains the file
     * location.
     */
    public final static String TAG_FILE = "file";

    /**
     * This constant defines the name for the xml tag which contains the genre.
     */
    private static final String TAG_GENRE = "genre";

    /**
     * This constant defines the name for the root tag..
     */
    public final static String TAG_ROOT = "audiolist";

    /**
     * This constant defines the name for the xml tag which contains the
     * complete tag information.
     */
    public final static String TAG_TAG = "tag";

    /**
     * This constant defines the name for the xml tag which contains the title.
     */
    public final static String TAG_TITLE = "title";

    /**
     * This constant defines the name for the xml tag which contains the track.
     */
    public final static String TAG_TRACK = "track";

    /**
     * This constant defines the name for the xml tag which contains the year.
     */
    public final static String TAG_YEAR = "year";

    /**
     * This field indicates that the {@link #close()}method was called. <br>
     * If this field is <code>true</code>, no more insertions of tags are
     * possible.
     */
    private boolean closed = false;

    /**
     * This field is stores the helper for creating nice looking xml.
     */
    private XmlIndentHelper xih;

    /**
     * Creates an instance.
     * 
     */
    public XmlCreator() {
        initialize();
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.Lister#addFile(java.lang.String)
     */
    public void addFile(String fileName) {
        if (this.closed) {
            throw new IllegalStateException("Xml creation was already closed.");
        }
        xih.openTagWithAttributes(TAG_FILE, new String[] { ATTR_NAME },
                new String[] { CustomCharacterReplace.replace(fileName) });
        xih.closeTag();
        xih.newlIne();
    }

    /**
     * This method appends the tag to the xml.
     * 
     * @param audioFile
     *                   The file whose Tag is to be inserted.
     * @param fileName
     *                   filename of the file containing <code>tag</code>.
     */
    public void addFile(AudioFile audioFile, String fileName) {
        if (this.closed) {
            throw new IllegalStateException("Xml creation was already closed.");
        }
        Tag tag = audioFile.getTag();

        xih.openTagWithAttributes(TAG_FILE, new String[] { ATTR_NAME },
                new String[] { CustomCharacterReplace.replace(fileName) });
        xih.newlIne();
        xih.openSimpleTag(TAG_TAG);
        xih.newlIne();
        createSimpleTag(TAG_ARTIST, tag.getFirstArtist());
        createSimpleTag(TAG_ALBUM, tag.getFirstAlbum());
        createSimpleTag(TAG_TRACK, tag.getFirstTrack());
        createSimpleTag(TAG_TITLE, tag.getFirstTitle());
        createSimpleTag(TAG_GENRE, tag.getFirstGenre());
        createSimpleTag(TAG_YEAR, tag.getFirstYear());
        createSimpleTag(TAG_COMMENT, tag.getFirstComment());
        xih.closeTag();
        xih.newlIne();
        xih.closeTag();
        xih.newlIne();
    }

    /**
     * This Method closes the creation of the xml. It must be called after the
     * last insertion ({@link #addFile(AudioFile, String)})<br>
     */
    public void close() {
        if (!this.closed) {
            xih.closeTag();
            xih.newlIne();
        }
        this.closed = true;
    }

    /**
     * This method appends a new tag to the xml and assigns the content.
     * 
     * @param tagName
     *                   Name of the tag.
     * @param tagContent
     *                   Value of the tags content.
     */
    private void createSimpleTag(String tagName, String tagContent) {
        if (tagContent != null && tagContent.trim().length() > 0) {
            xih.openSimpleTag(tagName);
            xih.append(CustomCharacterReplace.replace(tagContent), false);
            xih.closeTag(false);
            xih.newlIne();
        }
    }

    /**
     * (overridden)
     * 
     * @see entagged.listing.Lister#getContent()
     */
    public String getContent() {
        return getXML();
    }

    /**
     * This method returns the currently created xml string.
     * 
     * @return currently state of xml creation.
     */
    public String getXML() {
        return this.xih.toString();
    }

    /**
     * Initializes the XMLCreator and writes the XML header.
     */
    private void initialize() {
        this.xih = new XmlIndentHelper();
        xih.append("<?xml version=\'1.0\' encoding=\"UTF-8\"?>", false);
        xih.newlIne();
        xih.append("<!--DOCTYPE audiolist PUBLIC \"ENTAGGED AUDIOLIST "
                + "2004//EN\" \"\"-->", false);
        xih.newlIne();
        xih.openSimpleTag(TAG_ROOT);
        xih.newlIne();
    }

}