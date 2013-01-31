package outag.audioformats.generic;

/** Extends the default field definition by methods for working
 * with human readable text.<br>
 * A TagTextField does not store binary data. */
public interface TagTextField extends TagField {
	/** @return Content */
	public String getContent();

	/** @return Charset encoding. */
	public String getEncoding();

	/** @param content - fields content. */
	public void setContent(String content);

	/** Sets the charset encoding used by the field.
	 * @param encoding - charset. */
	public void setEncoding(String encoding);
}