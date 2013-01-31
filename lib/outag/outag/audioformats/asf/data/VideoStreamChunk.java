package outag.audioformats.asf.data;

import java.math.BigInteger;

import outag.audioformats.asf.util.Utils;

public class VideoStreamChunk extends StreamChunk {

	/** Stores the codecs id. Normally the Four-CC (4-Bytes). */
	private byte[] codecId;

	/** Stores the height of the video stream. */
	private long pictureHeight;

	/** Stores the width of the video stream. */
	private long pictureWidth;

	/** Creates an instance.
	 * @param pos - Position of the current chunk in the asf file or stream.
	 * @param chunkLen - Length of the entire chunk (including guid and size) */
	public VideoStreamChunk(long pos, BigInteger chunkLen) { super(pos, chunkLen); }

	/** @return Returns the codecId. */
	public byte[] getCodecId() { return this.codecId; }

	/** @return the {@link #getCodecId()}, as a String, where each byte has been converted to a <code>char</code>.*/
	public String getCodecIdAsString() { return (getCodecId() != null) ? new String(getCodecId()) : "Unknown"; }

	/** @return the pictureHeight. */
	public long getPictureHeight() { return pictureHeight; }

	/** @return the pictureWidth. */
	public long getPictureWidth() { return pictureWidth; }

	public String prettyPrint() {
		StringBuffer result = new StringBuffer(super.prettyPrint().replaceAll(
				Utils.LINE_SEPARATOR, Utils.LINE_SEPARATOR + "   "));
		result.insert(0, Utils.LINE_SEPARATOR + "VideoStream");
		result.append("Video info:" + Utils.LINE_SEPARATOR);
		result.append("      Width  : " + getPictureWidth()
				+ Utils.LINE_SEPARATOR);
		result.append("      Heigth : " + getPictureHeight()
				+ Utils.LINE_SEPARATOR);
		result.append("      Codec  : " + getCodecIdAsString()
				+ Utils.LINE_SEPARATOR);
		return result.toString();
	}

	/** @param codecId - The codecId to set. */
	public void setCodecId(byte[] codecId) { this.codecId = codecId; }

	/** @param picHeight */
	public void setPictureHeight(long picHeight) { this.pictureHeight = picHeight; }

	/** @param picWidth */
	public void setPictureWidth(long picWidth) { this.pictureWidth = picWidth; }
}