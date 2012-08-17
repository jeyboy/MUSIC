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
package entagged.freedb;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

//$Id: FreedbReadResult.java,v 1.1 2007/03/23 14:16:58 nicov1 Exp $
public class FreedbReadResult implements Comparable {

    /**
     * Creates a special data structure that allows to concatenate multiples
     * strings at once. I created this because when the freedb Read query
     * arrives, there are no means of knowing how many fields there will be, how
     * many extd track comments there will be, etc. Here is how it works :<br>
     * An ExtensibleStringList is created with a given number of "boxes" that
     * contains an empty string. Each times the concat method is called, the
     * box[index] receive a new bit of string that it concatenates to the
     * existing one and set the modified flag. After the "filling" process, the
     * trim() method is called. It takes only the boxese that have been filled
     * and reenter those in a new array.
     * 
     * @author Rapha�l Slinckx (KiKiDonK)
     * @version v0.03
     */
    private static class ExtensibleStringList {

        /** An array of of elements contained in this List */
        private ExtensibleStringListElement[] l;

        /** The number of filled boxes */
        private int validElements = 0;

        /**
         * @param dim
         *                  the dimension of the list (typically the number of lines
         *                  the server returned)
         */
        public ExtensibleStringList(int dim) {
            this.l = new ExtensibleStringListElement[dim];
            for (int i = 0; i < this.l.length; i++)
                this.l[i] = new ExtensibleStringListElement("");
        }

        /**
         * Concatenates a bit of string in the given box and set the modified
         * flag
         * 
         * @param s
         *                  the string bit to concatenate
         * @param index
         *                  the index of the box to be filled
         */
        public void concat(String s, int index) {
            this.l[index].setContent(this.l[index].getContent() + s);
            this.l[index].setTrackNumber(index);
            if (!this.l[index].isModified())
                this.validElements++;
            this.l[index].setModified();
        }

        /**
         * Trim the current Elements array into a new array that fits exactly
         * the modified elements
         * 
         * @return The final array containing only the interesting fields
         */
        public String[] trim() {
            String[] s = new String[this.validElements];
            int j = 0;

            for (int i = 0; i < this.l.length; i++)
                if (this.l[i].isModified()) {
                    s[j] = this.l[i].getContent();
                    j++;
                }
            return s;
        }
        
        /**
         * This method returns the indices used with {@link #concat(String, int)}
         * @return 
         */
        public int[] getTrackNumbers () {
            int[] result = new int[validElements];
            int j = 0;

            for (int i = 0; i < this.l.length; i++)
                if (this.l[i].isModified()) {
                    result[j] = this.l[i].getTrackNumber();
                    j++;
                }
           return result;
        }

        /**
         * Gets the current String in the given box
         * 
         * @param i
         *                  the index of the box
         * @return the string in that box
         */
        /*
         * 
         * private String get( int i ) { return this.l[i].getContent(); }
         */
    }

    /**
     * Creates an Element of the Extensible List that contains a String and a
     * flag.
     * 
     * @author Rapha�l Slinckx (KiKiDonK)
     * @version v0.03
     */
    private static class ExtensibleStringListElement {

        /** The tracknumber of the represented track */
        private int trackNumber;
        
        /** The content of this box */
        private String content;

        /** Flag indicating if this box has been filled */
        private boolean modified = false;

        /**
         * Creates a new Box with the given String
         * 
         * @param s
         *                  the content of this Box
         */
        public ExtensibleStringListElement(String s) {
            this.content = s;
        }

        /**
         * Get the Content of this Box
         * 
         * @return the string contained in this box
         */
        public String getContent() {
            return this.content;
        }

        /**
         * Get the value of the Flag for this Box
         * 
         * @return the Flag value
         */
        public boolean isModified() {
            return this.modified;
        }

        /**
         * Set a new string in this box
         * 
         * @param s
         *                  the new string contained in this box
         */
        public void setContent(String s) {
            this.content = s;
        }

        /**
         * Set the modified Flag to true for this Box meaning that a content has
         * been added
         */
        public void setModified() {
            this.modified = true;
        }
        public int getTrackNumber() {
            return trackNumber;
        }
        public void setTrackNumber(int trackNumber) {
            this.trackNumber = trackNumber;
        }
    }

    private boolean exactMatch;

    private Hashtable fields;

    /**
     * This field holds the length of each track in seconds, if the comment of
     * the result could be parsed. <br>
     * FreeDb org just tells that the track offsets should be in the comment and
     * specify a form. However since it is a comment it could be ignored by
     * applications that insert into the database. <br>
     * Finally if the durations could not be determined (say not given or error
     * from the programmer (ME)) this field hold <code>null</code>.
     */
    private int[] trackDurations;

