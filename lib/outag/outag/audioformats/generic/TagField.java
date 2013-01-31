package outag.audioformats.generic;

import java.io.UnsupportedEncodingException;

/** Implementing classes represent a tag field for the outag audio library.<br>
 * Very basic functionality is defined for use with {@link outag.audioformats.Tag}. */
public interface TagField {
	/** Copies the data of the given filed to the current data.
	 * @param field - The field containing the data to be taken. */
	public void copyContent(TagField field);

	/** @return Unique identifier for the fields type. (title, artist...)
	 * This value should uniquely identify a kind of tag data, like title.
	 * {@link AbstractTag} will use the &quot;id&quot; to summarize multiple
	 * fields. */
	public String getId();

	/** Delivers the binary representation of the fields data in
	 * order to be directly written to the file.
	 * @return Binary data representing the current tag field.
	 * @throws UnsupportedEncodingException
	 *             Most tag data represents text. In some cases the underlying
	 *             implementation will need to convert the text data in java to
	 *             a specific charset encoding. In these cases an
	 *             {@link UnsupportedEncodingException} may occur. */
	public byte[] getRawContent() throws UnsupportedEncodingException;

	/** Determines whether the represented field contains (is made up of) binary
	 * data, instead of text data.<br>
	 * Software can identify fields to be displayed because they are human
	 * readable if this mehtod returns <code>false</code>.
	 * @return <code>true</code> if field represents binary data (not human readable). */
	public boolean isBinary();

	/** Identifies a field to be of common use.<br>
	 * Some software may differ between common and not common fields. A common
	 * one is for sure the title field. A web link may not be of common use for
	 * tagging. However some file formats, or future development of users
	 * expectations will make more fields common than now can be known.
	 * @return <code>true</code> if the field is of common use. */
	public boolean isCommon();

	/** Determines whether the content of the field is empty.<br>
	 * @return <code>true</code> if no data is stored (or empty String). */
	public boolean isEmpty();

	/** @return a human readable description of the fields contents.<br>
	 * For text fields it should be the text itself. Other fields containing
	 * images may return a formatted string with image properties like width,
	 * height and so on. */
	public String toString();
}