package outag.audioformats.flac;

import outag.audioformats.*;
import outag.audioformats.exceptions.*;
import outag.audioformats.flac.util.FlacInfoReader;
import outag.audioformats.flac.util.FlacTagReader;

import java.io.*;

import outag.audioformats.generic.AudioFileReader;

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