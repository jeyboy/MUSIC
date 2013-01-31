package outag.audioformats.ogg.util;

import outag.audioformats.ogg.*;
import outag.audioformats.generic.Utils;

import java.io.*;

public class OggTagReader {
	public OggTag read(RandomAccessFile raf) throws IOException {
		OggTag tag = new OggTag();
		
		byte[] b = new byte[4];
		raf.read(b);
		int vendorStringLength = Utils.getNumber(b, 0, 3);
		b = new byte[vendorStringLength];
		raf.read(b);

		tag.setVendor(new String(b, "UTF-8"));
		
		b = new byte[4];
		raf.read(b);
		int userComments = Utils.getNumber(b, 0, 3);

		for (int i = 0; i < userComments; i++) {
			b = new byte[4];
			raf.read(b);
			int commentLength = Utils.getNumber(b, 0, 3);
			b = new byte[commentLength];
			raf.read(b);
			
			OggTagField field = new OggTagField(b);
			tag.add(field);
		}
		
		return tag;
	}
}