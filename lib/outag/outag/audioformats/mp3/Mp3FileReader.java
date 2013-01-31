package outag.audioformats.mp3;

import java.io.*;

import outag.audioformats.EncodingInfo;
import outag.audioformats.Tag;
import outag.audioformats.exceptions.CannotReadException;
import outag.audioformats.generic.AudioFileReader;
import outag.audioformats.generic.GenericTag;
import outag.audioformats.mp3.util.Id3v1TagReader;
import outag.audioformats.mp3.util.Id3v2TagReader;
import outag.audioformats.mp3.util.Mp3InfoReader;

public class Mp3FileReader extends AudioFileReader {
	private Mp3InfoReader ir = new Mp3InfoReader();
	private Id3v2TagReader idv2tr = new Id3v2TagReader();
	private Id3v1TagReader idv1tr = new Id3v1TagReader();
	
	protected EncodingInfo getEncodingInfo( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ir.read(raf);
	}
	
	protected Tag getTag( RandomAccessFile raf )  throws IOException {
		Id3v2Tag v2 = null;
		Id3v1Tag v1 = null;
		
		try {
			v2 = idv2tr.read(raf);
		} catch(CannotReadException e) {
			v2 = null;
			e.printStackTrace();
		}
		
		try {
			v1 = idv1tr.read(raf);
		} catch(CannotReadException e) {
			v1 = null;
			e.printStackTrace();
		}

		if(v1 == null && v2 == null)
			return new GenericTag();
            
		
		if(v2 == null) {
			return v1;
		}
		else if(v1 != null) {
			v2.merge( v1 );
			v2.hasId3v1(true);
		}
		
		return v2;
	}
}