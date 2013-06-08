package outag.formats.wv.io;

import java.io.IOException;

import outag.file_presentation.Parseable;

//4 bytes - 'wvpk'
//32 bits - total block size (not counting this field or 'wvpk')
//16 bits - version (current valid versions are 0x402 - 0x410)
//8  bits - track number (not currently implemented)
//8  bits - track sub index (not currently implemented)
//32 bits - total samples in file (may be 0xFFFFFFFF if unknown)
//32 bits - offset in samples for current block (i.e. how many samples should be decoded by now)
//32 bits - samples in this block (may be 0 if no audio present)
//32 bits - flags
//32 bits - CRC

public class HeadChunk {
	int blockLength;
	public String version;
	byte trackNumber;
	byte trackSubIndex;
	int totalSamples; //only valid if block_index == 0 and a value of -1 indicates unknown length
	int samplesOffsetFroCurrentBlock;
	int totalSamplesInThisBlock;
	public HeadFlags flags;
	int crc;
	
	
	public HeadChunk(Parseable p) throws IOException, Exception {
		if (!p.Str(4).equals("wvpk"))
			throw new Exception("Wrong file format");
		
		blockLength = 8 + p.UInt();
		version = Integer.toHexString(p.UShort());
		trackNumber = p.UByte();
		trackSubIndex = p.UByte();
		totalSamples = p.UInt();
		
		samplesOffsetFroCurrentBlock = p.UInt();
		totalSamplesInThisBlock = p.UInt();
		
		flags = new HeadFlags(p);
		crc = p.UInt();
	}
	
	public float getDuration() {
		return (float) (flags.getSampleRate() > 0 ?
			(float) totalSamples / (float) flags.getSampleRate() + 0.5 
			: 0f);
	}
	
	/** @param streamLength - actual stream size without tags length */
	public int getAudioBitrate(int streamLength) {
		float duration = getDuration();
		return (int) (duration > 0 ?
			((streamLength * 8L) / duration) / 1000 : 0);
	}		
}