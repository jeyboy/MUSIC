package outag.formats.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import outag.formats.Tag;

/** This class is the default implementation for
 * {@link outag.formats.Tag} and introduces some more useful
 * functionality to be implemented. */
public abstract class AbstractTag implements Tag {

	/** Stores the amount of {@link TagField} with {@link TagField#isCommon()} <code>true</code>. */
	protected int commonNumber = 0;

	/** Stores the {@linkplain TagField#getId() ids} of the stored
	 * fields to the {@linkplain TagField fields} themselves. */
	protected HashMap<String, List> fields = new HashMap<String, List>();

	/** @see outag.formats.Tag#add(outag.audioformats.generic.TagField) */
	public void add(TagField field) {
		if (field == null || field.isEmpty())
			return;

		List<TagField> list = (List<TagField>) fields.get(field.getId());

		// There was no previous item
		if (list == null) {
			list = new ArrayList<TagField>();
			list.add(field);
			fields.put(field.getId(), list);
			if (field.isCommon())
				commonNumber++;
		} else {
			// We append to existing list
			list.add(field);
		}
	}

	/** @see outag.formats.Tag#addAlbum(java.lang.String) */
	public void addAlbum(String s) { add(createAlbumField(s)); }

	/** @see outag.formats.Tag#addArtist(java.lang.String) */
	public void addArtist(String s) { add(createArtistField(s)); }

	/** @see outag.formats.Tag#addComment(java.lang.String) */
	public void addComment(String s) { add(createCommentField(s)); }

	/** @see outag.formats.Tag#addGenre(java.lang.String) */
	public void addGenre(String s) { add(createGenreField(s)); }

	/** @see outag.formats.Tag#addTitle(java.lang.String) */
	public void addTitle(String s) { add(createTitleField(s)); }

	/** @see outag.formats.Tag#addTrack(java.lang.String) */
	public void addTrack(String s) { add(createTrackField(s)); }

	/** @see outag.formats.Tag#addYear(java.lang.String) */
	public void addYear(String s) { add(createYearField(s)); }

	/** Creates a field which represents the &quot;album&quot;.<br>
	 * The field will already contain the given content.
	 * @param content - The content of the created field.
	 * @return tag field representing the &quot;album&quot;*/
	protected abstract TagField createAlbumField(String content);

	/** Creates a field which represents the &quot;artist&quot;.<br>
	 * The field will already contain the given content.
	 * @param content - The content of the created field.
	 * @return tag field representing the &quot;artist&quot; */
	protected abstract TagField createArtistField(String content);

	/** Creates a field which represents the &quot;comment&quot;.<br>
	 * The field will already contain the given content.
	 * @param content - The content of the created field.
	 * @return tag field representing the &quot;comment&quot; */
	protected abstract TagField createCommentField(String content);

	/** Creates a field which represents the &quot;genre&quot;.<br>
	 * The field will already contain the given content.
	 * @param content - The content of the created field.
	 * @return tag field representing the &quot;genre&quot; */
	protected abstract TagField createGenreField(String content);

	/** Creates a field which represents the &quot;title&quot;.<br>
	 * The field will already contain the given content.
	 * @param content - The content of the created field.
	 * @return tag field representing the &quot;title&quot; */
	protected abstract TagField createTitleField(String content);

	/** Creates a field which represents the &quot;track&quot;.<br>
	 * The field will already contain the given content.
	 * @param content - The content of the created field.
	 * @return tag field representing the &quot;track&quot; */
	protected abstract TagField createTrackField(String content);

	/** Creates a field which represents the &quot;year&quot;.<br>
	 * The field will already contain the given content.
	 * @param content - The content of the created field.
	 * @return tag field representing the &quot;year&quot; */
	protected abstract TagField createYearField(String content);

	/** @see outag.formats.Tag#get(java.lang.String) */
	public List get(String id) {
		List list = (List) fields.get(id);
		return (list == null) ? new ArrayList<Object>() : list;
	}

	/** @see outag.formats.Tag#getAlbum() */
	public List getAlbum() { return get(getAlbumId()); }

	/** @see TagField#getId()
	 * @return identifier for the &quot;album&quot; field. */
	protected abstract String getAlbumId();

	/** @see outag.formats.Tag#getArtist() */
	public List getArtist() { return get(getArtistId()); }

	/** @see TagField#getId()
	 * @return identifier for the &quot;artist&quot; field. */
	protected abstract String getArtistId();

	/** @see outag.formats.Tag#getComment() */
	public List getComment() { return get(getCommentId()); }

	/** @see TagField#getId()
	 * @return identifier for the &quot;comment&quot; field. */
	protected abstract String getCommentId();

	/** @see outag.formats.Tag#getFields() */
	public Iterator getFields() {
		final Iterator it = this.fields.entrySet().iterator();
		return new Iterator<Object>() {
			private Iterator<?> fieldsIt;

			private void changeIt() {
				if (!it.hasNext())
					return;

				List<?> l = (List<?>) ((Map.Entry) it.next()).getValue();
				fieldsIt = l.iterator();
			}

			public boolean hasNext() {
				if (fieldsIt == null) {
					changeIt();
				}
				return it.hasNext() || (fieldsIt != null && fieldsIt.hasNext());
			}

			public Object next() {
				if (!fieldsIt.hasNext())
					changeIt();

				return fieldsIt.next();
			}

			public void remove() {
				fieldsIt.remove();
			}
		};
	}

	/** @see outag.formats.Tag#getFirstAlbum() */
	public String getFirstAlbum() {
		List l = get(getAlbumId());
		return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
	}

	/** @see outag.formats.Tag#getFirstArtist() */
	public String getFirstArtist() {
		List l = get(getArtistId());
		return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
	}

