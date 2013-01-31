package outag.audioformats.flac.util;

import outag.audioformats.exceptions.CannotReadException;
import outag.audioformats.ogg.OggTag;
import outag.audioformats.ogg.util.OggTagReader;

import java.io.*;

public class FlacTagReader {
	
	private OggTagReader oggTagReader = new OggTagReader();
	
	public OggTag read( RandomAccessFile raf ) throws CannotReadException, IOException {
		if ( raf.length()==0 ) throw new CannotReadException("Error: File empty");
		raf.seek( 0 );

		//FLAC Header string
		byte[] b = new byte[4];
		raf.read(b);
		String flac = new String(b);
		if(!flac.equals("fLaC")) throw new CannotReadException("fLaC Header not found, not a flac file");
		
		OggTag tag = null;
		
		//Seems like we hava a valid stream
		boolean isLastBlock = false;
		while(!isLastBlock) {
			b = new byte[4];
			raf.read(b);
			MetadataBlockHeader mbh = new MetadataBlockHeader(b);
		
			switch(mbh.getBlockType()) {
				//We got a vorbis comment block, parse it
				case MetadataBlockHeader.VORBIS_COMMENT : 	tag = handleVorbisComment(mbh, raf);
															mbh = null;
															return tag; //We have it, so no need to go further
				
				//This is not a vorbis comment block, we skip to next block
				default : 	raf.seek(raf.getFilePointer()+mbh.getDataLength());
							break;
			}

			isLastBlock = mbh.isLastBlock();
			mbh = null;
		}
		//FLAC not found...
		throw new CannotReadException("FLAC Tag could not be found or read..");
	}
	
	private OggTag handleVorbisComment(MetadataBlockHeader mbh, RandomAccessFile raf) throws IOException, CannotReadException {
		long oldPos = raf.getFilePointer();
		
		OggTag tag = oggTagReader.read(raf);
		
		long newPos = raf.getFilePointer();
		
		if(newPos - oldPos != mbh.getDataLength())
			throw new CannotReadException("Tag length do not match with flac comment data length");
		
		return tag;
	}
}