package outag.formats.real.utils;

import java.io.DataInputStream;

import outag.formats.exceptions.InvalidSequenceException;
import outag.formats.exceptions.UnsupportedException;
import outag.formats.real.RealTag;
 
public class LosslessAudioInfo {

//	bytes  0- 3 - codec FOURCC, always "LSD:"
//	bytes  4- 5 - version
//	bytes  8- 9 - number of channels
//	bytes 10-11 - bits per sample
//	bytes 12-15 - sampling rate
//	bytes 16-19 - block size in samples?
//	bytes 20-23 - ignored?
	
	
//	/** possible values : 3,4,5 */
//	short version;
//	public RealTag tag = null;
	public CodecInfo codecInfo = null;
	
	public LosslessAudioInfo(DataInputStream f) throws Exception {
		byte [] buffer;
		codecInfo = new CodecInfo();
		
		buffer = new byte[4]; f.read(buffer);
		
		f.readShort(); // version
		f.readShort(); //unknow
		
		codecInfo.setchannels(f.readShort());
		codecInfo.setsampleSize(f.readShort());
		codecInfo.setsampleRate(f.readInt());
		codecInfo.setsubpacketSize(f.readInt());
		f.readInt(); //Unknow
	}
}