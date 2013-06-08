package outag.formats.generic;

/** Complete implementation of {@link outag.formats.generic.AbstractTag}.<br>
 * The identifiers of commonly used fields is defined by {@link #keys}. */
public class GenericTag extends AbstractTag {
	private class GenericTagTextField implements TagTextField {
		private String content;
		private final String id;

		/** Creates an instance.
		 * @param fieldId - identifier
		 * @param initialContent - string */
		public GenericTagTextField(String fieldId, String initialContent) {
			this.id = fieldId;
			this.content = initialContent;
		}

		/** @see outag.formats.generic.TagField#copyContent(outag.formats.generic.TagField) */
		public void copyContent(TagField field) {
			if (field instanceof TagTextField)
				this.content = ((TagTextField) field).getContent();
		}

		/** @see outag.formats.generic.TagTextField#getContent() */
		public String getContent() { return this.content; }

		/** @see outag.formats.generic.TagTextField#getEncoding() */
		public String getEncoding() { return "ISO-8859-1"; }

		/** @see outag.formats.generic.TagField#getId() */
		public String getId() {	return id; }

		/** @see outag.formats.generic.TagField#getRawContent() */
		public byte[] getRawContent() {	return this.content == null ? new byte[] {} : this.content.getBytes(); }

		/** @see outag.formats.generic.TagField#isBinary() */
		public boolean isBinary() { return false; }

		/** @see outag.formats.generic.TagField#isBinary(boolean) */
		public void isBinary(boolean b) { /* not supported */ }

		/** @see outag.formats.generic.TagField#isCommon() */
		public boolean isCommon() {	return true; }

		/** @see outag.formats.generic.TagField#isEmpty() */
		public boolean isEmpty() { return this.content.equals(""); }

		/** @see outag.formats.generic.TagTextField#setContent(java.lang.String) */
		public void setContent(String s) { this.content = s; }

		/** @see outag.formats.generic.TagTextField#setEncoding(java.lang.String) */
		public void setEncoding(String s) {	/* Not allowed */ }

		/** @see java.lang.Object#toString() */
		public String toString() { return getId() + " : " + getContent(); }
	}

	/** Index for the &quot;artist&quot;-identifier in {@link #keys}. */
	public static final int ARTIST = 0;	
	
	/** Index for the &quot;album&quot;-identifier in {@link #keys}. */
	public static final int ALBUM = 1;

	/** Index for the &quot;title&quot;-identifier in {@link #keys}. */
	public static final int TITLE = 2;

	/** Index for the &quot;track&quot;-identifier in {@link #keys}. */
	public static final int TRACK = 3;

	/** Index for the &quot;year&quot;-identifier in {@link #keys}. */
	public static final int YEAR = 4;	
	
	/** Index for the &quot;genre&quot;-identifier in {@link #keys}. */
	public static final int GENRE = 5;	
	
	/** Index for the &quot;comment&quot;-identifier in {@link #keys}. */
	public static final int COMMENT = 6;

	/** Stores the generic identifiers of commonly used fields. */
	private final static String[] keys = { "ARTIST", "ALBUM", "TITLE", "TRACK",
			"YEAR", "GENRE", "COMMENT", };

	/** @see outag.formats.generic.AbstractTag#createAlbumField(java.lang.String) */
	protected TagField createAlbumField(String content) {
		return new GenericTagTextField(keys[ALBUM], content);
	}

	/** @see outag.formats.generic.AbstractTag#createArtistField(java.lang.String)	 */
	protected TagField createArtistField(String content) {
		return new GenericTagTextField(keys[ARTIST], content);
	}

	/** @see outag.formats.generic.AbstractTag#createCommentField(java.lang.String) */
	protected TagField createCommentField(String content) {
		return new GenericTagTextField(keys[COMMENT], content);
	}

	/** @see outag.formats.generic.AbstractTag#createGenreField(java.lang.String) */
	protected TagField createGenreField(String content) {
		return new GenericTagTextField(keys[GENRE], content);
	}

	/** @see outag.formats.generic.AbstractTag#createTitleField(java.lang.String) */
	protected TagField createTitleField(String content) {
		return new GenericTagTextField(keys[TITLE], content);
	}

	/** @see outag.formats.generic.AbstractTag#createTrackField(java.lang.String) */
	protected TagField createTrackField(String content) {
		return new GenericTagTextField(keys[TRACK], content);
	}

	/** @see outag.formats.generic.AbstractTag#createYearField(java.lang.String) */
	protected TagField createYearField(String content) {
		return new GenericTagTextField(keys[YEAR], content);
	}

	/** @see outag.formats.generic.AbstractTag#getAlbumId() */
	protected String getAlbumId() { return keys[ALBUM];	}

	/** @see outag.formats.generic.AbstractTag#getArtistId() */
	protected String getArtistId() { return keys[ARTIST]; }

	/** @see outag.formats.generic.AbstractTag#getCommentId() */
	protected String getCommentId() { return keys[COMMENT];	}

	/** @see outag.formats.generic.AbstractTag#getGenreId() */
	protected String getGenreId() { return keys[GENRE]; }

	/** @see outag.formats.generic.AbstractTag#getTitleId() */
	protected String getTitleId() { return keys[TITLE]; }

	/** @see outag.formats.generic.AbstractTag#getTrackId() */
	protected String getTrackId() {	return keys[TRACK];	}

	/** @see outag.formats.generic.AbstractTag#getYearId() */
	protected String getYearId() { return keys[YEAR]; }

	/** @see outag.formats.generic.AbstractTag#isAllowedEncoding(java.lang.String) */
	protected boolean isAllowedEncoding(String enc) { return true; }
}