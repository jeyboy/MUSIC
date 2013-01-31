package outag.audioformats.mp4;

import java.io.IOException;
import java.io.RandomAccessFile;

import outag.audioformats.EncodingInfo;
import outag.audioformats.Tag;
import outag.audioformats.exceptions.CannotReadException;
import outag.audioformats.generic.AudioFileReader;
import outag.audioformats.mp4.util.Mp4InfoReader;
import outag.audioformats.mp4.util.Mp4TagReader;

public class Mp4FileReader extends AudioFileReader {
    private Mp4InfoReader ir = new Mp4InfoReader();
	private Mp4TagReader tr = new Mp4TagReader();
	
	protected EncodingInfo getEncodingInfo( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ir.read(raf);
	}
	
	protected Tag getTag( RandomAccessFile raf )  throws CannotReadException, IOException {
		return tr.read(raf);
	}
}