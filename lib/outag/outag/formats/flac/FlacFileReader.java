package outag.formats.flac;


import java.io.*;

import outag.formats.*;
import outag.formats.exceptions.*;
import outag.formats.flac.util.FlacInfoReader;
import outag.formats.flac.util.FlacTagReader;
import outag.formats.generic.AudioFileReader;

public class FlacFileReader extends AudioFileReader {
	
	private FlacInfoReader ir = new FlacInfoReader();
	private FlacTagReader tr = new FlacTagReader();
	
	protected EncodingInfo getEncodingInfo( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ir.read(raf);
	}
	
	protected Tag getTag( RandomAccessFile raf )  throws CannotReadException, IOException {
		return tr.read(raf);
	}
}