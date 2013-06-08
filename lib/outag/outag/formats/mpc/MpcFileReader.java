package outag.formats.mpc;

import java.io.IOException;
import java.io.RandomAccessFile;

import outag.formats.EncodingInfo;
import outag.formats.Tag;
import outag.formats.ape.util.ApeTagReader;
import outag.formats.exceptions.CannotReadException;
import outag.formats.generic.AudioFileReader;
import outag.formats.mpc.util.MpcInfoReader;

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