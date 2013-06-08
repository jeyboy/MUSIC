package outag.formats.asf.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

import outag.formats.asf.data.Chunk;
import outag.formats.asf.data.GUID;
import outag.formats.asf.data.StreamBitratePropertiesChunk;
import outag.formats.asf.util.Utils;

/** This class reads the chunk containing the stream bitrate properties. */
public class StreamBitratePropertiesReader {
	/** Reads the current data and interprets it as an "stream bitrate properties" chunk. <br>
	 * @param raf - Input source
	 * @param candidate - Chunk which possibly contains encoding data.
	 * @return StreamBitratePropertiesChunk, <code>null</code> if its not a valid one. <br>
	 * @throws IOException - read errors. */
	public static StreamBitratePropertiesChunk read(RandomAccessFile raf,
			Chunk candidate) throws IOException {
		if (raf == null || candidate == null) {
			throw new IllegalArgumentException("Arguments must not be null.");
		}
		if (GUID.GUID_STREAM_BITRATE_PROPERTIES.equals(candidate.getGuid())) {
			raf.seek(candidate.getPosition());
			return new StreamBitratePropertiesReader().parseData(raf);
		}
		return null;
	}

	/** Should not be used for now. */
	protected StreamBitratePropertiesReader() { /* NOTHING toDo */ }

	/** see {@link #read(RandomAccessFile, Chunk)}
	 * @param raf - input source.
	 * @return StreamBitratePropertiesChunk, <code>null</code> if its not a valid one. <br>
	 * @throws IOException - read errors. */
	private StreamBitratePropertiesChunk parseData(RandomAccessFile raf)
			throws IOException {
		StreamBitratePropertiesChunk result = null;
		long chunkStart = raf.getFilePointer();
		GUID guid = Utils.readGUID(raf);
		if (GUID.GUID_STREAM_BITRATE_PROPERTIES.equals(guid)) {
			BigInteger chunkLen = Utils.readBig64(raf);
			result = new StreamBitratePropertiesChunk(chunkStart, chunkLen);

			/*
			 * Read the amount of bitrate records
			 */
			long recordCount = Utils.readUINT16(raf);
			for (int i = 0; i < recordCount; i++) {
				int flags = Utils.readUINT16(raf);
				long avgBitrate = Utils.readUINT32(raf);
				result.addBitrateRecord(flags & 0x00FF, avgBitrate);
			}

		}
		return result;
	}
}