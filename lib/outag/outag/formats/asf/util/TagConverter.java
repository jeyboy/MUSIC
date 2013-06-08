package outag.formats.asf.util;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import outag.formats.Tag;
import outag.formats.asf.data.AsfHeader;
import outag.formats.asf.data.ContentDescription;
import outag.formats.asf.data.ContentDescriptor;
import outag.formats.asf.data.ExtendedContentDescription;
import outag.formats.asf.data.wrapper.ContentDescriptorTagField;
import outag.formats.generic.GenericTag;
import outag.formats.generic.TagField;
import outag.formats.generic.TagTextField;
import outag.reference.GenreTypes;

/** Provides functionality to convert
 * {@link outag.formats.asf.data.AsfHeader} objects into
 * {@link outag.formats.Tag} objects (More extract information and
 * create a {@link outag.formats.generic.GenericTag}). */
public class TagConverter {

	/** Assigns those tags of <code>tag</code> which are defined to be common by outag. <br>
	 * @param tag - The tag from which the values are gathered. <br>
	 *            Assigned values are: <br>
	 * @see Tag#getAlbum() <br>
	 * @see Tag#getTrack() <br>
	 * @see Tag#getYear() <br>
	 * @see Tag#getGenre() <br>
	 * @param description
	 *            The extended content description which should receive the values. <br>
	 *            <b>Warning: </b> the common values will be replaced. */
	public static void assignCommonTagValues(Tag tag,
			ExtendedContentDescription description) {
		ContentDescriptor tmp = null;
		if (tag.getFirstAlbum() != null && tag.getFirstAlbum().length() > 0) {
			tmp = new ContentDescriptor(ContentDescriptor.ID_ALBUM,
					ContentDescriptor.TYPE_STRING);
			tmp.setStringValue(tag.getFirstAlbum());
			description.addOrReplace(tmp);
		} else {
			description.remove(ContentDescriptor.ID_ALBUM);
		}
		if (tag.getFirstTrack() != null && tag.getFirstTrack().length() > 0) {
			tmp = new ContentDescriptor(ContentDescriptor.ID_TRACKNUMBER,
					ContentDescriptor.TYPE_STRING);
			tmp.setStringValue(tag.getFirstTrack());
			description.addOrReplace(tmp);
		} else {
			description.remove(ContentDescriptor.ID_TRACKNUMBER);
		}
		if (tag.getFirstYear() != null && tag.getFirstYear().length() > 0) {
			tmp = new ContentDescriptor(ContentDescriptor.ID_YEAR,
					ContentDescriptor.TYPE_STRING);
			tmp.setStringValue(tag.getFirstYear());
			description.addOrReplace(tmp);
		} else {
			description.remove(ContentDescriptor.ID_YEAR);
		}
		if (tag.getFirstGenre() != null && tag.getFirstGenre().length() > 0) {
			tmp = new ContentDescriptor(ContentDescriptor.ID_GENRE,
					ContentDescriptor.TYPE_STRING);
			tmp.setStringValue(tag.getFirstGenre());
			description.addOrReplace(tmp);
			
			int index = GenreTypes.getCodeByName(tag.getFirstGenre());
			if (index != -1) {
				tmp = new ContentDescriptor(ContentDescriptor.ID_GENREID,
						ContentDescriptor.TYPE_STRING);
				tmp.setStringValue("(" + index + ")");
				description.addOrReplace(tmp);
			} else {
				description.remove(ContentDescriptor.ID_GENREID);
			}
		} else {
			description.remove(ContentDescriptor.ID_GENRE);
			description.remove(ContentDescriptor.ID_GENREID);
		}
	}

