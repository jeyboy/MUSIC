package outag.formats.wav;

import outag.formats.EncodingInfo;
import outag.formats.Tag;
import outag.formats.exceptions.CannotReadException;
import outag.formats.generic.AudioFileReader;
import outag.formats.generic.GenericTag;
import outag.formats.wav.util.WavInfoReader;

import java.io.*;

public class WavFileReader extends AudioFileReader {
	private WavInfoReader ir = new WavInfoReader();
	
	protected EncodingInfo getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException {
		return ir.read(raf);
	}
	
	protected Tag getTag(RandomAccessFile raf)  throws CannotReadException {
		return new GenericTag();
	}
}