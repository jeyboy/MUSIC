package outag.audioformats.asf.data;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;

import outag.audioformats.asf.util.Utils;

/** This class is a wrapper for properties within a
 * {@link entagged.audioformats.asf.data.ExtendedContentDescription}. */
public final class ContentDescriptor implements Comparable<Object> {
    /** This field stores all values of the "ID_"-constants. */
    public final static HashSet<String> COMMON_FIELD_IDS;

    /** This constant gives the common id (name) for the "album" field in an asf extended content description. */
    public final static String ID_ALBUM = "WM/AlbumTitle";

    /** This constant gives the common id (name) for the "artist" field in an asf extended content description. */
    public final static String ID_ARTIST ="WM/AlbumArtist";
    
    /** This constant gives the common id (name) for the "genre" field in an asf extended content description. */
    public final static String ID_GENRE = "WM/Genre";

    /** This constant gives the common id (name) for the "genre Id" field in an asf extended content description. */
    public final static String ID_GENREID = "WM/GenreID";

    /** This constant gives the common id (name) for the "track number" field in an asf extended content description. */
    public final static String ID_TRACKNUMBER = "WM/TrackNumber";
    
    /** This constant gives the common id (name) for the "year" field in an asf extended content description. */
    public final static String ID_YEAR = "WM/Year";

    /** Constant for the content descriptor-type for binary data. */
    public final static int TYPE_BINARY = 1;

    /** Constant for the content descriptor-type for booleans.*/
    public final static int TYPE_BOOLEAN = 2;

    /** Constant for the content descriptor-type for integers (32-bit). */
    public final static int TYPE_DWORD = 3;

    /** Constant for the content descriptor-type for integers (64-bit). */
    public final static int TYPE_QWORD = 4;

    /** Constant for the content descriptor-type for Strings. */
    public final static int TYPE_STRING = 0;

    /** Constant for the content descriptor-type for integers (16-bit). */
    public final static int TYPE_WORD = 5;

    static {
        COMMON_FIELD_IDS = new HashSet<String>();
        COMMON_FIELD_IDS.add(ID_ALBUM);
        COMMON_FIELD_IDS.add(ID_ARTIST);
        COMMON_FIELD_IDS.add(ID_GENRE);
        COMMON_FIELD_IDS.add(ID_GENREID);
        COMMON_FIELD_IDS.add(ID_TRACKNUMBER);
        COMMON_FIELD_IDS.add(ID_YEAR);
    }

    /** The binary representation of the value. */
    protected byte[] content = new byte[0];

    /** This field shows the type of the content descriptor. <br>
     * @see #TYPE_BINARY
     * @see #TYPE_BOOLEAN
     * @see #TYPE_DWORD
     * @see #TYPE_QWORD
     * @see #TYPE_STRING
     * @see #TYPE_WORD */
    private int descriptorType;

    /** The name of the content descriptor. */
    private final String name;

    /** Creates an Instance.
     * @param propName  Name of the ContentDescriptor.
     * @param propType  Type of the content descriptor. See {@link #descriptorType} */
    public ContentDescriptor(String propName, int propType) {
        if (propName == null)
            throw new IllegalArgumentException("Arguments must not be null.");
        
        Utils.checkStringLengthNullSafe(propName);
        this.name = propName;
        this.descriptorType = propType;
    }

    public Object clone() throws CloneNotSupportedException { return createCopy(); }

    public int compareTo(Object o) {
        int result = 0;
        if (o instanceof ContentDescriptor) {
            ContentDescriptor other = (ContentDescriptor) o;
            result = getName().compareTo(other.getName());
        }
        return result;
    }

