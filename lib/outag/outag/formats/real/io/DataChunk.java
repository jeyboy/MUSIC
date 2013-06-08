package outag.formats.real.io;

import java.io.DataInputStream;
import java.util.Vector;

import outag.formats.real.utils.DataPacket;

/** DATA chunk */
// TODO : Lossless mode not realised
public class DataChunk {
	/** Offset of the next DATA chunk (form the start of the file) */
	int nextDataChunkOffset = -1;
	Vector<DataPacket> dataPackets;

	
//	dword   Chunk type ('DATA')
//	dword   Chunk size
//	word    Chunk version (always 0, for every known file)
//	dword   Number of data packets in this chunk
//	dword   Offset of the next DATA chunk (form the start of the file)
//	byte[]  Data packets
	
	public DataChunk(DataInputStream f) throws Exception {		
		int dataPacketsNum = f.readInt();
		dataPackets = new Vector<DataPacket>(dataPacketsNum);
		nextDataChunkOffset = f.readInt(); // Maybe need offset file point before next data chunk proceeding with this value
		
		for(int packetNum = 0; packetNum < dataPacketsNum; packetNum++)
			dataPackets.add(new DataPacket(f));
	}
}