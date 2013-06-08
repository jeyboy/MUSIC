package outag.formats.mp3;

import java.util.List;
import java.util.Locale;

import outag.formats.generic.AbstractTag;
import outag.formats.generic.TagField;
import outag.formats.mp3.util.id3frames.CommId3Frame;
import outag.formats.mp3.util.id3frames.TextId3Frame;

/** Implementation of {@link outag.formats.Tag} of the
 * ID3V2 tagging system used with MP3s. */
public class Id3v2Tag extends AbstractTag {
	/** Default encoding to use for new frames.<br>
	 * The code indirectly will choose the UTF-16LE variant with BOM. */
	public static String DEFAULT_ENCODING = "UTF-16";

	/** Used to identify the minor version 2 of the ID3V2 tag. */
	public static byte ID3V22 = 0;

	/** Used to identify the minor version 3 of the ID3V2 tag. */
	public static byte ID3V23 = 1;

	/** Used to identify the minor version 4 of the ID3V2 tag. */
	public static byte ID3V24 = 2;

	/** If this flag is <code>true</code>, the file which contained this ID3V2
	 * tag also contains a ID3V1 Tag, whose values are merged into this tag object. */
	private boolean hasV1 = false;

	/** Stores the ID3V2 version identifier to which the current tag belongs.<br>
	 * <ul>
	 * <li> {@link #ID3V22} </li>
	 * <li> {@link #ID3V23} </li>
	 * <li> {@link #ID3V24} </li>
	 * </ul> */
	private byte representedVersion = ID3V23;

	/** Creates a default instance. <br>
	 * Tag version is {@link #ID3V22}. */
	public Id3v2Tag() {	/* Nothing to do */	}

	/** Creates an instance.
	 * @param version - The version to represent. {@link #representedVersion}. */
	public Id3v2Tag(byte version) { this.representedVersion = version; }

	/** @see outag.formats.generic.AbstractTag#createAlbumField(java.lang.String) */
	protected TagField createAlbumField(String content) { return new TextId3Frame("TALB", content);	}

	/** @see outag.formats.generic.AbstractTag#createArtistField(java.lang.String) */
	protected TagField createArtistField(String content) { return new TextId3Frame("TPE1", content); }

	/** @see outag.formats.generic.AbstractTag#createCommentField(java.lang.String) */
	protected TagField createCommentField(String content) {	return new CommId3Frame(content); }

	/** @see outag.formats.generic.AbstractTag#createGenreField(java.lang.String) */
	protected TagField createGenreField(String content) { return new TextId3Frame("TCON", content);	}

	/** @see outag.formats.generic.AbstractTag#createTitleField(java.lang.String) */
	protected TagField createTitleField(String content) { return new TextId3Frame("TIT2", content);	}

	/** @see outag.formats.generic.AbstractTag#createTrackField(java.lang.String) */
	protected TagField createTrackField(String content) { return new TextId3Frame("TRCK", content); }

	/** @see outag.formats.generic.AbstractTag#createYearField(java.lang.String) */
	protected TagField createYearField(String content) { return new TextId3Frame("TDRC", content); }

	/** @see outag.formats.generic.AbstractTag#getAlbumId() */
	protected String getAlbumId() {	return "TALB"; }

	/** @see outag.formats.generic.AbstractTag#getArtistId() */
	protected String getArtistId() { return "TPE1";	}
	
	/** @see outag.formats.generic.AbstractTag#getCommentId() */
	protected String getCommentId() { return "COMM"; }

	/** @see outag.formats.generic.AbstractTag#getGenreId() */
	protected String getGenreId() {	return "TCON"; }
	
	/** @see outag.formats.generic.AbstractTag#getTitleId() */
	protected String getTitleId() {	return "TIT2"; }

	/** @see outag.formats.generic.AbstractTag#getTrackId() */
	protected String getTrackId() {	return "TRCK"; }

	/** @see outag.formats.generic.AbstractTag#getYearId() */
	protected String getYearId() { return "TDRC"; }	

	/** @see outag.formats.generic.AbstractTag#getComment() */
	public List getComment() {
		List comments = super.getComment();
		String currIso = Locale.getDefault().getISO3Language();
		CommId3Frame top = null;
		for (int i = 0; i < comments.size(); i++) {
			if (comments.get(i) instanceof CommId3Frame) {
				top = (CommId3Frame) comments.get(i);
				if (!top.getLangage().equals(currIso)) {
					top = null;
				} else {
					comments.remove(i);
					break;
				}
			}
		}
		if (top != null)
			comments.add(0, top);

		return comments;
	}

	/** @return the Id3v2 minor version identifier, the tag represents.<br>
	 * Values are {@link #ID3V22}, {@link #ID3V23} and {@link #ID3V24}. */
	public byte getRepresentedVersion() { return this.representedVersion; }

	/** Determines whether the values of a parallel stored ID3V1 tag are merged
	 * whithin this object.<br> In fact no value may have made it into this, but
	 * there is an ID3V1 tag present in the original file.
	 * @return <code>true</code> if there was a ID3V1 tag present. */
	public boolean hasId3v1() { return hasV1; }

	/** Set the ID3V1 tag present property.
	 * @see #hasId3v1()
	 * @param b - the value to set. */
	protected void hasId3v1(boolean b) { this.hasV1 = b; }

	/** @see outag.formats.generic.AbstractTag#isAllowedEncoding(java.lang.String) */
	protected boolean isAllowedEncoding(String enc) {
		boolean result = enc.equals("ISO-8859-1") || enc.startsWith("UTF-16");
		if (!result && this.representedVersion == ID3V24)
			result = enc.equals("UTF-16BE") || enc.equals("UTF-8");

		return result;
	}

	/** @see java.lang.Object#toString() */
	public String toString() { return "Id3v2 " + super.toString(); }

	/** @param representedVersion The representedVersion to set. */
	protected void setRepresentedVersion(byte representedVersion) {
		this.representedVersion = representedVersion;
	}
}