package outag.audioformats.mp3.util.id3frames;

import java.io.UnsupportedEncodingException;

import outag.audioformats.generic.TagField;
import outag.audioformats.generic.TagTextField;
import outag.audioformats.mp3.Id3v2Tag;

public class TextId3Frame extends Id3Frame implements TagTextField {
	protected String content;

	protected byte encoding;

	protected String id;

	protected boolean common;

	/*
	 * 0,1| frame flags 2| encoding 3,..,(0x00(0x00))| text content
	 */

	public TextId3Frame(String id, String content) {
		this.id = id;
		checkCommon();
		this.content = content;
		setEncoding(Id3v2Tag.DEFAULT_ENCODING);
	}

	public TextId3Frame(String id, byte[] rawContent, byte version)
			throws UnsupportedEncodingException {
		super(rawContent, version);
		this.id = id;
		checkCommon();
	}

	private void checkCommon() {
		// TODO on renaming time field, change this too
		this.common = id.equals("TIT2") || id.equals("TALB")
				|| id.equals("TPE1") || id.equals("TCON") || id.equals("TRCK")
				|| id.equals("TDRC") || id.equals("COMM");
	}

	public String getEncoding() {
		if (encoding == 0)
			return "ISO-8859-1";
		else if (encoding == 1)
			return "UTF-16";
		else if (encoding == 2)
			return "UTF-16BE";
		else if (encoding == 3) {
			return "UTF-8";
		}
		return "ISO-8859-1";
	}

	public void setEncoding(String enc) {
		if ("ISO-8859-1".equals(enc))
			encoding = 0;
		else if ("UTF-16".equals(enc))
			encoding = 1;
		else if ("UTF-16BE".equals(enc))
			encoding = 2;
		else if ("UTF-8".equals(enc))
			encoding = 3;
		else
			encoding = 1;
	}

	public String getContent() { return content; }

	public boolean isBinary() { return false; }

	public String getId() {	return this.id; }

	public boolean isCommon() { return this.common; }

	public void setContent(String s) { this.content = s;}

	public boolean isEmpty() { return this.content.equals(""); }

	public void copyContent(TagField field) {
		if (field instanceof TextId3Frame) {
			this.content = ((TextId3Frame) field).getContent();
			setEncoding(((TextId3Frame) field).getEncoding());
		}
	}

	protected void populate(byte[] raw) throws UnsupportedEncodingException {
		this.encoding = raw[flags.length];
		if (this.encoding != 0 && this.encoding != 1 && this.encoding != 2
				&& this.encoding != 3)
			this.encoding = 0;

		this.content = getString(raw, flags.length + 1, raw.length
				- flags.length - 1, getEncoding());

		int i = this.content.indexOf("\u0000");

		if (i != -1)
			this.content = this.content.substring(0, i);
	}

	protected byte[] build() throws UnsupportedEncodingException {
		byte[] data = getBytes(this.content, getEncoding());
		// the return byte[]
		byte[] b = new byte[4 + 4 + flags.length + 1 + data.length];

		int offset = 0;
		copy(getIdBytes(), b, offset);
		offset += 4;
		copy(getSize(b.length - 10), b, offset);
		offset += 4;
		copy(flags, b, offset);
		offset += flags.length;

		b[offset] = this.encoding;
		offset += 1;

		copy(data, b, offset);

		return b;
	}

	public String toString() { return getContent(); }
}