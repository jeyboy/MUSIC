package outag.audioformats.asf.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

import outag.audioformats.asf.data.Chunk;
import outag.audioformats.asf.data.FileHeader;
import outag.audioformats.asf.data.GUID;
import outag.audioformats.asf.util.Utils;

/** Reads and interprets the data of the file header. */
public class FileHeaderReader {
	/** Creates and fills a {@link FileHeader}from given file. <br>
	 * @param raf - Input
	 * @param candidate - Chunk which possibly is a file header.
	 * @return FileHeader if filepointer of <code>raf</code> is at valid fileheader.
	 * @throws IOException - Read errors. */
	public static FileHeader read(RandomAccessFile raf, Chunk candidate)
			throws IOException {
		if (raf == null || candidate == null) 
			throw new IllegalArgumentException("Arguments must not be null.");
		
		if (GUID.GUID_FILE.equals(candidate.getGuid())) {
			raf.seek(candidate.getPosition());
			return new FileHeaderReader().parseData(raf);
		}
		return null;
	}

	/** Should not be used for now. */
	protected FileHeaderReader() { /* NOTHING toDo */ }

	/** Extract an ASF file header object out of the given input. 
	 * @param raf
	 * @return <code>null</code> if no valid file header object.
	 * @throws IOException */
	private FileHeader parseData(RandomAccessFile raf) throws IOException {
		FileHeader result = null;
		long fileHeaderStart = raf.getFilePointer();
		GUID guid = Utils.readGUID(raf);
		if (GUID.GUID_FILE.equals(guid)) {
			BigInteger chunckLen = Utils.readBig64(raf);
			// Skip client GUID.
			raf.skipBytes(16);

			BigInteger fileSize = Utils.readBig64(raf);
			if (fileSize.intValue() != raf.length()) {
				System.err
						.println("Filesize of file doesn't match len of Fileheader. ("
								+ fileSize.toString() + ", file: "+raf.length()+")");
			}
			// fileTime in 100 ns since midnight of 1st january 1601 GMT
			BigInteger fileTime = Utils.readBig64(raf);

			BigInteger packageCount = Utils.readBig64(raf);

			BigInteger timeEndPos = Utils.readBig64(raf);
			BigInteger duration = Utils.readBig64(raf);
			BigInteger timeStartPos = Utils.readBig64(raf);

			long flags = Utils.readUINT32(raf);

			long minPkgSize = Utils.readUINT32(raf);
			long maxPkgSize = Utils.readUINT32(raf);
			long uncompressedFrameSize = Utils.readUINT32(raf);

			result = new FileHeader(fileHeaderStart, chunckLen, fileSize,
					fileTime, packageCount, duration, timeStartPos, timeEndPos,
					flags, minPkgSize, maxPkgSize, uncompressedFrameSize);
		}
		return result;
	}
}