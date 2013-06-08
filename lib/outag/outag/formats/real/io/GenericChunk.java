package outag.formats.real.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.RandomAccessFile;

import outag.formats.exceptions.UnsupportedException;
import outag.formats.generic.Utils;

/** Common chunk */
public class GenericChunk {
	public String obj_id;
	public DataInputStream data;
	
	
	public GenericChunk(RandomAccessFile f) throws Exception {
		obj_id = Utils.readString(f, 4);
		
		int size = f.readInt();
		short obj_version = f.readShort();		
		
		if ((obj_version != 0) && (obj_version != 1))
			throw new UnsupportedException("Unsupported header version");		
		
		byte[] bytes = new byte[size - 10];
		f.readFully(bytes);
		data = new DataInputStream(new ByteArrayInputStream(bytes));
	}
}