package outag.formats.real.utils;

import java.io.DataInputStream;

public class IndexEntry {
//	 word   Entry version (always 0, for every known file)
//	 dword  Timestamp (in ms)
//	 dword  Packet offset in file (form the start of the file)
//	 dword  Packet number
	
	public int timestamp;
	/** Packet offset in file (form the start of the file) */
	public int packetOffset;
	public int packetSequenceNumber;
	
	public IndexEntry(DataInputStream f) throws Exception {
		f.readShort(); // version
		timestamp = f.readInt();
		packetOffset = f.readInt();
		packetSequenceNumber = f.readInt();
	}
}