package outag.formats.ape;

import outag.formats.EncodingInfo;
import outag.formats.Tag;
import outag.formats.ape.util.ApeTagReader;
import outag.formats.ape.util.MonkeyInfoReader;
import outag.formats.exceptions.*;
import outag.formats.generic.AudioFileReader;

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