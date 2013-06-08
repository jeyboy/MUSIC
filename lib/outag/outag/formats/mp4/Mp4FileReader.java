package outag.formats.mp4;

import java.io.RandomAccessFile;

import outag.formats.EncodingInfo;
import outag.formats.Tag;
import outag.formats.generic.AudioFileReader;
import outag.formats.mp4.util.Mp4InfoReader;
import outag.formats.mp4.util.Mp4TagReader;

public class Mp4FileReader extends AudioFileReader {
    private Mp4InfoReader ir = new Mp4InfoReader();
	private Mp4TagReader tr = new Mp4TagReader();
	
	protected EncodingInfo getEncodingInfo( RandomAccessFile raf )  throws Exception {
		return ir.read(raf);
	}
	
	protected Tag getTag( RandomAccessFile raf )  throws Exception {
		return tr.read(raf);
	}
}