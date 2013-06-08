package outag.formats.asf.data;

import java.math.BigInteger;

import outag.formats.asf.util.Utils;

/** This class represents the data of a chunk which contains title, author,
 * copyright, description and the rating of the file. <br>
 * It is optional whithin asf files. But if exists only once. */
public class ContentDescription extends Chunk {
    /** File artist. */
    private String author = null;

    /** File copyright. */
    private String copyRight = null;

    /** File comment. */
    private String description = null;

    /** File rating. */
    private String rating = null;

    /** File title. */
    private String title = null;

    /** Creates an instance. */
    public ContentDescription() { this(0, BigInteger.valueOf(0)); }

    /** Creates an instance.
     * @param pos Position of content description within file or stream
     * @param chunkLen Length of content description. */
    public ContentDescription(long pos, BigInteger chunkLen) {
        super(GUID.GUID_CONTENTDESCRIPTION, pos, chunkLen);
    }

    /** @return Returns the author. */
    public String getAuthor() { return author == null ? "" : author; }
    /** @param fileAuthor The author to set.
     * @throws IllegalArgumentException
     *                    If "UTF-16LE"-byte-representation would take more than 65535
     *                    bytes. */
    public void setAuthor(String fileAuthor) throws IllegalArgumentException {
        Utils.checkStringLengthNullSafe(fileAuthor);
        this.author = fileAuthor;
    }    

    /** @return Returns the comment. */
    public String getComment() { return description == null ? "" : description; }
    /** @param tagComment The comment to set.
     * @throws IllegalArgumentException
     *                    If "UTF-16LE"-byte-representation would take more than 65535
     *                    bytes. */
    public void setComment(String tagComment) throws IllegalArgumentException {
        Utils.checkStringLengthNullSafe(tagComment);
        this.description = tagComment;
    }    

    /** @return Returns the copyRight. */
    public String getCopyRight() { return copyRight == null ? "" : copyRight; }
    /** @param cpright The copyRight to set.
     * @throws IllegalArgumentException
     *                    If "UTF-16LE"-byte-representation would take more than 65535
     *                    bytes. */
    public void setCopyRight(String cpright) throws IllegalArgumentException {
        Utils.checkStringLengthNullSafe(cpright);
        this.copyRight = cpright;
    }    

    /** @return returns the rating. */
    public String getRating() { return rating == null ? "" : rating; }
    /** @param ratingText The rating to be set.
     * @throws IllegalArgumentException
     *                    If "UTF-16LE"-byte-representation would take more than 65535
     *                    bytes. */
    public void setRating(String ratingText) throws IllegalArgumentException {
        Utils.checkStringLengthNullSafe(ratingText);
        this.rating = ratingText;
    }    

    /** @return Returns the title. */
    public String getTitle() { return title == null ? "" : title; }
    /** @param songTitle The title to set.
     * @throws IllegalArgumentException
     *                    If "UTF-16LE"-byte-representation would take more than 65535
     *                    bytes. */
    public void setTitle(String songTitle) throws IllegalArgumentException {
        Utils.checkStringLengthNullSafe(songTitle);
        this.title = songTitle;
    }    

    public String prettyPrint() {
        StringBuffer result = new StringBuffer(super.prettyPrint());
        result.insert(0, Utils.LINE_SEPARATOR + "Content Description:"
                + Utils.LINE_SEPARATOR);
        result.append("   Title      : " + getTitle() + Utils.LINE_SEPARATOR);
        result.append("   Author     : " + getAuthor() + Utils.LINE_SEPARATOR);
        result.append("   Copyright  : " + getCopyRight()
                + Utils.LINE_SEPARATOR);
        result.append("   Description: " + getComment() + Utils.LINE_SEPARATOR);
        result.append("   Rating     :" + getRating() + Utils.LINE_SEPARATOR);
        return result.toString();
    }
}