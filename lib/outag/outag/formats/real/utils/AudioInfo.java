package outag.formats.real.utils;

import java.io.DataInputStream;

import outag.formats.exceptions.InvalidSequenceException;
import outag.formats.exceptions.UnsupportedException;
import outag.formats.real.RealTag;
 
public class AudioInfo {
	/** possible values : 3,4,5 */
	short version;
	public RealTag tag = null;
	public CodecInfo codecInfo = null;
	
	public AudioInfo(DataInputStream f) throws Exception {
		byte [] buffer;

		if (f.readInt() != 779248125) //['.', 'r', 'a', 0xfd]
			throw new InvalidSequenceException("AudioCodec info has wrong header");
	
		version = f.readShort(); //word     Version (3, 4 or 5)
		if (version < 3 || version > 5) throw new UnsupportedException("Wrong or unsupported version");

		if (version == 3) {
			f.readShort(); // headerSize not including first 8 bytes
			buffer = new byte[10]; f.read(buffer); //	 byte[10] Unknown
			f.readInt(); // Data size
			
			tag = new RealTag();
			
			buffer = new byte[f.read()]; f.read(buffer);
			tag.addTitle(new String(buffer));
			
			buffer = new byte[f.read()]; f.read(buffer);
			tag.addArtist(new String(buffer));			

			buffer = new byte[f.read()]; f.read(buffer);
			tag.setCopyright(new String(buffer));			

			buffer = new byte[f.read()]; f.read(buffer);
			tag.addComment(new String(buffer));			

			f.read(); //    Unknown *
			buffer = new byte[f.read()]; f.read(buffer);
			if (!new String(buffer).equals("lpcJ"))
				throw new InvalidSequenceException("AudioInfo sequence corrupted"); 
		} else {
			codecInfo = new CodecInfo();
			
			f.readShort(); // Unused (always 0)
			buffer = new byte[4]; f.read(buffer);
//		 	byte[4]  ra signature (".ra4" or ".ra5", depending on version)
			
			f.readInt(); // possible data size - 0x27
			f.readShort(); //  Version2 (always equal to version)
			f.readInt();//  Header size - 16
			
			codecInfo.setcodecFlavor(f.readShort());
			codecInfo.setcodecFrameSize(f.readInt());
			buffer = new byte[12]; f.read(buffer); //Unknow

			codecInfo.setsubpacketH(f.readShort());
			codecInfo.setframeSize(f.readShort());
			codecInfo.setsubpacketSize(f.readShort());
			
			f.readShort(); //   Unknown
			
			if (version == 5) {
				buffer = new byte[6]; f.read(buffer); //Unknow
			}
				
			codecInfo.setsampleRate(f.readShort());
			f.readShort(); // Unknow
			codecInfo.setsampleSize(f.readShort());
			codecInfo.setchannels(f.readShort());

			if (version == 4) {
				buffer = new byte[f.read()]; f.read(buffer); //Interleaver ID string // always 4 bytes
				buffer = new byte[f.read()]; f.read(buffer); //FourCC string // always 4 bytes
			} else {
				f.readInt(); // Interleaver ID / maybe need convert value to string 
				f.readInt(); // FourCC // FourCC is four character constant / maybe need convert value to string 
			}
			
			buffer = new byte[3]; f.read(buffer); //Unknow
			if (version == 5)
				f.read(); //Unknow

//TODO: Check exist of next block for version 4			
			if (version == 4) {
				tag = new RealTag();
				
				buffer = new byte[f.read()]; f.read(buffer);
				tag.addTitle(new String(buffer));
				
				buffer = new byte[f.read()]; f.read(buffer);
				tag.addArtist(new String(buffer));			

				buffer = new byte[f.read()]; f.read(buffer);
				tag.setCopyright(new String(buffer));			

				buffer = new byte[f.read()]; f.read(buffer);
				tag.addComment(new String(buffer));						
			}

//TODO: Check exist of next inclusions (maybe only for 5 version) 			 			
			buffer = new byte[(int)f.readInt()]; f.read(buffer);
			codecInfo.setcodecExtraData(buffer);
		}			
	}
}