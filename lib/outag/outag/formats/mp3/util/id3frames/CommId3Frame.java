package outag.formats.mp3.util.id3frames;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import outag.formats.generic.TagField;
import outag.formats.generic.TagTextField;

public class CommId3Frame extends TextId3Frame implements TagTextField {
	private String shortDesc;

	private String lang;

	/*
	 * 0,1| frame flags 2| encoding 3,4,5| lang 6,..,0x00(0x00)| short descr
	 * x,..| actual comment
	 */

	public CommId3Frame(String content) {
		super("COMM", content);

		this.shortDesc = "";
		// this.lang = "eng";
		this.lang = Locale.getDefault().getISO3Language();
	}

	public CommId3Frame(byte[] rawContent, byte version)
			throws UnsupportedEncodingException {
		super("COMM", rawContent, version);
	}

	public String getLangage() { return this.lang; }

	protected void populate(byte[] raw) throws UnsupportedEncodingException {
		this.encoding = raw[flags.length];
		if (flags.length + 1 + 3 > raw.length - 1) {
			this.lang = "XXX";
			content = "";
			shortDesc = "";
			return;
		}
		this.lang = new String(raw, flags.length + 1, 3);

		int commentStart = getCommentStart(raw, flags.length + 4, getEncoding());

		this.shortDesc = getString(raw, flags.length + 4, commentStart
				- flags.length - 4, getEncoding());
		this.content = getString(raw, commentStart, raw.length - commentStart,
				getEncoding());
		assert lang != null && this.shortDesc != null && this.content != null;
	}

	/** Interprets content to be a valid comment section. where
	 * first comes a short comment directly after that the comment section. This
	 * method searches for the terminal character of the short description, and
	 * return the index of the first byte of the fulltext comment.
	 * @param content - the comment data.
	 * @param offset - the offset where the short descriptions is about to start.
	 * @param encoding - the encoding of the field.
	 * @return the index (including given offset) for the first byte of the
	 *         full text comment. */
	public int getCommentStart(byte[] content, int offset, String encoding) {
		int result = 0;
		if ("UTF-16".equals(encoding)) {
			for (result = offset; result < content.length; result += 2) {
				if (content[result] == 0x00 && content[result + 1] == 0x00) {
					result += 2;
					break;
				}
			}
		} else {
			for (result = offset; result < content.length; result++) {
				if (content[result] == 0x00) {
					result++;
					break;
				}
			}
		}
		return result;
	}

	protected byte[] build() throws UnsupportedEncodingException {
		byte[] shortDescData = getBytes(this.shortDesc, getEncoding());
		byte[] contentData = getBytes(this.content, getEncoding());
		byte[] data = new byte[shortDescData.length + contentData.length];
		System.arraycopy(shortDescData, 0, data, 0, shortDescData.length);
		System.arraycopy(contentData, 0, data, shortDescData.length,
				contentData.length);
		byte[] lan = this.lang.getBytes();

		// the return byte[]
		byte[] b = new byte[4 + 4 + flags.length + 1 + 3 + data.length];

		int offset = 0;
		copy(getIdBytes(), b, offset);
		offset += 4;
		copy(getSize(b.length - 10), b, offset);
		offset += 4;
		copy(flags, b, offset);
		offset += flags.length;

		b[offset] = this.encoding;
		offset += 1;

		copy(lan, b, offset);
		offset += lan.length;

		copy(data, b, offset);

		return b;
	}

	public String getShortDescription() { return shortDesc;	}

	public boolean isEmpty() {
		return this.content.equals("") && this.shortDesc.equals("");
	}

	public void copyContent(TagField field) {
		super.copyContent(field);
		if (field instanceof CommId3Frame) {
			this.shortDesc = ((CommId3Frame) field).getShortDescription();
			this.lang = ((CommId3Frame) field).getLangage();
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(getLangage()).append("] ").append("(").append(
				getShortDescription()).append(") ").append(getContent());
		return sb.toString();
	}
}