	/** Add or replace all values of tag are not defined as common by outag.
	 * @param tag - The tag containing the values.
	 * @param descriptor - the extended content description. */
	public static void assignOptionalTagValues(Tag tag,
			ExtendedContentDescription descriptor) {
		Iterator<?> it = tag.getFields();
		ContentDescriptor tmp = null;
		while (it.hasNext()) {
			try {
				TagField currentField = (TagField) it.next();
				if (!currentField.isCommon()) {
					tmp = new ContentDescriptor(currentField.getId(),
							ContentDescriptor.TYPE_STRING);
					if (currentField.isBinary()) {
						tmp.setBinaryValue(currentField.getRawContent());
					} else {
						tmp.setStringValue(currentField.toString());
					}
					descriptor.addOrReplace(tmp);
				}
			} catch (UnsupportedEncodingException uee) {
				uee.printStackTrace();
			}
		}
	}

	/** Creates a new {@link ContentDescription}object, filled with
	 * the according values of the given <code>tag</code>.<br>
	 * <b>Warning </b>: <br>
	 * Only the first values can be stored in asf files, because the content description is limited.
	 * @param tag - The tag from which the values are taken. <br>
	 * @see Tag#getFirstArtist() <br>
	 * @see Tag#getFirstTitle() <br>
	 * @see Tag#getFirstComment() <br>
	 * @return A new content description object filled with <code>tag</code>. */
	public static ContentDescription createContentDescription(Tag tag) {
		ContentDescription result = new ContentDescription();
		result.setAuthor(tag.getFirstArtist());
		result.setTitle(tag.getFirstTitle());
		result.setComment(tag.getFirstComment());
		TagTextField cpField = AsfCopyrightField.getCopyright(tag);
		if (cpField != null) {
			result.setCopyRight(cpField.getContent());
		}
		return result;
	}

	/** Creates a new {@link ExtendedContentDescription}object
	 * filled with the values of the given <code>tag</code>.<br>
	 * Since extended content description of asf files can store name-value
	 * pairs, nearly each {@link outag.formats.generic.TagField}can be
	 * stored whithin. <br>
	 * One constraint is that the strings must be convertable to "UTF-16LE"
	 * encoding and don't exceed a length of 65533 in binary representation.
	 * <br>
	 * 
	 * @param tag - The tag whose values the result will be filled with.
	 * @return A new extended content description object. */
	public static ExtendedContentDescription createExtendedContentDescription(Tag tag) {
		ExtendedContentDescription result = new ExtendedContentDescription();
		assignCommonTagValues(tag, result);
		return result;
	}

	/** Creates a {@link Tag}and fills it with the contents of the given {@link AsfHeader}.<br>
	 * @param source - The asf header which contains the information.
	 * @return A Tag with all its values. */
	public static Tag createTagOf(AsfHeader source) {
		GenericTag result = new GenericTag();
		/*
		 * It is possible that the file contains no content description, since
		 * that some informations aren't available.
		 */
		if (source.getContentDescription() != null) {
			result.setArtist(source.getContentDescription().getAuthor());
			result.setComment(source.getContentDescription().getComment());
			result.setTitle(source.getContentDescription().getTitle());
			AsfCopyrightField cpField = new AsfCopyrightField();
			/*
			 * I know I said use the setString() method. However, the value is
			 * already a "UTF-16LE" string within is bounds. So Nothing could
			 * happen.
			 */
			cpField.setContent(source.getContentDescription().getCopyRight());
			result.set(cpField);
		}
		/*
		 * It is possible that the file contains no extended content
		 * description. In that case some informations cannot be provided.
		 */
		if (source.getExtendedContentDescription() != null) {
			result.setTrack(source.getExtendedContentDescription().getTrack());
			result.setYear(source.getExtendedContentDescription().getYear());
			result.setGenre(source.getExtendedContentDescription().getGenre());
			result.setAlbum(source.getExtendedContentDescription().getAlbum());

			/*
			 * Now any properties, which don't belong to the common section of
			 * outag.
			 */
			ExtendedContentDescription extDesc = source
					.getExtendedContentDescription();
			Iterator<?> it = extDesc.getDescriptors().iterator();
			while (it.hasNext()) {
				ContentDescriptor current = (ContentDescriptor) it.next();
				// If common, it has been added to the result some lines upward.
				if (!current.isCommon()) {
					result.add(new ContentDescriptorTagField(current));
				}
			}
		}
		return result;
	}
}