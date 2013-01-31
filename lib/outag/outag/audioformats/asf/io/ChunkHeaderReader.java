package outag.audioformats.asf.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

import outag.audioformats.asf.data.Chunk;
import outag.audioformats.asf.data.GUID;
import outag.audioformats.asf.util.Utils;

/** Default reader, Reads GUID and size out of an inputsream and creates a
 * {@link outag.audioformats.asf.data.Chunk}object. */
class ChunkHeaderReader {
	/** Interprets current data as a header of a chunk.
	 * @param input - inputdata
	 * @return Chunk.
	 * @throws IOException - Access errors. */
	public static Chunk readChunckHeader(RandomAccessFile input)
			throws IOException {
		long pos = input.getFilePointer();
		GUID guid = Utils.readGUID(input);
		BigInteger chunkLength = Utils.readBig64(input);
		return new Chunk(guid, pos, chunkLength);
	}
}