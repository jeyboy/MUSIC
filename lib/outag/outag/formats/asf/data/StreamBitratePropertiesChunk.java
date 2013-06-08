package outag.formats.asf.data;

import java.math.BigInteger;
import java.util.ArrayList;

import outag.formats.asf.util.Utils;

/** This class represents the "Stream Bitrate Properties" chunk of an asf media file. <br>
 * It is optional, but contains useful information about the streams bitrate.*/
public class StreamBitratePropertiesChunk extends Chunk {

	/** For each call of {@link #addBitrateRecord(int, long)} an {@link Long}
	 * object is appended, which represents the average bitrate. */
	private final ArrayList<Long> bitRates;

	/** For each call of {@link #addBitrateRecord(int, long)} an {@link Integer}
	 * object is appended, which represents the stream-number. */
	private final ArrayList<Integer> streamNumbers;

	/** Creates an instance.
	 * @param pos - Position of the chunk within file or stream
	 * @param chunkLen - Length of current chunk. */
	public StreamBitratePropertiesChunk(long pos, BigInteger chunkSize) {
		super(GUID.GUID_STREAM_BITRATE_PROPERTIES, pos, chunkSize);
		this.bitRates = new ArrayList<Long>();
		this.streamNumbers = new ArrayList<Integer>();
	}

	/** Adds the public values of a stream-record.
	 * @param streamNum - The number of the refered stream.
	 * @param averageBitrate - Its average Bitrate. */
	public void addBitrateRecord(int streamNum, long averageBitrate) {
		this.streamNumbers.add(new Integer(streamNum));
		this.bitRates.add(new Long(averageBitrate));
	}

	/** Returns the average bitrate of the given stream.<br>
	 * @param streamNumber - Number of the stream whose bitrate to determine.
	 * @return The average bitrate of the numbered stream. <code>-1</code> if no information was given. */
	public long getAvgBitrate(int streamNumber) {
		Integer seach = new Integer(streamNumber);
		int index = streamNumbers.indexOf(seach);
		if (index != -1) {
			return ((Long) bitRates.get(index)).longValue();
		}
		return -1;
	}

	public String prettyPrint() {
		StringBuffer result = new StringBuffer(super.prettyPrint());
		result.insert(0, Utils.LINE_SEPARATOR + "Stream Bitrate Properties:"
				+ Utils.LINE_SEPARATOR);
		for (int i = 0; i < bitRates.size(); i++) {
			result.append("   Stream no. \"" + streamNumbers.get(i)
					+ "\" has an average bitrate of \"" + bitRates.get(i)
					+ "\"" + Utils.LINE_SEPARATOR);
		}
		result.append(Utils.LINE_SEPARATOR);
		return result.toString();
	}
}