    /** This mehtod creates a copy of the current object. <br>
     * @return A new Contentdescriptor containing the same values as the current one. */
    public ContentDescriptor createCopy() {
        ContentDescriptor result = new ContentDescriptor(getName(), getType());
        result.content = getRawData();
        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ContentDescriptor) {
            if (obj == this) {
                result = true;
            } else {
                ContentDescriptor other = (ContentDescriptor) obj;
                result = other.getName().equals(getName())
                        && other.descriptorType == this.descriptorType
                        && Arrays.equals(this.content, other.content);
            }
        }
        return result;
    }

    /** Returns the value of the ContentDescriptor as a Boolean. <br>
     * If no Conversion is Possible false is returned. <br>
     * <code>true</code> if first byte of {@link #content}is not zero.
     * @return boolean representation of the current value. */
    public boolean getBoolean() { return content.length > 0 && content[0] != 0; }

    /** This method returns the name of the content descriptor. */
    public String getName() { return this.name; }

    /** Returns the value of the content descriptor as an integer.
     * <br>
     * Converts the needed amount of byte out of {@link #content}to a number.
     * <br>
     * Only possible if {@link #getType()}equals on of the following: <br>
     * <li>
     * 
     * @see #TYPE_BOOLEAN </li>
     *           <li>
     * @see #TYPE_DWORD </li>
     *           <li>
     * @see #TYPE_QWORD </li>
     *           <li>
     * @see #TYPE_WORD </li>
     * 
     * @return integer value.*/
    public long getNumber() {
        long result = 0;
        int bytesNeeded = -1;
        switch (getType()) {
        case TYPE_BOOLEAN:
            bytesNeeded = 1;
            break;
        case TYPE_DWORD:
            bytesNeeded = 4;
            break;
        case TYPE_QWORD:
            bytesNeeded = 8;
            break;
        case TYPE_WORD:
            bytesNeeded = 2;
            break;
        default:
            throw new UnsupportedOperationException(
                    "The current type doesn't allow an interpretation as a number.");
        }
        if (bytesNeeded > content.length) {
            throw new IllegalStateException(
                    "The stored data cannot represent the type of current object.");
        }
        for (int i = 0; i < bytesNeeded; i++) {
            result |= (content[i] << (i * 8));
        }
        return result;
    }

    /** This method returns a copy of the content of the descriptor. <br>
     * @return The content in binary representation, as it would be written to asf file. */
    public byte[] getRawData() {
        byte[] copy = new byte[this.content.length];
        System.arraycopy(copy, 0, this.content, 0, this.content.length);
        return copy;
    }

    /** Returns the value of the ContentDescriptor as a String. 
     * @return String - Representation Value */
    public String getString() {
        String result = "";
        switch (getType()) {
        case TYPE_BINARY:
            result = "binary data";
            break;
        case TYPE_BOOLEAN:
            result = String.valueOf(getBoolean());
            break;
        case TYPE_QWORD:
        case TYPE_DWORD:
        case TYPE_WORD:
            result = String.valueOf(getNumber());
            break;
        case TYPE_STRING:
            try { result = new String(content, "UTF-16LE"); }
            catch (Exception e) { e.printStackTrace(); }
            break;
        default:
            throw new IllegalStateException("Current type is not known.");
        }
        return result;
    }

    /** Returns the type of the content descriptor. <br>
     * @see #TYPE_BINARY
     * @see #TYPE_BOOLEAN
     * @see #TYPE_DWORD
     * @see #TYPE_QWORD
     * @see #TYPE_STRING
     * @see #TYPE_WORD
     * 
     * @return the value of {@link #descriptorType} */
    public int getType() { return this.descriptorType; }

    /** Checks whether the name of the current field is one of the commonly specified fields. <br>
     * @see #ID_ALBUM
     * @see #ID_GENRE
     * @see #ID_GENREID
     * @see #ID_TRACKNUMBER
     * @see #ID_YEAR
     * @return <code>true</code> if a common field. */
    public boolean isCommon() { return COMMON_FIELD_IDS.contains(this.getName()); }

    /** This method checks if the binary data is empty. <br>
     * Disregarding the type of the descriptor its content is stored as a byte array.
     * @return <code>true</code> if no value is set. */
    public boolean isEmpty() { return this.content.length == 0; }

    /** Sets the Value of the current content descriptor. <br>
     * Using this method will change {@link #descriptorType} to {@link #TYPE_BINARY}.<br>
     * @param data Value to set.
     * @throws IllegalArgumentException
     *                    If the byte array is greater that 65535 bytes. */
    public void setBinaryValue(byte[] data) throws IllegalArgumentException {
        if (data.length > 65535) {
            throw new IllegalArgumentException(
                    "Too many bytes. 65535 is maximum.");
        }
        this.content = data;
        this.descriptorType = TYPE_BINARY;
    }

    /** Sets the Value of the current content descriptor. <br>
     * Using this method will change {@link #descriptorType} to {@link #TYPE_BOOLEAN}.<br>
     * @param value Value to set. */
    public void setBooleanValue(boolean value) {
        this.content = new byte[] { value ? (byte) 1 : 0, 0, 0, 0 };
        this.descriptorType = TYPE_BOOLEAN;
    }

    /** Sets the Value of the current content descriptor. <br>
     * Using this method will change {@link #descriptorType} to {@link #TYPE_DWORD}.
     * @param value Value to set. */
    public void setDWordValue(long value) {
        this.content = Utils.getBytes(value, 4);
        this.descriptorType = TYPE_DWORD;
    }

    /**  Sets the Value of the current content descriptor. <br>
     * Using this method will change {@link #descriptorType} to {@link #TYPE_QWORD}
     * @param value Value to set. */
    public void setQWordValue(long value) {
        this.content = Utils.getBytes(value, 8);
        this.descriptorType = TYPE_QWORD;
    }

    /** Sets the Value of the current content descriptor. <br>
     * Using this method will change {@link #descriptorType} to {@link #TYPE_STRING}.
     * @param value Value to set.
     * @throws IllegalArgumentException
     *                    If byte representation would take more than 65535 Bytes. */
    public void setStringValue(String value) throws IllegalArgumentException {
        try {
            byte[] tmp = value.getBytes("UTF-16LE");
            if (tmp.length > 65535) {
                throw new IllegalArgumentException(
                        "Byte representation of String in "
                                + "\"UTF-16LE\" is to great. (Maximum is 65535 Bytes)");
            }
            this.content = tmp;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.content = new byte[0];
        }
        this.descriptorType = TYPE_STRING;
    }

    /** Sets the Value of the current content descriptor. <br>
     * Using this method will change {@link #descriptorType} to {@link #TYPE_WORD}
     * @param value Value to set. */
    public void setWordValue(int value) {
        this.content = Utils.getBytes(value, 2);
        this.descriptorType = TYPE_WORD;
    }

    public String toString() {
        return getName()
                + " : "
                + new String[] { "String: ", "Binary: ", "Boolean: ",
                        "DWORD: ", "QWORD:", "WORD:" }[this.descriptorType]
                + getString();
    }
}