    public FreedbReadResult(String freedbReadResult, boolean exactMatch) {
        this.fields = new Hashtable();
        this.exactMatch = exactMatch;

        this.fields.put("DISCID", "");
        this.fields.put("DTITLE", "");
        this.fields.put("DYEAR", "");
        this.fields.put("DGENRE", "");
        this.fields.put("EXTD", "");
        this.fields.put("PLAYORDER", "");
        this.fields.put("TITLE", new String[0]);
        this.fields.put("EXTT", new String[0]);
        this.fields.put("NUMBERING",new int[0]);

        trackDurations = extractTrackDurations(freedbReadResult);

        StringTokenizer st = new StringTokenizer(freedbReadResult, "\n");

        ExtensibleStringList TITLE = new ExtensibleStringList(st.countTokens());
        ExtensibleStringList EXTT = new ExtensibleStringList(st.countTokens());

        String[] answers = new String[st.countTokens()];

        for (int i = 0; i < answers.length; i++)
            answers[i] = st.nextToken();

        int j = 1;

        while (answers[j].substring(0, 1).equals("#"))
            j++;
        for (int i = j; i < answers.length - 1; i++)
            if (answers[i].substring(0, 4).equals("EXTD"))
                this.fields.put("EXTD", this.fields.get("EXTD")
                        + answers[i].substring(5));

            else if (answers[i].substring(0, 4).equals("EXTT")) {
                st = new StringTokenizer(answers[i], "=");

                String field = st.nextToken();
                int index = Integer.parseInt(field.substring(4));

                try {
                    EXTT.concat(st.nextToken(), index);
                } catch (NoSuchElementException e) {
                    //e.printStackTrace();
                    //System.out.println("No extended track info for track
                    // "+(index+1));
                    EXTT.concat("", index);
                }
            }

            else if (answers[i].substring(0, 5).equals("DYEAR"))
                this.fields.put("DYEAR", this.fields.get("DYEAR")
                        + answers[i].substring(6));

            else if (answers[i].substring(0, 6).equals("TTITLE")) {
                st = new StringTokenizer(answers[i], "=");

                String field = st.nextToken();
                int index = Integer.parseInt(field.substring(6));

                TITLE.concat(st.nextToken(), index);
            }

            else if (answers[i].substring(0, 6).equals("DISCID"))
                this.fields.put("DISCID", this.fields.get("DISCID")
                        + answers[i].substring(7));
            else if (answers[i].substring(0, 6).equals("DTITLE"))
                this.fields.put("DTITLE", this.fields.get("DTITLE")
                        + answers[i].substring(7));
            else if (answers[i].substring(0, 6).equals("DGENRE"))
                this.fields.put("DGENRE", this.fields.get("DGENRE")
                        + answers[i].substring(7));
            else if (answers[i].substring(0, 9).equals("PLAYORDER"))
                this.fields.put("PLAYORDER", this.fields.get("PLAYORDER")
                        + answers[i].substring(10));

        this.fields.put("TITLE", TITLE.trim());
        this.fields.put("EXTT", EXTT.trim());
        this.fields.put("NUMBERING",TITLE.getTrackNumbers());

        String[] info = ((String) this.fields.get("DTITLE")).split(" / ", 2);
        this.fields.put("ARTIST", info[0]);
        this.fields.put("ALBUM", (info.length > 1) ? info[1] : "");
        this.fields.put("TRACKNUMBER", new Integer(((String[]) this.fields
                .get("EXTT")).length));
    }

    public FreedbReadResult(String freedbReadResult, String genre) {
        this(freedbReadResult, true);
        this.fields.put("CAT", genre);
    }

    public int compareTo(Object o) {
        if (o == null)
            return 1;
        FreedbReadResult rr = (FreedbReadResult) o;

        if (this.getQuality() == rr.getQuality()) {
            //We have an ex-aequo, trying to determine the winner by
            // genre/comment length and exttrackinfo presence
            //match 1: genre length:
            int win = 0;
            if (this.getGenre().length() > rr.getGenre().length())
                win++;
            else if (this.getGenre().length() < rr.getGenre().length())
                win--;

            //match 2: comment length
            if (this.getAlbumComment().length() > rr.getAlbumComment().length())
                win++;
            else if (this.getAlbumComment().length() < rr.getAlbumComment()
                    .length())
                win--;

            //match 3: extinfopresence
            int nb1 = getTracksNumber();
            int count1 = 0;
            for (int i = 0; i < nb1; i++)
                if (!getTrackComment(i).equals(""))
                    count1++;
            int nb2 = rr.getTracksNumber();
            int count2 = 0;
            for (int i = 0; i < nb2; i++)
                if (!rr.getTrackComment(i).equals(""))
                    count2++;
            if (count1 > count2)
                win++;
            else if (count1 < count2)
                win--;

            return win;

        }

        return this.getQuality() - rr.getQuality();
    }

