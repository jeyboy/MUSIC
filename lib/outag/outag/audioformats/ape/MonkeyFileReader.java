package outag.audioformats.ape;

import outag.audioformats.EncodingInfo;
import outag.audioformats.Tag;
import outag.audioformats.ape.util.ApeTagReader;
import outag.audioformats.ape.util.MonkeyInfoReader;
import outag.audioformats.exceptions.*;
import outag.audioformats.generic.AudioFileReader;

import java.io.*;

public class MonkeyFileReader extends AudioFileReader {
	private ApeTagReader ape = new ApeTagReader();
	private MonkeyInfoReader ir = new MonkeyInfoReader();
	
	protected EncodingInfo getEncodingInfo( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ir.read(raf);
	}
	
	protected Tag getTag( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ape.read(raf);
	}
}