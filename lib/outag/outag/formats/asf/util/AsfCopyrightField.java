package outag.formats.asf.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import outag.formats.Tag;
import outag.formats.generic.TagField;
import outag.formats.generic.TagTextField;

/** Represents the copyright field of asf files. <br>
 * The copyright cannot be accessed like an ordinary "name"-"value"-property,
 * since it has a defined position within the asf structure. <br>
 * <b>Use: </b> <br>
 * If you want to modify the copyright field of asf files using the outag
 * audio library, you should access it using this class. <br>
 * You can get a it using {@link outag.formats.Tag#get(String)}. The
 * argument must be the value of {@link #FIELD_ID}, since the reading facility
 * of asf files will place the copyright there. (consider that the copyright
 * field of asf fils is only supported once, you may place several copyright
 * fields into {@link outag.formats.Tag}, however only the first one
 * will be written.) <br>
 * It is recommended to use the {@link #setString(String)}method if possible
 * because only then you will get feedback of validity checks. If your
 * environment doesn't support "UTF-16LE" encoding or the
 * "UTF-16LE"-representation would exceed 65533 Bytes you will be notified.
 * Otherwise if you use the standard method {@link #setContent(String)}, the
 * value "Conversion Exception occured." will be placed on errors. <br>
 * <b>Alternative: </b> <br>
 * You can use your own implementation of
 * {@link outag.formats.generic.TagField}, then you must remember using
 * {@link #FIELD_ID}as the "ID", since the asf writing methods will look at
 * that for writing the file. Additional isCommon() must return
 * <code>true</code><br>
 * <b>Example </b>: <br>
 * <i>Reading copyright </i> <br>
 * 
 * <pre><code>
 * AudioFile file = AudioFileIO.read(new File(&quot;/somewhere/test.wma&quot;));
 * // Get the copyright field
 * TagTextField copyright = AsfCopyrightField.getCopyright(file.getTag());
 * // Will be null, if no copyright is specified.
 * if (copyright != null)
 * 	System.out.println(copyright.getContent());
 * </code></pre>
 * 
 * <br>
 * <br>
 * <i>Modifying copyright </i> <br>
 * 
 * <pre><code>
 * AudioFile file = AudioFileIO.read(new File(&quot;/somewhere/test.wma&quot;));
 * // Get the copyright field
 * TagTextField copyright = AsfCopyrightField.getCopyright(file.getTag());
 * // Will be null, if no copyright is specified.
 * if (copyright != null) {
 * 	System.out.println(copyright.getContent());
 * 	// setThe value
 * 	copyright.setContent(&quot;It belongs to me now.&quot;);
 * }
 * AudioFileIO.write(file);
 * </code></pre>

 * <br>
 * <br>
 * <i>Creating new copyright </i> <br>
 * 
 * <pre><code>
 * AudioFile file = AudioFileIO.read(new File(&quot;/somewhere/test.wma&quot;));
 * // Create field
 * TagTextField copyright = new AsfCopyrightField();
 * // setThe value
 * copyright.setContent(&quot;My intellectual property&quot;);
 * // Add the new field
 * file.getTag().set(copyright);
 * AudioFileIO.write(file);
 * </code></pre>
 * 
 * <br>
 * <br>
 * <i>Delete copyright </i> <br>
 * 
 * <pre><code>
 * AudioFile file = AudioFileIO.read(new File(&quot;/somewhere/test.wma&quot;));
 * // Create field
 * TagTextField copyright = new AsfCopyrightField();
 * // Without a value, the field will be deleted.
 * file.getTag().set(copyright);
 * AudioFileIO.write(file);
 * </code></pre>
 * <br>
 * <b>Programming internals </b> <br>
 * This class's method {@link #isCommon()}will always return <code>true</code>,
 * so that the represented value will not be handled as a
 * "name"-"value"-property. The
 * {@link outag.formats.asf.util.TagConverter}will identify this field
 * at
 * {@link outag.formats.asf.util.TagConverter#createContentDescription(Tag)}
 * and place the value into the corresponding java object (
 * {@link outag.formats.asf.data.ContentDescription}). */
public final class AsfCopyrightField implements TagTextField {
	/** Represents the field id of the currently represented copyright field. <br>
	 * This value will be filtered at {@link TagConverter}. */
	public final static String FIELD_ID = "SPECIAL/WM/COPYRIGHT";

	/** Extract the copyright field out of asf files. <br>
	 * @param tag - The tag which contains the copyright field.
	 * @return <code>null</code> if the tag represents a file which is not a
	 *         asf file, or no copyright has been entered. */
	public static TagTextField getCopyright(Tag tag) {
		TagTextField result = null;
		List<?> list = tag.get(FIELD_ID);
		if (list != null && list.size() > 0) {
			TagField field = (TagField) list.get(0);
			if (field instanceof TagTextField)
				result = (TagTextField) field;
		}
		return result;
	}

	/** Contains the value of the copyright field. <br>
	 * In other word, the copyright is stored in this field. */
	private String value = "";

	public void copyContent(TagField field) {
		if (field instanceof TagTextField)
			value = ((TagTextField) field).getContent();
	}

	public String getContent() { return value; }

	public String getEncoding() { return "UTF-16LE"; }

	public String getId() { return FIELD_ID; }

	public byte[] getRawContent() throws UnsupportedEncodingException {	return value.getBytes("UTF-16LE"); }

	public boolean isBinary() { return false; }

	public void isBinary(boolean b) {
		if (b) {
			throw new UnsupportedOperationException(
					"No conversion supported. Copyright is a String");
		}
	}

	/** Will return true, so it won't be used for the extended content
	 * description chunk, where the "name"-"value" properties are stored. <br>
	 * @see TagConverter#assignOptionalTagValues(Tag,
	 *      ExtendedContentDescription)
	 * @see outag.formats.generic.TagField#isCommon() */
	public boolean isCommon() {
		// Return true, to let this field be ignored at assignOptionalTagValues
		return true;
	}

	public boolean isEmpty() { return value.length() == 0; }

	public void setContent(String s) {
		try { setString(s);	} 
		catch (IllegalArgumentException iae) {
			iae.printStackTrace();
			value = "Conversion Exception occured.";
		}
	}

	public void setEncoding(String s) {
		if (s == null || !s.equalsIgnoreCase("UTF-16LE"))
			throw new UnsupportedOperationException(
					"The encoding of Asf tags cannot be "
							+ "changed.(specified to be UTF-16LE)");
	}

	/** Sets the content of the current copyright field. <br>
	 * The reason for existence of this method is that you will get Exceptions
	 * if the given value cannot be used for asf files. <br>
	 * The restriction for asf field contents is that they must not exceed 65535
	 * bytes including the zero termination character and they must be stored in
	 * "UTF-16LE" encoding.
	 * @param s - The new copyright.
	 * @throws IllegalArgumentException
	 *             If the size of the value would exceed 65535 bytes in UTF-16LE
	 *             encoding including two bytes for zero termination. */
	public void setString(String s) { Utils.checkStringLengthNullSafe((value = s));	}

	public String toString() { return FIELD_ID + ":\"" + getContent() + "\""; }
}