package outag.audioformats;

import java.util.Iterator;
import java.util.List;

import outag.audioformats.generic.TagField;

/** Represents the basic data structure for the default audiolibrary functionality.<br>
 * <b>Code Examples:</b><br>
 * <pre><code>
 * AudioFile file = AudioFileIO.read(new File(&quot;C:\\test.mp3&quot;));
 * 
 * Tag tag = file.getTag();
 * </code></pre> */
public interface Tag {
	/** This final field contains all the tags that id3v1 supports. */
	public static final String[] DEFAULT_GENRES = { "Blues", "Classic Rock",
			"Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz",
			"Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap",
			"Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska",
			"Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient",
			"Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical",
			"Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel",
			"Noise", "AlternRock", "Bass", "Soul", "Punk", "Space",
			"Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic",
			"Gothic", "Darkwave", "Techno-Industrial", "Electronic",
			"Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy",
			"Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle",
			"Native American", "Cabaret", "New Wave", "Psychadelic", "Rave",
			"Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk",
			"Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll",
			"Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing",
			"Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass",
			"Avantgarde", "Gothic Rock", "Progressive Rock",
			"Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band",
			"Chorus", "Easy Listening", "Acoustic", "Humour", "Speech",
			"Chanson", "Opera", "Chamber Music", "Sonata", "Symphony",
			"Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam",
			"Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad",
			"Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo",
			"A capella", "Euro-House", "Dance Hall" };

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