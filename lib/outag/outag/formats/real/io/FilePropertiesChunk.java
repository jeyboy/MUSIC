package outag.formats.real.io;

import java.io.DataInputStream;

/** PROP chunk */
public class FilePropertiesChunk {
	public int maxBitrate;
	public int averageBitrate;
	int maxDataPacketSize;
	int averageDataPacketSize;
	int dataPacketsCount;
	/** file common duration */
	public int duration; //in ms
	int playBufferInMs;
	/** Offset of the first INDX chunk form the start of the file */
	int INDX_chunkOffset;
	
	/** Offset of the first DATA chunk form the start of the file */
	int DATA_chunkOffset;
	short streamCount;
	
	/**
	 * <ol start="0">
	 *   <li>bit : file can be saved on disk</li>
	 *   <li>bit : PerfectPlay can be used (extra buffering)</li>
	 *   <li>bit : the file is a live broadcast </li>
	 *  </ol>
	 * */
	short flags;
	
//	dword  Chunk type ('PROP')
//	dword  Chunk size (typically 0x32)
//	word   Chunk version (always 0, for every known file)
//	dword  Maximum bit rate
//	dword  Average bit rate
//	dword  Size of largest data packet
//	dword  Average size of data packet
//	dword  Number of data packets in the file
//	dword  File duration in ms
//	dword  Suggested number of ms to buffer before starting playback
//	dword  Offset of the first INDX chunk form the start of the file
//	dword  Offset of the first DATA chunk form the start of the file
//	word   Number of streams in the file
//	word   Flags (bitfield, see below)	
	
	public FilePropertiesChunk(DataInputStream f) throws Exception {
		maxBitrate = f.readInt();
		averageBitrate = f.readInt();
		maxDataPacketSize = f.readInt();
		averageDataPacketSize = f.readInt();
		dataPacketsCount = f.readInt();
		duration = f.readInt();
		playBufferInMs = f.readInt();
		INDX_chunkOffset = f.readInt();
		DATA_chunkOffset = f.readInt();
		streamCount = f.readShort();
		flags = f.readShort();		
	}
}