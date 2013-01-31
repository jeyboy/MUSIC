package outag.audioformats.ogg;

import outag.audioformats.*;
import outag.audioformats.ogg.util.*;
import outag.audioformats.exceptions.*;
import outag.audioformats.generic.AudioFileReader;

import java.io.*;

public class OggFileReader extends AudioFileReader {
	private OggInfoReader ir = new OggInfoReader();
	private VorbisTagReader otr = new VorbisTagReader();
	
	protected EncodingInfo getEncodingInfo( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ir.read(raf);
	}
	
	protected Tag getTag( RandomAccessFile raf )  throws CannotReadException, IOException {
		return otr.read(raf);
	}
}