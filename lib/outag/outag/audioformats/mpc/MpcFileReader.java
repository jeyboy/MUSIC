package outag.audioformats.mpc;

import java.io.IOException;
import java.io.RandomAccessFile;

import outag.audioformats.EncodingInfo;
import outag.audioformats.Tag;
import outag.audioformats.ape.util.ApeTagReader;
import outag.audioformats.exceptions.CannotReadException;
import outag.audioformats.generic.AudioFileReader;
import outag.audioformats.mpc.util.MpcInfoReader;

public class MpcFileReader  extends AudioFileReader {
	private MpcInfoReader ir = new MpcInfoReader();
	private ApeTagReader tr = new ApeTagReader();
	
	protected EncodingInfo getEncodingInfo( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ir.read(raf);
	}
	
	protected Tag getTag( RandomAccessFile raf )  throws CannotReadException, IOException {
		return tr.read(raf);
	}
}