	/** @see outag.formats.Tag#getFirstComment() */
	public String getFirstComment() {
		List l = get(getCommentId());
		return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
	}

	/** @see outag.formats.Tag#getFirstGenre() */
	public String getFirstGenre() {
		List l = get(getGenreId());
		return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
	}

	/** @see outag.formats.Tag#getFirstTitle() */
	public String getFirstTitle() {
		List l = get(getTitleId());
		return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
	}

	/** @see outag.formats.Tag#getFirstTrack() */
	public String getFirstTrack() {
		List l = get(getTrackId());
		return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
	}

	/** @see outag.formats.Tag#getFirstYear() */
	public String getFirstYear() {
		List l = get(getYearId());
		return (l.size() != 0) ? ((TagTextField) l.get(0)).getContent() : "";
	}

	/** @see outag.formats.Tag#getGenre() */
	public List getGenre() { return get(getGenreId()); }

	/** @see TagField#getId()
	 * @return identifier for the &quot;genre&quot; field. */
	protected abstract String getGenreId();

	/** @see outag.formats.Tag#getTitle() */
	public List getTitle() { return get(getTitleId()); }

	/** @see TagField#getId()
	 * @return identifier for the &quot;title&quot; field. */
	protected abstract String getTitleId();

	/** @see outag.formats.Tag#getTrack() */
	public List getTrack() { return get(getTrackId()); }

	/** @see TagField#getId()
	 * @return identifier for the &quot;track&quot; field. */
	protected abstract String getTrackId();

	/** @see outag.formats.Tag#getYear() */
	public List getYear() { return get(getYearId()); }

	/** @see TagField#getId()
	 * @return identifier for the &quot;year&quot; field. */
	protected abstract String getYearId();

	/** @see outag.formats.Tag#hasCommonFields() */
	public boolean hasCommonFields() { return commonNumber != 0; }

	/** @see outag.formats.Tag#hasField(java.lang.String) */
	public boolean hasField(String id) { return get(id).size() != 0; }

	/** Determines whether the given charset encoding may be used for the
	 * represented tagging system.
	 * @param enc - charset encoding.
	 * @return <code>true</code> if the given encoding can be used. */
	protected abstract boolean isAllowedEncoding(String enc);

	/** @see outag.formats.Tag#isEmpty() */
	public boolean isEmpty() { return fields.size() == 0; }

	/** @see outag.formats.Tag#merge(outag.formats.Tag) */
	public void merge(Tag tag) {
		// FIXME: Improve me, for the moment,
		// it overwrites this tag with other values
		// FIXME: TODO: an abstract method that merges particular things for
		// each
		// format
		if (getTitle().size() == 0)
			setTitle(tag.getFirstTitle());
		if (getArtist().size() == 0)
			setArtist(tag.getFirstArtist());
		if (getAlbum().size() == 0)
			setAlbum(tag.getFirstAlbum());
		if (getYear().size() == 0)
			setYear(tag.getFirstYear());
		if (getComment().size() == 0)
			setComment(tag.getFirstComment());
		if (getTrack().size() == 0)
			setTrack(tag.getFirstTrack());
		if (getGenre().size() == 0)
			setGenre(tag.getFirstGenre());
	}

	/** @see outag.formats.Tag#set(outag.audioformats.generic.TagField) */
	public void set(TagField field) {
		if (field == null) return;

		// If an empty field is passed, we delete all the previous ones
		if (field.isEmpty()) {
			Object removed = fields.remove(field.getId());
			if (removed != null && field.isCommon())
				commonNumber--;
			return;
		}

		// If there is already an existing field with same id
		// and both are TextFields, we update the first element
		List<TagField> l = (List<TagField>) fields.get(field.getId());
		if (l != null) {
			TagField f = (TagField) l.get(0);
			f.copyContent(field);
			return;
		}

		// Else we put the new field in the fields.
		l = new ArrayList<TagField>();
		l.add(field);
		fields.put(field.getId(), l);
		if (field.isCommon())
			commonNumber++;
	}

	/** @see outag.formats.Tag#setAlbum(java.lang.String) */
	public void setAlbum(String s) { set(createAlbumField(s)); }

	/** @see outag.formats.Tag#setArtist(java.lang.String) */
	public void setArtist(String s) { set(createArtistField(s)); }

	/** @see outag.formats.Tag#setComment(java.lang.String) */
	public void setComment(String s) { set(createCommentField(s)); }

	/** @see outag.formats.Tag#setEncoding(java.lang.String) */
	public boolean setEncoding(String enc) {
		if (!isAllowedEncoding(enc))
			return false;

		Iterator<TagField> it = getFields();
		while (it.hasNext()) {
			TagField field = (TagField) it.next();
			if (field instanceof TagTextField)
				((TagTextField) field).setEncoding(enc);
		}

		return true;
	}

	/** @see outag.formats.Tag#setGenre(java.lang.String) */
	public void setGenre(String s) { set(createGenreField(s)); }

	/** @see outag.formats.Tag#setTitle(java.lang.String) */
	public void setTitle(String s) { set(createTitleField(s)); }

	/** @see outag.formats.Tag#setTrack(java.lang.String) */
	public void setTrack(String s) { set(createTrackField(s)); }

	/** @see outag.formats.Tag#setYear(java.lang.String) */
	public void setYear(String s) { set(createYearField(s)); }

	/** @see java.lang.Object#toString() */
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append("Tag content:\n");
		Iterator<?> it = getFields();
		while (it.hasNext()) {
			TagField field = (TagField) it.next();
			out.append("\t");
			out.append(field.getId());
			out.append(" : ");
			out.append(field.toString());
			out.append("\n");
		}
		return out.toString().substring(0, out.length() - 1);
	}
}