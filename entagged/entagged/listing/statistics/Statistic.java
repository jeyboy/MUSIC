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
package entagged.listing.statistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import entagged.tageditor.resources.LangageManager;

/**
 * A record for statistical information about audio files. <br>
 * 
 * @author Christian Laireiter (liree)
 */
public class Statistic {

    /**
     * This class stores a String and a number which indicates how often this
     * specific string has been used. (Ignoring case)
     */
    public final class StringCountRecord implements Comparable {

        /**
         * Stores the number of occurences.
         */
        private int count = 0;

        /**
         * Stores the represented String, which is about to be counted.
         */
        private final String value;

        /**
         * Creates an instance.
         * 
         * @param toCount
         *                   The string which will be counted.
         */
        public StringCountRecord(String toCount) {
            this.value = toCount;
        }

        /**
         * (overridden)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(Object arg0) {
            return this.value.compareTo(arg0.toString());
        }

        /**
         * (overridden)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object arg0) {
            return this.value.equals(arg0.toString());
        }

        /**
         * The amount of {@link #increase()}calls. <br>
         * 
         * @return The amount of increasements.
         */
        public int getCounter() {
            return this.count;
        }

        /**
         * Returns the string which is counted.
         * 
         * @return the String which is counted.
         */
        public String getString() {
            return this.value;
        }

        /**
         * Increses the counter of the current object.
         * 
         * @return returns the new value
         */
        public int increase() {
            return ++this.count;
        }

        /**
         * (overridden)
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return getString()
                    + " ("
                    + count
                    + " "
                    + LangageManager
                            .getProperty("statistic.occurrencedescriptor")
                    + ")";
        }
    }

    /**
     * This constant stores the name for the hashmap which will store the
     * information about used bitrates..
     */
    public static final String MAP_BITRATE = "BITRATE";

    /**
     * This constant stores the name for the hashmap which will store the
     * information about used channel number.
     */
    public static final String MAP_CHANNELS = "CHANNEL";

    /**
     * This constant stores the name for the hashmap which will store the
     * information about used codecs.
     */
    public static final String MAP_CODEC = "CODEC";

    /**
     * This constant stores the name for the hashmap which will store the
     * information about used sampling rates.
     */
    public static final String MAP_SAMPLING = "SAMPLING";

    /**
     * The sum of each media length.
     */
    private long duration = 0;

    /**
     * The amount of files which couldn't be read due to errors. <br>
     */
    private int invalidFileCount = 0;

    /**
     * Stores other hash maps which will store a string to a
     * {@link StringCountRecord}.<br>
     */
    private HashMap recordMaps = new HashMap();

    /**
     * Stores the size of the valid files in bytes.
     */
    private long totalFileSize = 0;

    /**
     * Stores the amount of audio files which were read successfully and
     * inserted into the current statistics.
     */
    private int validFileCount = 0;

    /**
     * This method increases the total duration.
     * 
     * @param milliSeconds
     *                   the time to be added. (Milliseconds)
     * @return the value of the total duration after insertion.
     */
    public long addDuration(int milliSeconds) {
        return this.duration += milliSeconds;
    }

    /**
     * Adds the given number of bytes to the total sum.
     * 
     * @param fileSize
     *                   bytes
     * @return the total size after adding.
     */
    public long addFileSize(long fileSize) {
        return this.totalFileSize += fileSize;
    }

    /**
     * This method adds a value to the statistics into the category given by
     * <code>itemIdentifier.</code>
     * 
     * @param itemIdentifier
     *                   The category where this value belongs to. <br>
     *                   A new string will create a new category.
     * @param value
     *                   The value to insert.
     * @return The new amount of occurrences of <code>value</code> in the
     *               category named <code>itemIdentifier</code>.
     */
    public int addStatisticItem(String itemIdentifier, String value) {
        return addToStringCountRecord(getRecordMap(itemIdentifier), value);
    }

    /**
     * This method will add or count the given String using the given HashSet
     * which must contain {@link StringCountRecord}objects.
     * 
     * @param recordMap
     *                   The map which contains the count records.
     * @param toInsert
     *                   The string to be registered or counted up.
     * @return The new amount of this string whithin the record set.
     */
    protected int addToStringCountRecord(HashMap recordMap, String toInsert) {
        StringCountRecord record = (StringCountRecord) recordMap.get(toInsert
                .toLowerCase());
        if (record == null) {
            record = new StringCountRecord(toInsert);
            recordMap.put(toInsert.toLowerCase(), record);
        }
        return record.increase();
    }

