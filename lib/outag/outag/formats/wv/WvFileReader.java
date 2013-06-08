package outag.formats.wv;

import outag.formats.EncodingInfo;
import outag.formats.Tag;
import outag.formats.exceptions.CannotReadException;
import outag.formats.generic.AudioFileReader;
import outag.formats.generic.GenericTag;
import outag.formats.wv.util.WvInfoReader;

import java.io.*;

public class WvFileReader extends AudioFileReader {
	private WvInfoReader ir = new WvInfoReader();
	
	protected EncodingInfo getEncodingInfo(RandomAccessFile raf) throws Exception {
		return ir.read(raf);
	}
	
	protected Tag getTag(RandomAccessFile raf)  throws CannotReadException {
		return new GenericTag();
	}
}