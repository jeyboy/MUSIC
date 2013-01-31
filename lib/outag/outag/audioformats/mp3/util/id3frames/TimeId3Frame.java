package outag.audioformats.mp3.util.id3frames;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Calendar;

/** Specialization of a text field represents ID3v2 timestamp fields since
 * 2.4.<br>
 * <b>Warning:</b> For now only the year will be processed. Other information
 * will be discarded.
 * <p>
 * The Time field allows various patterns:<br>
 * <ul>
 * <li>yyyy</li>
 * <li>yyyy-MM</li>
 * <li>yyyy-MM-dd</li>
 * <li>yyyy-MM-ddTHH</li>
 * <li>yyyy-MM-ddTHH:mm</li>
 * <li>yyyy-MM-ddTHH:mm:ss</li>
 * </ul>
 * </p> */
public class TimeId3Frame extends TextId3Frame {

	/** Creates the string representation for the time value given by the calendar.<br>
	 * <b>Warning:</b> For now only the year will be processed. Other information will be discarded.
	 * @param calendar - the time to create a string of.
	 * @return A string storing the id3v2.4 timestamp. */
	private static String createString(Calendar calendar) {
		StringBuffer result = new StringBuffer();
		result.append(new DecimalFormat("0000").format(calendar
				.get(Calendar.YEAR)));
		return result.toString();
	}

	/** Creates a timestamp field.
	 * @param fieldId - frame identifier.
	 * @param rawContent - the raw data of the frame.
	 * @param version - the version identifier of the tag.
	 * @throws UnsupportedEncodingException
	 *             When encoding is not supported. */
	public TimeId3Frame(String fieldId, byte[] rawContent, byte version)
			throws UnsupportedEncodingException {
		super(fieldId, rawContent, version);
	}

	/** Creates a timestamp field.
	 * @param fieldId - frame identifier.
	 * @param calendar - Calendar containing the represented time. */
	public TimeId3Frame(String fieldId, Calendar calendar) { super(fieldId, createString(calendar)); }

	/** Creates a timestamp field.
	 * @param fieldId - frame identifier.
	 * @param content - The content of the field in string representation. */
	public TimeId3Frame(String fieldId, String content) { super(fieldId, content); }

	/** @see outag.audioformats.mp3.util.id3frames.TextId3Frame#populate(byte[]) */
	protected void populate(byte[] raw) throws UnsupportedEncodingException {
		super.populate(raw);
		this.content = this.content.substring(0, 4);
	}
}