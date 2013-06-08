package outag.formats.real.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

public class NameValueProperty {
//	NameValueProperty
//	{
//	  ULONG32             size;
//	  UINT16               object_version;
//
//	  if (object_version == 0)
//	  {
//	    UINT8   name_length;
//	    UINT8       name[namd_length];
//	    INT32         type;
//	    UINT16     value_length;
//	    UINT8      value_data[value_length];
//	  }
//	}
	
	public Vector<Byte> names;
	public Vector<Byte> values;
	
	public NameValueProperty(DataInputStream f) throws IOException {
		int size = f.readInt();
		short version = f.readShort();
		
		byte nameCount = f.readByte();
		names = new Vector<Byte>(nameCount);
		for(byte loop1 = 0 ; loop1 < nameCount; loop1++)
			names.add(f.readByte());
		
		int type = f.readInt();

		short valueCount = f.readShort();
		values = new Vector<Byte>(valueCount);
		for(byte loop1 = 0 ; loop1 < valueCount; loop1++)
			values.add(f.readByte());		
	}
}