package outag.audioformats.generic;

/** Complete implementation of {@link outag.audioformats.generic.AbstractTag}.<br>
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

		/** @see outag.audioformats.generic.TagField#copyContent(outag.audioformats.generic.TagField) */
		public void copyContent(TagField field) {
			if (field instanceof TagTextField)
				this.content = ((TagTextField) field).getContent();
		}

		/** @see outag.audioformats.generic.TagTextField#getContent() */
		public String getContent() { return this.content; }

		/** @see outag.audioformats.generic.TagTextField#getEncoding() */
		public String getEncoding() { return "ISO-8859-1"; }

		/** @see outag.audioformats.generic.TagField#getId() */
		public String getId() {	return id; }

		/** @see outag.audioformats.generic.TagField#getRawContent() */
		public byte[] getRawContent() {	return this.content == null ? new byte[] {} : this.content.getBytes(); }

		/** @see outag.audioformats.generic.TagField#isBinary() */
		public boolean isBinary() { return false; }

		/** @see outag.audioformats.generic.TagField#isBinary(boolean) */
		public void isBinary(boolean b) { /* not supported */ }

		/** @see outag.audioformats.generic.TagField#isCommon() */
		public boolean isCommon() {	return true; }

		/** @see outag.audioformats.generic.TagField#isEmpty() */
		public boolean isEmpty() { return this.content.equals(""); }

		/** @see outag.audioformats.generic.TagTextField#setContent(java.lang.String) */
		public void setContent(String s) { this.content = s; }

		/** @see outag.audioformats.generic.TagTextField#setEncoding(java.lang.String) */
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

	/** @see outag.audioformats.generic.AbstractTag#createAlbumField(java.lang.String) */
	protected TagField createAlbumField(String content) {
		return new GenericTagTextField(keys[ALBUM], content);
	}

	/** @see outag.audioformats.generic.AbstractTag#createArtistField(java.lang.String)	 */
	protected TagField createArtistField(String content) {
		return new GenericTagTextField(keys[ARTIST], content);
	}

	/** @see outag.audioformats.generic.AbstractTag#createCommentField(java.lang.String) */
	protected TagField createCommentField(String content) {
		return new GenericTagTextField(keys[COMMENT], content);
	}

	/** @see outag.audioformats.generic.AbstractTag#createGenreField(java.lang.String) */
	protected TagField createGenreField(String content) {
		return new GenericTagTextField(keys[GENRE], content);
	}

	/** @see outag.audioformats.generic.AbstractTag#createTitleField(java.lang.String) */
	protected TagField createTitleField(String content) {
		return new GenericTagTextField(keys[TITLE], content);
	}

	/** @see outag.audioformats.generic.AbstractTag#createTrackField(java.lang.String) */
	protected TagField createTrackField(String content) {
		return new GenericTagTextField(keys[TRACK], content);
	}

	/** @see outag.audioformats.generic.AbstractTag#createYearField(java.lang.String) */
	protected TagField createYearField(String content) {
		return new GenericTagTextField(keys[YEAR], content);
	}

	/** @see outag.audioformats.generic.AbstractTag#getAlbumId() */
	protected String getAlbumId() { return keys[ALBUM];	}

	/** @see outag.audioformats.generic.AbstractTag#getArtistId() */
	protected String getArtistId() { return keys[ARTIST]; }

	/** @see outag.audioformats.generic.AbstractTag#getCommentId() */
	protected String getCommentId() { return keys[COMMENT];	}

	/** @see outag.audioformats.generic.AbstractTag#getGenreId() */
	protected String getGenreId() { return keys[GENRE]; }

	/** @see outag.audioformats.generic.AbstractTag#getTitleId() */
	protected String getTitleId() { return keys[TITLE]; }

	/** @see outag.audioformats.generic.AbstractTag#getTrackId() */
	protected String getTrackId() {	return keys[TRACK];	}

	/** @see outag.audioformats.generic.AbstractTag#getYearId() */
	protected String getYearId() { return keys[YEAR]; }

	/** @see outag.audioformats.generic.AbstractTag#isAllowedEncoding(java.lang.String) */
	protected boolean isAllowedEncoding(String enc) { return true; }
}