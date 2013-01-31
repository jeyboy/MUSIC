package outag.audioformats.wav;

import outag.audioformats.EncodingInfo;
import outag.audioformats.Tag;
import outag.audioformats.exceptions.CannotReadException;
import outag.audioformats.generic.AudioFileReader;
import outag.audioformats.generic.GenericTag;
import outag.audioformats.wav.util.WavInfoReader;

import java.io.*;

public class WavFileReader extends AudioFileReader {
	private WavInfoReader ir = new WavInfoReader();
	
	protected EncodingInfo getEncodingInfo( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ir.read(raf);
	}
	
	protected Tag getTag( RandomAccessFile raf )  throws CannotReadException {
		return new GenericTag();
	}
}