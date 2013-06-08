package outag.formats.real.io;

import java.io.DataInputStream;
import java.util.Vector;

import outag.formats.real.utils.IndexEntry;

/** INDX chunk. Contains index entries. It comes after all the DATA chunks. 
 * An index chunk contains data for a single stream, A file can have more than one INDX chunk. */
public class IndexChunk {
	
//	dword   Number of entries in this chunk
//	word    Stream number
//	dword   Offset of the next INDX chunk (form the start of the file)
//	byte[]  Index entries
	
	/** Offset of the next INDX chunk (form the start of the file) */
	public int nextIndexChunkOffset;
	public Vector<IndexEntry> entries;
	public short streamID;
	
	public IndexChunk(DataInputStream f) throws Exception {		
		int entriesCount = f.readInt();
		entries = new Vector<IndexEntry>(entriesCount);
		streamID = f.readShort();
		nextIndexChunkOffset = f.readInt();
		for(int loop = 0; loop < entriesCount; loop++)
			entries.add(new IndexEntry(f));
	}
}