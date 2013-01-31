package outag.audioformats.ape.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import outag.audioformats.Tag;
import outag.audioformats.ape.ApeTag;
import outag.audioformats.exceptions.CannotReadException;
import outag.audioformats.generic.Utils;

public class ApeTagReader {
	public Tag read(RandomAccessFile raf) throws CannotReadException, IOException {
		ApeTag tag = new ApeTag();
		
		//Check wether the file contains an APE tag--------------------------------
		raf.seek( raf.length() - 32 );
		
		byte[] b = new byte[8];
		raf.read(b);
		
		String tagS = new String( b );
		if(!tagS.equals( "APETAGEX" )){
			throw new CannotReadException("There is no APE Tag in this file");
		}
		//Parse the tag -)------------------------------------------------
		//Version
		b = new byte[4];
		raf.read( b );
		int version = Utils.getNumber(b, 0,3);
		if(version != 2000) {
			throw new CannotReadException("APE Tag other than version 2.0 are not supported");
		}
		
		//Size
		b = new byte[4];
		raf.read( b );
		long tagSize = Utils.getLongNumber(b, 0,3);

		//Number of items
		b = new byte[4];
		raf.read( b );
		int itemNumber = Utils.getNumber(b, 0,3);
		
		//Tag Flags
		b = new byte[4];
		raf.read( b );
		//TODO handle these
		
		raf.seek(raf.length() - tagSize);
		
		for(int i = 0; i<itemNumber; i++) {
			//Content length
			b = new byte[4];
			raf.read( b );
			int contentLength = Utils.getNumber(b, 0,3);
			if(contentLength > 500000)
				throw new CannotReadException("Item size is much too large: "+contentLength+" bytes");
			
			//Item flags
			b = new byte[4];
			raf.read( b );
			//TODO handle these
			boolean binary = ((b[0]&0x06) >> 1) == 1;
			
			int j = 0;
			while(raf.readByte() != 0)
				j++;
			raf.seek(raf.getFilePointer() - j -1);
			int fieldSize = j;
			
			//Read Item key
			b = new byte[fieldSize];
			raf.read( b );
			raf.skipBytes(1);
			String field = new String(b);
			
			//Read Item content
			b = new byte[contentLength];
			raf.read( b );
			if(!binary)
			    tag.add(new ApeTagTextField(field, new String(b, "UTF-8")));
			else
			    tag.add(new ApeTagBinaryField(field, b));
		}
		
		return tag;
	} 
}
