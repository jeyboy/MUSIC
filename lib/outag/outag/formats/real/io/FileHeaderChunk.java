package outag.formats.real.io;

import java.io.DataInputStream;

/** .RMF chunk */
public class FileHeaderChunk {
	long file_version = -1;
	long num_headers = -1;
	
//	dword chunk type ('.RMF')
//	dword chunk size (typically 0x12)
//	word  chunk version (always 0, for every known file)
//	dword file version
//	dword number of headers	
	
	public FileHeaderChunk(DataInputStream f) throws Exception {
		file_version = f.readInt();
		num_headers = f.readInt();			
	}
}