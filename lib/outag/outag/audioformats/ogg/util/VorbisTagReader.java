package outag.audioformats.ogg.util;

import outag.audioformats.*;
import outag.audioformats.ogg.*;
import outag.audioformats.exceptions.*;

import java.io.*;

public class VorbisTagReader {
	private OggTagReader oggTagReader = new OggTagReader();
	
	public Tag read( RandomAccessFile raf ) throws CannotReadException, IOException {
		long oldPos = 0;
		//----------------------------------------------------------
		
		//Check wheter we have an ogg stream---------------
		raf.seek( 0 );
		byte[] b = new byte[4];
		raf.read(b);
		
		String ogg = new String(b);
		if( !ogg.equals("OggS") )
			throw new CannotReadException("OggS Header could not be found, not an ogg stream");
		//--------------------------------------------------
		
		//Parse the tag ------------------------------------
		raf.seek( 0 );

		//Supposing 1st page = codec infos
		//			2nd page = comment+decode info
		//...Extracting 2nd page
		
		//1st page to get the length
		b = new byte[4];
		oldPos = raf.getFilePointer();
		raf.seek(26);
		int pageSegments = raf.readByte()&0xFF; //unsigned
		raf.seek(oldPos);
		
		b = new byte[27 + pageSegments];
		raf.read( b );

		OggPageHeader pageHeader = new OggPageHeader( b );

		raf.seek( raf.getFilePointer() + pageHeader.getPageLength() );

		//2nd page extraction
		oldPos = raf.getFilePointer();
		raf.seek(raf.getFilePointer() + 26);
		pageSegments = raf.readByte()&0xFF; //unsigned
		raf.seek(oldPos);
		
		b = new byte[27 + pageSegments];
		raf.read( b );
		pageHeader = new OggPageHeader( b );

		b = new byte[7];
		raf.read( b );
		
		String vorbis = new String(b, 1, 6);
		if(b[0] != 3 || !vorbis.equals("vorbis"))
			throw new CannotReadException("Cannot find comment block (no vorbis header)");

		//Begin tag reading
		OggTag tag = oggTagReader.read(raf);
		
		byte isValid = raf.readByte();
		if ( isValid == 0 )
			throw new CannotReadException("Error: The OGG Stream isn't valid, could not extract the tag");
		
		return tag;
	}
}