    /**
     * @param freedbReadResult
     * @return
     */
    private int[] extractTrackDurations(String freedbReadResult) {
        int[] result = null;
        try {
            final String startString = "# Track frame offsets:";
            final String endString = "# Disc length:";
            int startIndex = freedbReadResult.indexOf(startString);
            int endIndex = freedbReadResult.indexOf(endString);
            if (startIndex >= 0 && endIndex > startIndex) {
                String[] splitted = freedbReadResult.substring(startIndex,
                        endIndex).split("\n");
                ArrayList trackOffsets = new ArrayList();
                // Start at 1 because the first line contains startString
                for (int i = 1; i < splitted.length; i++) {
                    String curr = splitted[i];
                    if (curr.startsWith("#")) {
                        curr = curr.substring(1);
                    }
                    curr = curr.trim();
                    if (curr.length() == 0) {
                        break; // This is the end of the track list.
                    }
                    trackOffsets.add(new Integer(curr));
                }
                /*
                 * Now we have all track offsets and we need the disc length to
                 * determine even the length of the last track.
                 */
                int startPointer = endIndex + endString.length();
                char current = freedbReadResult.charAt(startPointer);
                StringBuffer numBuf = new StringBuffer();
                while (Character.isWhitespace(current) && numBuf.length() == 0
                        || Character.isDigit(current)) {
                    if (Character.isDigit(current)) {
                        numBuf.append(current);
                    }
                    current = freedbReadResult.charAt(++startPointer);
                }
                int discLength = Integer.parseInt(numBuf.toString());
                result = new int[trackOffsets.size()];
                int i = 1;
                for (; i < trackOffsets.size(); i++) {
                    result[i - 1] = ((Integer) trackOffsets.get(i)).intValue()
                            - ((Integer) trackOffsets.get(i - 1)).intValue();
                    result[i - 1] /= 75; // Make seconds of frames.
                }
                result[i - 1] = discLength
                        - ((Integer) trackOffsets.get(i - 1)).intValue() / 75;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public String getAlbum() {
        return (String) this.fields.get("ALBUM");
    }

    public String getAlbumComment() {
        return (String) this.fields.get("EXTD");
    }

    public String getArtist() {
        return (String) this.fields.get("ARTIST");
    }

    public String getCategory() {
        return (String) this.fields.get("CAT");
    }

    public String getDiscId() {
        return (String) this.fields.get("DISCID");
    }

    public String getGenre() {
        return (String) this.fields.get("DGENRE");
    }

    public int getQuality() {
        /*
         * Artist: mandatory Album: mandatory Tracks: mandatory Comment: 25
         * Year: 40 Genre: 20 TracksComment: 15
         */
        return getQuality(25, 40, 20, 15);

    }

    private int getQuality(int comment, int year, int genre, int trackComment) {
        if (this.exactMatch)
            return 100;

        int q = 0;
        if (!getGenre().equals(""))
            q += comment;
        if (!getYear().equals(""))
            q += year;
        if (!getAlbumComment().equals(""))
            q += genre;

        int nb = getTracksNumber();
        int nbTrackComment = 0;
        for (int i = 0; i < nb; i++)
            if (!getTrackComment(i).equals(""))
                nbTrackComment++;

        q += (int) (trackComment * (((double) nbTrackComment) / nb));
        return q;
    }

    public String getTrackComment(int i) {
        return ((String[]) this.fields.get("EXTT"))[i];
    }

    /**
     * This method will return the length of the specified track in seconds.
     * <br>
     * <b>Warning: </b> It is possible that these durations could not be parsed.
     * If so this method will ever return "-1".
     * 
     * @param i
     *                  The track.
     * @return The duration of the track in seconds or -1 if unknown.
     */
    public int getTrackDuration(int i) {
        if (this.trackDurations != null) {
            return trackDurations[i];
        }
        return -1;
    }

    public int getTracksNumber() {
        return ((Integer) this.fields.get("TRACKNUMBER")).intValue();
    }

    public String getTrackTitle(int i) {
        return ((String[]) this.fields.get("TITLE"))[i];
    }

    public int getTrackNumber (int i ) {
        return ((int[])this.fields.get("NUMBERING"))[i]+1;
    }
    
    public String getYear() {
        return (String) this.fields.get("DYEAR");
    }

    public boolean isExactMatch() {
        return this.exactMatch;
    }

    public void swapTracks(int i1, int i2) {
        String[] extt = (String[]) this.fields.get("EXTT");
        String[] title = (String[]) this.fields.get("TITLE");
        int[] trackNumber = (int[])this.fields.get("NUMBERING");
        
        String temp = extt[i1];
        extt[i1] = extt[i2];
        extt[i2] = temp;

        temp = title[i1];
        title[i1] = title[i2];
        title[i2] = temp;

        int tmp = trackNumber[i1];
        trackNumber[i1] = trackNumber[i2];
        trackNumber[i2] = tmp;
        
        if (this.trackDurations != null) {
            tmp = trackDurations[i1];
            trackDurations[i1] = trackDurations[i2];
            trackDurations[i2] = tmp;
        }
    }

    public String toString() {
        String output = "---Free DB Read Result-----------------------\n";

        output += "Artiste: " + getArtist() + "\tAlbum: " + getAlbum() + "\n";
        output += "Disc ID: " + getDiscId() + "\tExt.DiscInfo: "
                + getAlbumComment() + "\n";
        output += "Ann\ufffde: " + getYear() + "\tGenre: " + getGenre() + "\n";
        int nb = getTracksNumber();
        output += " Tracks total: " + nb + "\n";
        for (int i = 0; i < nb; i++)
            output += "\tTrack " + i + ": " + getTrackTitle(i)
                    + "\tExt.Trackinfo: " + getTrackComment(i) + "\n";
        output += "----------------------------------------";
        return output;
    }

}