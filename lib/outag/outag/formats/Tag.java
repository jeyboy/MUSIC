package outag.formats;

import java.util.Iterator;
import java.util.List;

import outag.formats.generic.TagField;

/** Represents the basic data structure for the default audiolibrary functionality.<br>
 * <b>Code Examples:</b><br>
 * <pre><code>
 * AudioFile file = AudioFileIO.read(new File(&quot;C:\\test.mp3&quot;));
 * 
 * Tag tag = file.getTag();
 * </code></pre> */
public interface Tag {
	/** Adds a tagfield to the structure.<br>
	 * @param field - The field to add. */
	public void add(TagField field);

	/** Adds an album to the tag.<br>
	 * @param album - Album description */
	public void addAlbum(String album);

	/** Adds an artist to the tag.<br>
	 * @param artist - Artist's name */
	public void addArtist(String artist);

	/** Adds a comment to the tag.<br>
	 * @param comment - Comment. */
	public void addComment(String comment);

	/** Adds a genre to the tag.<br>
	 * @param genre - Genre */
	public void addGenre(String genre);

	/** Adds a title to the tag.<br>
	 * @param title - Title */
	public void addTitle(String title);

	/** Adds a track to the tag.<br>
	 * @param track - Track */
	public void addTrack(String track);

	/** Adds a year to the Tag.<br>
	 * @param year - Year */
	public void addYear(String year);

	/** @return a {@linkplain List list} of {@link TagField} objects whose &quot;{@linkplain TagField#getId() id}&quot; is the specified one.<br>
	 * @param id - The field id. */
	public List get(String id);

	public List getAlbum();

	public List getArtist();

	public List getComment();

	public Iterator getFields();

	public String getFirstAlbum();

	public String getFirstArtist();

	public String getFirstComment();

	public String getFirstGenre();

	public String getFirstTitle();

	public String getFirstTrack();

	public String getFirstYear();

	public List getGenre();

	public List getTitle();

	public List getTrack();

	public List getYear();

	/** @return <code>true</code> if a {@linkplain TagField#isCommon() common} field is present. */
	public boolean hasCommonFields();

	/** Determines whether the tag has at least one field with the specified &quot;id&quot;.
	 * @param id - The field id to look for.
	 * @return <code>true</code> if tag contains a {@link TagField} with the given {@linkplain TagField#getId() id}. */
	public boolean hasField(String id);

	/** Determines whether the tag has no fields specified.
	 * @return <code>true</code> if tag contains no field. */
	public boolean isEmpty();

	public void merge(Tag tag);

	public void set(TagField field);

	public void setAlbum(String s);

	public void setArtist(String s);

	public void setComment(String s);

	public boolean setEncoding(String enc);

	public void setGenre(String s);

	public void setTitle(String s);

	public void setTrack(String s);

	public void setYear(String s);

	public String toString();
}