    /**
     * Creates a string which will show the amount of different entries given by
     * <code>codecRecords</code>.<br>
     * 
     * @param records
     *                   The map containing the records.
     * @param description
     *                   A description (headline) for this section.
     * @return A string for a console output.
     */
    private String createSummary(HashMap records, String description) {
        StringBuffer result = new StringBuffer();
        final String lineSeparator = System.getProperty("line.separator");
        result.append(description + lineSeparator);
        StringCountRecord[] entries = (StringCountRecord[]) records.values()
                .toArray(new StringCountRecord[records.size()]);
        Arrays.sort(entries);
        for (int i = 0; i < entries.length; i++) {
            result.append(entries[i].toString() + lineSeparator);
        }
        return result.toString();
    }

    /**
     * @return Returns the duration.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * @return Returns the invalidFileCount.
     */
    public int getInvalidFileCount() {
        return invalidFileCount;
    }

    /**
     * This method will return (and create if needed) a Hashmap for the given
     * name.
     * 
     * @param mapName
     *                   The identifier of the map.
     * @return the hashmap for the identifier.
     */
    protected HashMap getRecordMap(String mapName) {
        HashMap result = (HashMap) recordMaps.get(mapName);
        if (result == null) {
            result = new HashMap();
            recordMaps.put(mapName, result);
        }
        return result;
    }

    /**
     * Returns all records on the given category.
     * 
     * @param category
     *                   The category.
     * @return Inserted items.
     */
    public StringCountRecord[] getRecords(String category) {
        StringCountRecord[] result = new StringCountRecord[0];
        HashMap map = getRecordMap(category);
        if (map != null) {
            result = (StringCountRecord[]) map.values().toArray(
                    new StringCountRecord[map.size()]);
        }
        return result;
    }

    /**
     * This method will return {@link #duration}as a string like "dd Days
     * hh:mm:ss.millis"
     * 
     * @return see description.
     */
    public String getTotalDuration() {
        StringBuffer result = new StringBuffer();
        long tmp = duration;
        long millis = tmp % 1000;
        result.append("." + String.valueOf(millis));
        tmp /= 1000;
        result.insert(0, tmp % 60);
        tmp /= 60;
        result.insert(0, tmp % 60 + ":");
        tmp /= 60;
        if (tmp > 0) {
            result.insert(0, tmp % 24 + ":");
            tmp /= 24;
            if (tmp > 0) {
                result.insert(0, tmp + " days ");
            }
        }
        return result.toString();
    }

    /**
     * Returns a representation of the total file size.
     * 
     * @return see description.
     */
    public String getTotalFileSize() {
        String unit = " Bytes";
        double tmp = this.totalFileSize;
        if (tmp > 1024) {
            tmp /= 1024;
            unit = " KiloBytes";
        }
        if (tmp > 1024) {
            tmp /= 1024;
            unit = " MegaBytes";
        }
        String result = String.valueOf(tmp);
        int index = result.indexOf('.');
        if (result.length() > index + 2) {
            result = result.substring(0, index + 2);
        }
        return result + unit;
    }

    /**
     * @return Returns the validFileCount.
     */
    public int getValidFileCount() {
        return validFileCount;
    }

    /**
     * Increases the number of invalid files by one.
     * 
     * @return The new amount of invalid files.
     */
    public int increaseInvalidFileCount() {
        return ++invalidFileCount;
    }

    /**
     * Increases the number of valid audio files by one
     * 
     * @return the new amount of parsed audio files.
     */
    public int increaseValidFileCount() {
        return ++validFileCount;
    }

    /**
     * (overridden)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        final String lineSeparator = System.getProperty("line.separator");
        result.append("Statistic:" + lineSeparator);
        result.append("Number of audio files: " + validFileCount
                + lineSeparator);
        result.append("Number of invalid files: " + invalidFileCount
                + lineSeparator);
        result.append("Total time: " + getTotalDuration() + lineSeparator);
        Iterator it = recordMaps.keySet().iterator();
        while (it.hasNext()) {
            result.append(lineSeparator);
            String name = (String) it.next();
            result.append(createSummary(getRecordMap(name), name));
        }
        return result.toString();
    }
}
