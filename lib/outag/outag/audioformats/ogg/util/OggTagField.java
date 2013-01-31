package outag.audioformats.ogg.util;

import java.io.UnsupportedEncodingException;

import outag.audioformats.generic.TagField;
import outag.audioformats.generic.TagTextField;
import outag.audioformats.generic.Utils;

/** Name and content of a tag entry in ogg-files.  */
public class OggTagField implements TagTextField {

    /** If <code>true</code>, the id of the current encapsulated tag field is
     * specified as a common field. <br> Example is "ARTIST" which should be
     * interpreted by any application as the artist of the media content. <br>
     * Will be set during construction with {@link #checkCommon()}. */
    private boolean common;

    /** Content of the tag field. */
    private String content;

    /** id (name) of the tag field. */
    private String id;

    /** Creates an instance.
     * @param raw - Raw byte data of the tagfield.
     * @throws UnsupportedEncodingException - If the data doesn't conform "UTF-8" specification. */
    public OggTagField(byte[] raw) throws UnsupportedEncodingException {
        String field = new String(raw, "UTF-8");

        String[] splitField = field.split("=");
        if (splitField.length > 1) {
            this.id = splitField[0].toUpperCase();
            this.content = splitField[1];
        } else {
            //Either we have "XXXXXXX" without "="
            //Or we have "XXXXXX=" with nothing after the "="
            int i = field.indexOf("="); 
            if(i != -1) {
                this.id = field.substring(0, i+1);
                this.content = "";
            }
            else {
	            //Beware that ogg ID, must be capitalized and contain no space..
	            this.id = "ERRONEOUS";
	            this.content = field;
            }
        }

        checkCommon();
    }

    /** Creates an instance.
     * @param fieldId - ID (name) of the field.
     * @param fieldContent - Content of the field. */
    public OggTagField(String fieldId, String fieldContent) {
        this.id = fieldId.toUpperCase();
        this.content = fieldContent;
        checkCommon();
    }

    /** Examines the ID of the current field and modifies
     * {@link #common}in order to reflect if the tag id is a commonly used one. */
    private void checkCommon() {
        this.common = id.equals("TITLE") || id.equals("ALBUM")
                || id.equals("ARTIST") || id.equals("GENRE")
                || id.equals("TRACKNUMBER") || id.equals("DATE")
                || id.equals("DESCRIPTION") || id.equals("COMMENT")
                || id.equals("TRACK");
    }


    /** @see outag.audioformats.generic.TagField#copyContent(outag.audioformats.generic.TagField) */
    public void copyContent(TagField field) {
        if (field instanceof TagTextField)
            this.content = ((TagTextField) field).getContent();
    }

    /** Return the byte representation of the given
     * string after it has been converted to the given encoding.
     * @param s - the string whose converted bytes should be returned.
     * @param encoding - the encoding type to which the string should be converted.
     * @return If <code>encoding</code> is supported the byte data of the
     *               given string is returned in that encoding.
     * @throws UnsupportedEncodingException
     *                    If the requested encoding is not available. */
    protected byte[] getBytes(String s, String encoding)
            throws UnsupportedEncodingException {
        return s.getBytes(encoding);
    }

    /** @see outag.audioformats.generic.TagTextField#getContent() */
    public String getContent() { return content; }

    /** @see outag.audioformats.generic.TagTextField#getEncoding() */
    public String getEncoding() { return "UTF-8"; }

    /** @see outag.audioformats.generic.TagField#getId() */
    public String getId() { return this.id; }

    /** @see outag.audioformats.generic.TagField#getRawContent() */
    public byte[] getRawContent() throws UnsupportedEncodingException {
        byte[] size = new byte[4];
        byte[] idBytes = this.id.getBytes();
        byte[] contentBytes = getBytes(this.content, "UTF-8");
        byte[] b = new byte[4 + idBytes.length + 1 + contentBytes.length];

        int length = idBytes.length + 1 + contentBytes.length;
        size[3] = (byte) ((length & 0xFF000000) >> 24);
        size[2] = (byte) ((length & 0x00FF0000) >> 16);
        size[1] = (byte) ((length & 0x0000FF00) >> 8);
        size[0] = (byte) (length & 0x000000FF);

        int offset = 0;
        Utils.copy(size, b, offset);
        offset += 4;
        Utils.copy(idBytes, b, offset);
        offset += idBytes.length;
        b[offset] = (byte) 0x3D;
        offset++;// "="
        Utils.copy(contentBytes, b, offset);

        return b;
    }

    /** @see outag.audioformats.generic.TagField#isBinary() */
    public boolean isBinary() { return false; }

    /** @see outag.audioformats.generic.TagField#isBinary(boolean) */
    public void isBinary(boolean b) {
        if (b) {
            // Only throw if binary = true requested.
            throw new UnsupportedOperationException(
                    "OggTagFields cannot be changed to binary.\n"
                            + "binary data should be stored elsewhere"
                            + " according to Vorbis_I_spec.");
        }
    }

    /** @see outag.audioformats.generic.TagField#isCommon() */
    public boolean isCommon() { return common; }

    /** @see outag.audioformats.generic.TagField#isEmpty() */
    public boolean isEmpty() { return this.content.equals(""); }

    /** @see outag.audioformats.generic.TagTextField#setContent(java.lang.String) */
    public void setContent(String s) { this.content = s; }

    /** @see outag.audioformats.generic.TagTextField#setEncoding(java.lang.String) */
    public void setEncoding(String s) {
        if (s == null || !s.equalsIgnoreCase("UTF-8"))
            throw new UnsupportedOperationException(
                    "The encoding of OggTagFields cannot be "
                            + "changed.(specified to be UTF-8)");
    }
    
    public String toString() { return getContent(); }
}